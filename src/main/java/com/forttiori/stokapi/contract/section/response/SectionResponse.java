package com.forttiori.stokapi.contract.section.response;

import com.forttiori.stokapi.domain.section.dto.SectionDTO;
import lombok.Builder;

@Builder
public record SectionResponse(
         String id,
         Integer sectionId,
         String description
) {

    public static SectionResponse fromDomain(SectionDTO sectionDTO) {
        return SectionResponse.builder()
                .sectionId(sectionDTO.sectionId())
                .description(sectionDTO.description())
                .id(sectionDTO.id())
                .build();
    }
}
