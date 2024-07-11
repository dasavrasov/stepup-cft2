package ru.stepup.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.stepup.model.ProductInstanceRequest;
import ru.stepup.model.ProductInstanceResponse;
import ru.stepup.service.ProductService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/corporate-settlement-instance")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createInstance(@Valid @RequestBody ProductInstanceRequest request) {
        try {
            ProductInstanceResponse response = productService.createInstance(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }
    @ExceptionHandler({MethodArgumentNotValidException.class, Exception.class})
    public ResponseEntity<String> handleValidationExceptions(Exception ex) {
        String errorMessage;
        if (ex instanceof MethodArgumentNotValidException) {
            errorMessage = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().stream()
                    .map(error -> "Имя обязательного параметра " + error.getField() + " не заполнено")
                    .collect(Collectors.joining(", "));
        } else {
            errorMessage = ex.getMessage();
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}