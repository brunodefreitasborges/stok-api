package com.forttiori.stokapi.infrastructure.repository.section;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends ReactiveMongoRepository<SectionEntity, String> {
}
