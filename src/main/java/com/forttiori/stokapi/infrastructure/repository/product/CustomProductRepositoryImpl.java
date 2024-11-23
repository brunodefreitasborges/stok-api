package com.forttiori.stokapi.infrastructure.repository.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Void> createInsert(List<ProductEntity> products) {
        return reactiveMongoTemplate
                .createCollection(String.valueOf(LocalDate.now()), CollectionOptions.empty())
                .flatMapMany(collection -> reactiveMongoTemplate.insertAll(Mono.just(products), String.valueOf(LocalDate.now())))
                .then();
    }

    public Flux<ProductEntity> getAll(String date) {
        return reactiveMongoTemplate.findAll(ProductEntity.class, date);
    }
}
