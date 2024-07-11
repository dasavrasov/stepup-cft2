package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.model.AccountPool;

public interface AccountPoolRepository extends JpaRepository<AccountPool, Integer> {
}