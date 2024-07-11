package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.model.Agreement;

public interface AgreementRepository extends JpaRepository<Agreement, Integer> {
    Agreement findByNumber(String number);
}