package com.monitor.monitor.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties (ignoreUnknown = true)
public record BuscaSemEmissao(@JsonProperty("value") String value) {
}
