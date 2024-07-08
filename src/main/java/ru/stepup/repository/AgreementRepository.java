package ru.stepup.repository;

import org.springframework.data.repository.CrudRepository;
import ru.stepup.model.Agreement;

public interface AgreementRepository extends CrudRepository<Agreement, Integer> {
}