package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.model.ProductRegister;

public interface ProductRegisterRepository extends JpaRepository<ProductRegister, Integer> {
    ProductRegister findByProductIdAndType(Integer productId, String type);
}