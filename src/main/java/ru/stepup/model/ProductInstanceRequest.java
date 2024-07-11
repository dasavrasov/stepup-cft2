package ru.stepup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductInstanceRequest {

    @JsonProperty("instanceId")
    private Integer instanceId;

    @NotNull
    @JsonProperty("productType")
    private String productType;
    @NotNull
    @JsonProperty("productCode")
    private String productCode;
    @NotNull
    @JsonProperty("registerType")
    private String registerType;
    @NotNull
    @JsonProperty("mdmCode")
    private String mdmCode;
    @NotNull
    @JsonProperty("contractNumber")
    private String contractNumber;
    @NotNull
    @JsonProperty("contractDate")
    private String contractDate;
    @NotNull
    @JsonProperty("priority")
    private Integer priority;

    @JsonProperty("interestRatePenalty")
    private Double interestRatePenalty;

    @JsonProperty("minimalBalance")
    private Double minimalBalance;

    @JsonProperty("thresholdAmount")
    private Double thresholdAmount;

    @JsonProperty("accountingDetails")
    private String accountingDetails;

    @JsonProperty("rateType")
    private String rateType;

    @JsonProperty("taxPercentageRate")
    private Double taxPercentageRate;

    @JsonProperty("technicalOverdraftLimitAmount")
    private Double technicalOverdraftLimitAmount;
    @NotNull
    @JsonProperty("contractId")
    private Integer contractId;
    @NotNull
    @JsonProperty("BranchCode")
    private String branchCode;
    @NotNull
    @JsonProperty("IsoCurrencyCode")
    private String isoCurrencyCode;

    @JsonProperty("urgencyCode")
    private String urgencyCode;

    @JsonProperty("ReferenceCode")
    private Integer referenceCode;

    @JsonProperty("additionalProperties")
    private AdditionalProperties additionalProperties;

    @JsonProperty("instanceArrangement")
    private List<InstanceArrangement> instanceArrangement;


    @Getter
    @Setter
    public static class AdditionalProperties {

        @JsonProperty("data")
        private List<Data> data;

        @Getter
        @Setter
        public static class Data {

            @JsonProperty("key")
            private String key;

            @JsonProperty("value")
            private String value;

            @JsonProperty("name")
            private String name;
        }
    }

    @Getter
    @Setter
    public static class InstanceArrangement {

        @JsonProperty("GeneralAgreementId")
        private String generalAgreementId;

        @JsonProperty("SupplementaryAgreementId")
        private String supplementaryAgreementId;

        @JsonProperty("arrangementType")
        private String arrangementType;

        @JsonProperty("shedulerJobId")
        private Integer shedulerJobId;
        @NotNull
        @JsonProperty("Number")
        private String number;
        @NotNull
        @JsonProperty("openingDate")
        private String openingDate;

        @JsonProperty("closingDate")
        private String closingDate;

        @JsonProperty("CancelDate")
        private String cancelDate;

        @JsonProperty("validityDuration")
        private Integer validityDuration;

        @JsonProperty("cancellationReason")
        private String cancellationReason;

        @JsonProperty("Status")
        private String status;

        @JsonProperty("interestCalculationDate")
        private String interestCalculationDate;

        @JsonProperty("interestRate")
        private Double interestRate;

        @JsonProperty("coefficient")
        private Double coefficient;

        @JsonProperty("coefficientAction")
        private String coefficientAction;

        @JsonProperty("minimumInterestRate")
        private Double minimumInterestRate;

        @JsonProperty("minimumInterestRateCoefficient")
        private String minimumInterestRateCoefficient;

        @JsonProperty("minimumInterestRateCoefficientAction")
        private String minimumInterestRateCoefficientAction;

        @JsonProperty("maximalnterestRate")
        private Integer maximalInterestRate;

        @JsonProperty("maximalnterestRateCoefficient")
        private Integer maximalInterestRateCoefficient;

        @JsonProperty("maximalnterestRateCoefficientAction")
        private String maximalInterestRateCoefficientAction;
    }
}