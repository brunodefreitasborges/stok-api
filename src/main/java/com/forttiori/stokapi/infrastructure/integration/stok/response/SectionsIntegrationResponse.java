package com.forttiori.stokapi.infrastructure.integration.stok.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SectionsIntegrationResponse(
        Boolean success,
        List<Department> data
) {

    public record Department(
            @JsonProperty("children")
            List<Section> sections
    ) {
        public record Section(
                @JsonProperty("classificacao_mercadologica_id")
                Integer sectionId,
                @JsonProperty("descricao")
                String description
        ) {
        }
    }
}
