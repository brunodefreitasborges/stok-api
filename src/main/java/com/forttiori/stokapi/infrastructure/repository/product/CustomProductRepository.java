package com.forttiori.stokapi.infrastructure.repository.product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CustomProductRepository {
    Mono<Void> createInsert(List<ProductEntity> products);

    Flux<ProductEntity> getAll(String date);

    Flux<String> getAvailableDates();
}
