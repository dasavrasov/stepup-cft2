package ru.stepup.repository;

import org.springframework.data.repository.CrudRepository;
import ru.stepup.model.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {
}