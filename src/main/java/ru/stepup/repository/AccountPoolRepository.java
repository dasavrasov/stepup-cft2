package ru.stepup.repository;

import org.springframework.data.repository.CrudRepository;
import ru.stepup.model.AccountPool;

public interface AccountPoolRepository extends CrudRepository<AccountPool, Integer> {
}