package com.forttiori.stokapi.domain;

import com.forttiori.stokapi.infrastructure.integration.stok.StokIntegration;
import com.forttiori.stokapi.infrastructure.integration.stok.response.ProductIntegrationResponse;
import com.forttiori.stokapi.infrastructure.repository.product.ProductEntity;
import com.forttiori.stokapi.infrastructure.repository.product.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final StokIntegration stokIntegration;
    private final ProductRepository productRepository;

    private static final int LIMIT_SIMULTANEOUS_OPERATIONS = 5;

    public Flux<ProductEntity> getProducts() {
        return productRepository.getAll(String.valueOf(LocalDate.now()));
    }

    public Mono<Void> fetchAllProducts() {
        return stokIntegration.getSections()
                .map(sectionsDTO -> sectionsDTO.sections().stream().map(SectionsDTO.Section::sectionId).toList())
                .flatMap(this::fetchProducts)
                .map(products -> products.stream().map(ProductEntity::toEntity).toList())
                .flatMap(productRepository::createInsert);
    }

    private Mono<List<ProductIntegrationResponse.Product>> fetchProducts(List<Integer> sectionIds) {
        return Flux.fromIterable(sectionIds)
                .flatMap(this::fetchProducts, LIMIT_SIMULTANEOUS_OPERATIONS)
                .flatMapIterable(products -> products)
                .collectList();
    }

    private Mono<List<ProductIntegrationResponse.Product>> fetchProducts(Integer sectionId) {
        return stokIntegration.getProductsBySection(sectionId, 1)
                .expand(productIntegrationResponse -> {
                    if(productIntegrationResponse.paginator().total_pages() > productIntegrationResponse.paginator().page()) {
                        return stokIntegration.getProductsBySection(sectionId, productIntegrationResponse.paginator().page() + 1);
                    } else {
                        return Mono.empty();
                    }
                })
                .flatMapIterable(ProductIntegrationResponse::data)
                .collectList();
    }
}
