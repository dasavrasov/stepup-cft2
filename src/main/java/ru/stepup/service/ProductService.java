package ru.stepup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stepup.model.ProductInstanceRequest;
import ru.stepup.model.ProductInstanceResponse;
import ru.stepup.model.Product;
import ru.stepup.model.ProductRegister;
import ru.stepup.repository.ProductRegisterRepository;
import ru.stepup.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductRegisterRepository productRegisterRepository;
    // other repositories

    @Autowired
    public ProductService(ProductRepository productRepository, ProductRegisterRepository productRegisterRepository /*, other repositories */) {
        this.productRepository = productRepository;
        this.productRegisterRepository = productRegisterRepository;
        // initialize other repositories
    }

    public ProductInstanceResponse createInstance(ProductInstanceRequest request) throws Exception {

        // Parse ProductInstanceRequest and map fields to model classes
        // Create and save entities

         Product product = new Product();
        // set product fields from request
        // product.setField(request.getField());
        productRepository.save(product);

        ProductInstanceResponse response = new ProductInstanceResponse();
        // set response fields based on created entities
        // response.setField(entity.getField());
        return response;
    }
}