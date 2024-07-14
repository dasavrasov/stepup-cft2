package ru.stepup.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tpp_product_register")
@Getter
@Setter
public class ProductRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "value", nullable = false)
    private ProductRegisterType productRegisterType;

    @ManyToOne
    @JoinColumn(name = "account")
    private Account account;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "state")
    private String state;

    @Column(name = "account_number")
    private String accountNumber;

}