package ru.stepup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stepup.model.Account;
import ru.stepup.model.ProductRegister;
import ru.stepup.model.ProductRegisterType;
import ru.stepup.model.State;
import ru.stepup.repository.ProductRegisterRepository;

@Service
public class ProductRegisterService {

    private final ProductRegisterRepository productRegisterRepository;

    @Autowired
    public ProductRegisterService(ProductRegisterRepository productRegisterRepository) {
        this.productRegisterRepository = productRegisterRepository;
    }

    public ProductRegister createAndSaveProductRegister(Long productId, ProductRegisterType productRegisterType, Account account, String currencyCode, State state, String accountNumber) {
        ProductRegister productRegister = new ProductRegister();
        productRegister.setProductId(productId);
        productRegister.setProductRegisterType(productRegisterType);
        productRegister.setAccount(account);
        productRegister.setCurrencyCode(currencyCode);
        productRegister.setState(state);
        productRegister.setAccountNumber(accountNumber);
        return productRegisterRepository.save(productRegister);
    }
}