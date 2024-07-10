package ru.stepup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class InstanceResponse {

    @JsonProperty("data")
    private Data data;

    @Getter
    @Setter
    public static class Data {

        @JsonProperty("instanceId")
        private String instanceId;

        @JsonProperty("registerId")
        private List<String> registerId;

        @JsonProperty("supplementaryAgreementId")
        private List<String> supplementaryAgreementId;

    }
}