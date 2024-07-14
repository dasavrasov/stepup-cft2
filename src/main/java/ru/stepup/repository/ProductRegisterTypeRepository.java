package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.model.ProductRegisterType;

import java.util.List;

public interface ProductRegisterTypeRepository extends JpaRepository<ProductRegisterType, Integer> {
    List<ProductRegisterType> findByAccountTypeAndProductClass_Value(String accountType, String productClassValue);
    ProductRegisterType findByValue(String value);
}