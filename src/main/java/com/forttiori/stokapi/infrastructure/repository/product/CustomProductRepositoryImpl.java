package com.forttiori.stokapi.infrastructure.repository.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomProductRepositoryImpl implements CustomProductRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<Void> createInsert(List<ProductEntity> products) {
        return reactiveMongoTemplate
                .createCollection(String.valueOf(LocalDate.now()), CollectionOptions.empty())
                .flatMapMany(collection -> reactiveMongoTemplate.insertAll(Mono.just(products), String.valueOf(LocalDate.now())))
                .then();
    }

    @Override
    public Flux<ProductEntity> getAll(String date) {
        return reactiveMongoTemplate.findAll(ProductEntity.class, date);
    }

    @Override
    public Flux<String> getAvailableDates() {
        return reactiveMongoTemplate.getCollectionNames();
    }
}
