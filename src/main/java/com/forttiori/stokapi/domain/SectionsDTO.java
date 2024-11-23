package com.forttiori.stokapi.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record SectionsDTO(
        List<Section> sections
) {
    @Builder
    public record Section (
            Integer sectionId,
            String description
    ) {}
}
