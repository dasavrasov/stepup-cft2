package ru.stepup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepup.model.*;
import ru.stepup.repository.AccountPoolRepository;
import ru.stepup.repository.AccountRepository;
import ru.stepup.repository.ProductRegisterRepository;
import ru.stepup.repository.ProductRegisterTypeRepository;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final ProductRegisterRepository productRegisterRepository;
    private final ProductRegisterTypeRepository productRegisterTypeRepository;
    private final AccountPoolRepository accountPoolRepository;

    @Autowired
    private ProductRegisterService productRegisterService;

    @Autowired
    public AccountService(AccountRepository accountRepository, ProductRegisterRepository productRegisterRepository, ProductRegisterTypeRepository productRegisterTypeRepository, AccountPoolRepository accountPoolRepository) {
        this.accountRepository = accountRepository;
        this.productRegisterRepository = productRegisterRepository;
        this.productRegisterTypeRepository = productRegisterTypeRepository;
        this.accountPoolRepository = accountPoolRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductRegistryResponse createInstance(ProductRegistryRequest request) throws Exception {
        ProductRegister productRegister=null;
        // Шаг 2
        productRegister = productRegisterRepository.findByProductIdAndProductRegisterType_Value(request.getInstanceId(), request.getRegistryTypeCode());
        if (productRegister != null) {
            throw new Exception("Параметр " + request.getRegistryTypeCode() + " тип регистра " + request.getRegistryTypeCode() + " уже существует для ЭП с ИД " + productRegister.getProductId());
        }
        //Шаг 3. Взять занчение из RequestBody.registryTypeCode и найти в таблице product_register_type.value
        ProductRegisterType productRegisterType = productRegisterTypeRepository.findByValue(request.getRegistryTypeCode());
        if (productRegisterType == null) {
            throw new Exception("Код продукта " + request.getRegistryTypeCode() + " не найден в каталоге продуктов tpp_ref_product_register_type для данного типа Регистра");
        }
        //Шаг 4. Найти значение номера счета по параметрам branchCode, currencyCode, mdmCode, priorutyCode, registrTypeCode из RequstBody
        List<AccountPool> accountPools = accountPoolRepository.findByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCode(request.getBranchCode(), request.getCurrencyCode(), request.getMdmCode(), request.getPriorityCode(), request.getRegistryTypeCode());
        if (accountPools.isEmpty()) {
            throw new Exception("Не найден пул счетов для branchCode=" + request.getBranchCode() + ", currencyCode=" + request.getCurrencyCode() + ", mdmCode=" + request.getMdmCode() + ", priorityCode=" + request.getPriorityCode() + ", registryTypeCode=" + request.getRegistryTypeCode() + " в таблице account_pool");
        }
        AccountPool accountPool = accountPools.get(0);  // get the first returned record
        List<Account> accounts = accountRepository.findByAccountPool(accountPool);

        productRegister=productRegisterService.createAndSaveProductRegister(request.getInstanceId(), productRegisterType, accounts.get(0), request.getCurrencyCode(), State.OPEN, accounts.get(0).getAccountNumber());

        ProductRegistryResponse response = new ProductRegistryResponse();
        ProductRegistryResponse.Data responseData = new ProductRegistryResponse.Data();
        if (productRegister.getAccount() != null) {
            responseData.setAccountId(productRegister.getAccount().getId().toString());
        }
        response.setData(responseData);
        return response;
    }
}