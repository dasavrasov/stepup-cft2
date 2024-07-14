package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.model.AccountPool;

import java.util.List;

public interface AccountPoolRepository extends JpaRepository<AccountPool, Integer> {
    List<AccountPool> findByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCode(String branchCode, String currencyCode, String mdmCode, String priorityCode, String registryTypeCode);
}
