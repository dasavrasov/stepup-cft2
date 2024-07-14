package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.model.ProductRegister;

public interface ProductRegisterRepository extends JpaRepository<ProductRegister, Long> {
    ProductRegister findByProductIdAndProductRegisterType_Value(Long productId, String productRegisterTypeValue);
}