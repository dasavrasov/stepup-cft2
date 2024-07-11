package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.model.ProductRegisterType;

public interface ProductRegisterTypeRepository extends JpaRepository<ProductRegisterType, Integer> {
}