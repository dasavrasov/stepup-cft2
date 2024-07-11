package ru.stepup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stepup.model.*;
import ru.stepup.repository.AgreementRepository;
import ru.stepup.repository.ProductRegisterRepository;
import ru.stepup.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductRegisterRepository productRegisterRepository;
    private final AgreementRepository agreementRepository;
    // other repositories

    @Autowired
    public ProductService(ProductRepository productRepository, ProductRegisterRepository productRegisterRepository, AgreementRepository agreementRepository /*, other repositories */) {
        this.productRepository = productRepository;
        this.productRegisterRepository = productRegisterRepository;
        this.agreementRepository = agreementRepository;
        // initialize other repositories
    }

    public ProductInstanceResponse createInstance(ProductInstanceRequest request) throws Exception {

        // Parse ProductInstanceRequest and map fields to model classes
        // Create and save entities
        if (request.getInstanceId()==null) {
            //создание ЭП

            //Шап 1.1 проверка tpp_product на дубли
            Product existingProduct = productRepository.findByNumber(request.getContractNumber());
            if (existingProduct != null) {
                throw new Exception("Параметр " + request.getContractNumber() + " договора " + existingProduct.getNumber() + " уже существует для ЭП с ИД " + request.getInstanceId());
            }
            //Шаг 1.2 проверка таблицы ДС (agreement) на дубли
            for (int i = 0; i < request.getInstanceArrangement().size(); i++) {
                Agreement existingAgreement = agreementRepository.findByNumber(request.getInstanceArrangement().get(i).getNumber());
                if (existingAgreement != null) {
                    throw new Exception("Параметр " + i + " Дополнительного соглашения (сделки) " + request.getInstanceArrangement().get(i).getNumber() + " уже существует для ЭП с ИД " + request.getInstanceId());
                }
            }

            Product product = new Product();
            productRepository.save(product);
        }
        else {
            //создаени дополнительного соглашения к ЭП
        }

        // set product fields from request
        // product.setField(request.getField());

        ProductInstanceResponse response = new ProductInstanceResponse();
        // set response fields based on created entities
        // response.setField(entity.getField());
        return response;
    }
}