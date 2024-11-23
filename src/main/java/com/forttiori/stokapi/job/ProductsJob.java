package com.forttiori.stokapi.job;

import com.forttiori.stokapi.infrastructure.integration.stok.StokIntegration;
import com.forttiori.stokapi.infrastructure.integration.stok.response.ProductIntegrationResponse;
import com.forttiori.stokapi.infrastructure.integration.stok.response.SectionsIntegrationResponse;
import com.forttiori.stokapi.infrastructure.repository.product.ProductEntity;
import com.forttiori.stokapi.infrastructure.repository.product.ProductRepository;
import com.forttiori.stokapi.infrastructure.repository.section.SectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductsJob {

    private final StokIntegration stokIntegration;
    private final ProductRepository productRepository;
    private final SectionRepository sectionRepository;

    private static final int LIMIT_SIMULTANEOUS_OPERATIONS = 5;


    @Scheduled(cron = "0 0 6 * * *")
    public void fetchAllProducts() {
        stokIntegration.getSections()
                .map(sectionsIntegrationResponse -> sectionsIntegrationResponse.data().stream()
                        .flatMap(department -> department.sections().stream())
                        .map(SectionsIntegrationResponse.Department.Section::sectionId).toList())
                .flatMap(this::fetchProducts)
                .map(products -> products.stream()
                        .map(ProductEntity::toEntity).toList())
                .flatMap(productRepository::createInsert)
                .doOnError(e -> log.error("Failed to fetch and insert products: {}", e.getMessage()))
                .onErrorResume(e -> Mono.empty()).block();
    }

    private Mono<List<ProductIntegrationResponse.Product>> fetchProducts(List<Integer> sectionIds) {
        return Flux.fromIterable(sectionIds).flatMap(this::fetchProducts, LIMIT_SIMULTANEOUS_OPERATIONS).flatMapIterable(products -> products).collectList();
    }

    private Mono<List<ProductIntegrationResponse.Product>> fetchProducts(Integer sectionId) {
        return stokIntegration.getProductsBySection(sectionId, 1).expand(productIntegrationResponse -> {
            if (productIntegrationResponse.paginator().total_pages() > productIntegrationResponse.paginator().page()) {
                return stokIntegration.getProductsBySection(sectionId, productIntegrationResponse.paginator().page() + 1);
            } else {
                return Mono.empty();
            }
        }).flatMapIterable(ProductIntegrationResponse::data).collectList();
    }
}
