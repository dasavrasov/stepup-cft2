package ru.stepup.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tpp_ref_product_register_type")
@Getter
@Setter
public class ProductRegisterType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "internal_id")
    private Integer internalId;

    @Column(name = "value", unique = true, nullable = false)
    private String value;

    @Column(name = "register_type_name", nullable = false)
    private String registerTypeName;

    @ManyToOne
    @JoinColumn(name = "product_class_code", referencedColumnName = "value", nullable = false)
    private ProductClass productClass;

    @Column(name = "register_type_start_date")
    private LocalDateTime registerTypeStartDate;

    @Column(name = "register_type_end_date")
    private LocalDateTime registerTypeEndDate;

    @Column(name = "account_type")
    private String accountType;
}
