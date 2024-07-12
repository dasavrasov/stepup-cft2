package ru.stepup.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.stepup.model.*;
import ru.stepup.service.AccountService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/corporate-settlement-account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createInstance(@Valid @RequestBody ProductRegistryRequest request) {
        try {
            ProductRegistryResponse response = accountService.createInstance(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }    @ExceptionHandler({MethodArgumentNotValidException.class, Exception.class})
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