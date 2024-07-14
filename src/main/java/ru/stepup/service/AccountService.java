package ru.stepup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public AccountService(AccountRepository accountRepository, ProductRegisterRepository productRegisterRepository, ProductRegisterTypeRepository productRegisterTypeRepository, AccountPoolRepository accountPoolRepository) {
        this.accountRepository = accountRepository;
        this.productRegisterRepository = productRegisterRepository;
        this.productRegisterTypeRepository = productRegisterTypeRepository;
        this.accountPoolRepository = accountPoolRepository;
    }

    public ProductRegistryResponse createInstance(ProductRegistryRequest request) throws Exception {
        // Шаг 2
        ProductRegister productRegister = productRegisterRepository.findByProductIdAndProductRegisterType_Value(request.getInstanceId(), request.getRegistryTypeCode());
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
            throw new Exception("No account found with the provided parameters");
        }
        AccountPool accountPool = accountPools.get(0);  // get the first returned record
        List<Account> accounts = accountRepository.findByAccountPool(accountPool);

        ProductRegister productRegistry = new ProductRegister();
        productRegister.setProductId(request.getInstanceId().longValue());
        productRegister.setProductRegisterType(productRegisterType);
        productRegister.setAccount(accounts.get(0));
        productRegister.setCurrencyCode(request.getCurrencyCode());
        productRegister.setState("OPEN");
//        productRegister.setAccountNumber(accountPool.getAccount().getNumber());
        productRegisterRepository.save(productRegister);

        ProductRegistryResponse response = new ProductRegistryResponse();
        ProductRegistryResponse.Data responseData = new ProductRegistryResponse.Data();
        if (productRegister.getAccount() != null) {
            responseData.setAccountId(productRegister.getAccount().getId().toString());
        }
        response.setData(responseData);
        return response;
    }
}