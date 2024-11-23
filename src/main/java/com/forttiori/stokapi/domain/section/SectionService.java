package com.forttiori.stokapi.domain.section;

import com.forttiori.stokapi.domain.section.dto.SectionDTO;
import com.forttiori.stokapi.infrastructure.integration.stok.StokIntegration;
import com.forttiori.stokapi.infrastructure.repository.section.SectionEntity;
import com.forttiori.stokapi.infrastructure.repository.section.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final StokIntegration stokIntegration;

    public Flux<SectionDTO> list() {
        return sectionRepository.findAll()
                .map(SectionEntity::toDomain);
    }

    // Method to updated sections when needed
    public Flux<SectionDTO> fetch() {
        return stokIntegration.getSections()
                .map(sectionsIntegrationResponse -> sectionsIntegrationResponse.data().stream()
                        .flatMap(department -> department.sections().stream())
                        .map(section -> SectionEntity.builder()
                                .sectionId(section.sectionId())
                                .description(section.description())
                                .build()).toList())
                .flatMapMany(sectionRepository::saveAll)
                .map(SectionEntity::toDomain);
    }
}
