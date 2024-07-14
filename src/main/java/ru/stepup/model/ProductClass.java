package ru.stepup.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tpp_ref_product_class")
@Getter
@Setter
public class ProductClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "internal_id")
    private Long internalId;

    @Column(name = "value", unique = true, nullable = false)
    private String value;

    @Column(name = "gbi_code")
    private String gbiCode;

    @Column(name = "gbi_name")
    private String gbiName;

    @Column(name = "product_row_code")
    private String productRowCode;

    @Column(name = "product_row_name")
    private String productRowName;

    @Column(name = "subclass_code")
    private String subclassCode;

    @Column(name = "subclass_name")
    private String subclassName;
}