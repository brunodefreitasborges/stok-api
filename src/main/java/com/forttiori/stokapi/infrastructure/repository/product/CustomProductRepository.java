package com.forttiori.stokapi.infrastructure.repository.product;

import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CustomProductRepository {
    Mono<Void> createInsert(List<ProductEntity> products);

    Mono<Page<ProductEntity>> getPaginatedProducts(String date, int page, int size);

    Flux<String> getAvailableDates();
}
