package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.model.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByNumber(String number);
    Optional<Product> findById(Integer id);
}