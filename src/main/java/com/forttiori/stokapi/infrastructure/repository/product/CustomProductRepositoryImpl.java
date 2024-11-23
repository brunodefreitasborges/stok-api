package com.forttiori.stokapi.infrastructure.repository.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    public Mono<Page<ProductEntity>> getPaginatedProducts(String date, int page, int size) {
        Mono<Long> totalCount = reactiveMongoTemplate.count(
                Query.query(new Criteria()),
                date
        );

        Flux<ProductEntity> products = reactiveMongoTemplate.find(
                Query.query(new Criteria())
                        .skip((long) page * size)
                        .limit(size),
                ProductEntity.class,
                date
        );

        return Mono.zip(totalCount, products.collectList())
                .map(tuple -> {
                    long total = tuple.getT1();
                    List<ProductEntity> productList = tuple.getT2();
                    Pageable pageable = PageRequest.of(page, size);
                    return new PageImpl<>(productList, pageable, total);
                });
    }

    @Override
    public Flux<String> getAvailableDates() {
        return reactiveMongoTemplate.getCollectionNames();
    }
}
