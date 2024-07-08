package ru.stepup.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account_pool")
@Getter
@Setter
public class AccountPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "branch_code")
    private String branchCode;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "mdm_code")
    private String mdmCode;

    @Column(name = "priority_code")
    private String priorityCode;

    @Column(name = "registry_type_code")
    private String registryTypeCode;

}