package com.forttiori.stokapi.infrastructure.repository.section;

import com.forttiori.stokapi.domain.section.dto.SectionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "sections")
public class SectionEntity {
    @Id
    private String id;
    private Integer sectionId;
    private String description;

    public SectionDTO toDomain() {
        return SectionDTO.builder()
                .id(id)
                .description(description)
                .sectionId(sectionId)
                .build();
    }
}
