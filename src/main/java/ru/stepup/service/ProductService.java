package ru.stepup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stepup.model.InstanceRequest;
import ru.stepup.model.InstanceResponse;
import ru.stepup.model.Product;
import ru.stepup.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public InstanceResponse createInstance(InstanceRequest request) {
        // Parse InstanceRequest and map fields to model classes
        Product product = new Product();
        // set product fields from request
        // product.setField(request.getField());
        productRepository.save(product);

        InstanceResponse response = new InstanceResponse();
        // set response fields based on created entities
        // response.setField(entity.getField());
        return response;
    }
}