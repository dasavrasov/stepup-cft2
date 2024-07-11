package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByNumber(String number);
}