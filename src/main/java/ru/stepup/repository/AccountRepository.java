package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}