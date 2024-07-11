package ru.stepup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stepup.model.Account;
import ru.stepup.model.ProductRegister;
import ru.stepup.model.ProductRegistryRequest;
import ru.stepup.model.ProductRegistryResponse;
import ru.stepup.repository.AccountRepository;
import ru.stepup.repository.ProductRegisterRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ProductRegisterRepository productRegisterRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, ProductRegisterRepository productRegisterRepository) {
        this.accountRepository = accountRepository;
        this.productRegisterRepository = productRegisterRepository;
    }

    public ProductRegistryResponse createInstance(ProductRegistryRequest request) throws Exception {
        // Шаг 2
        ProductRegister existingProductRegister = productRegisterRepository.findByProductIdAndType(request.getInstanceId(), request.getRegistryTypeCode());
        if (existingProductRegister != null) {
            throw new Exception("Параметр " + request.getRegistryTypeCode() + " тип регистра " + request.getRegistryTypeCode() + " уже существует для ЭП с ИД " + request.getInstanceId());
        }

        ProductRegistryResponse response = new ProductRegistryResponse();
        // set response fields based on created account
        // response.setField(account.getField());

        return response;
    }
}