package com.forttiori.stokapi.domain.section.dto;

import lombok.Builder;

@Builder
public record SectionDTO(
        String id,
        Integer sectionId,
        String description
) {
}
