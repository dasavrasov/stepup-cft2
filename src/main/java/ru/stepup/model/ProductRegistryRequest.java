package ru.stepup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRegistryRequest {
    @NotNull
    @JsonProperty("instanceId")
    private Integer instanceId;

    @JsonProperty("sequence")
    private Integer sequence;

    @JsonProperty("registryTypeCode")
    private String registryTypeCode;

    @JsonProperty("accountType")
    private String accountType;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("branchCode")
    private String branchCode;

    @JsonProperty("priorityCode")
    private String priorityCode;

    @JsonProperty("mdmCode")
    private String mdmCode;

    @JsonProperty("clientCode")
    private String clientCode;

    @JsonProperty("trainRegion")
    private String trainRegion;

    @JsonProperty("counter")
    private String counter;

    @JsonProperty("salesCode")
    private String salesCode;

}