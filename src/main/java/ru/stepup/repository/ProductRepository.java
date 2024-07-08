package ru.stepup.repository;

import org.springframework.data.repository.CrudRepository;
import ru.stepup.model.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}