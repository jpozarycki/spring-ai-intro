package com.jpozarycki.springaiintro.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record GetCapitalWithInfoResponse(@JsonPropertyDescription("Name of the capital") String capital,
                                         @JsonPropertyDescription("Region of the capital") String region,
                                         @JsonPropertyDescription("Number of citizens") Long population,
                                         @JsonPropertyDescription("Primary language")String language,
                                         @JsonPropertyDescription("Currency") String currency) {
}
