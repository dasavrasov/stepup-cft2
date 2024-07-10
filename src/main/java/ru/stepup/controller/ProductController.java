package ru.stepup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stepup.model.InstanceRequest;
import ru.stepup.model.InstanceResponse;
import ru.stepup.model.Product;
import ru.stepup.service.ProductService;

@RestController
@RequestMapping("/corporate-settlement-instance")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<InstanceResponse> createInstance(@RequestBody InstanceRequest request) {
        InstanceResponse response = productService.createInstance(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}