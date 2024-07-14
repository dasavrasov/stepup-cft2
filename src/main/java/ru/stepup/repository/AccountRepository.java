package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.model.Account;
import ru.stepup.model.AccountPool;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByAccountPool(AccountPool accountPool);
}