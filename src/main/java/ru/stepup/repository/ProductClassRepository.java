package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.model.ProductClass;

public interface ProductClassRepository extends JpaRepository<ProductClass, Long> {
    ProductClass findByValue(String value);
}