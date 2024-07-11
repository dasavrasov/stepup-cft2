package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.model.ProductClass;

public interface ProductClassRepository extends JpaRepository<ProductClass, Integer> {
    ProductClass findByValue(String value);
}