package com.forttiori.stokapi.infrastructure.integration.stok.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forttiori.stokapi.domain.dto.SectionsDTO;

import java.util.List;

public record SectionsIntegrationResponse(
        Boolean success,
        List<Department> data
) {

    public SectionsDTO toDomain() {
        return SectionsDTO.builder()
                .sections(data.stream().flatMap(department -> department.sections().stream()
                        .map(section -> SectionsDTO.Section.builder()
                                .sectionId(section.sectionId)
                                .description(section.description)
                                .build()))
                        .toList())
                .build();
    }

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
