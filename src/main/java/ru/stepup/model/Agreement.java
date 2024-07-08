package ru.stepup.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "agreement")
@Getter
@Setter
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "general_agreement_id")
    private String generalAgreementId;

    @Column(name = "supplementary_agreement_id")
    private String supplementaryAgreementId;

    @Column(name = "arrangement_type")
    private String arrangementType;

    @Column(name = "sheduler_job_id")
    private Long shedulerJobId;

    @Column(name = "number")
    private String number;

    @Column(name = "opening_date")
    private LocalDateTime openingDate;

    @Column(name = "closing_date")
    private LocalDateTime closingDate;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

    @Column(name = "validity_duration")
    private Long validityDuration;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "status")
    private String status;

    @Column(name = "interest_calculation_date")
    private LocalDateTime interestCalculationDate;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "coefficient")
    private BigDecimal coefficient;

    @Column(name = "coefficient_action")
    private String coefficientAction;

    @Column(name = "minimum_interest_rate")
    private BigDecimal minimumInterestRate;

    @Column(name = "minimum_interest_rate_coefficient")
    private BigDecimal minimumInterestRateCoefficient;

    @Column(name = "minimum_interest_rate_coefficient_action")
    private String minimumInterestRateCoefficientAction;

    @Column(name = "maximal_interest_rate")
    private BigDecimal maximalInterestRate;

    @Column(name = "maximal_interest_rate_coefficient")
    private BigDecimal maximalInterestRateCoefficient;

    @Column(name = "maximal_interest_rate_coefficient_action")
    private String maximalInterestRateCoefficientAction;

}