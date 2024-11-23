package com.forttiori.stokapi.infrastructure.integration.stok;

import com.forttiori.stokapi.infrastructure.integration.stok.response.ProductIntegrationResponse;
import com.forttiori.stokapi.infrastructure.integration.stok.response.SectionsIntegrationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class StokIntegration {

    private final WebClient webClient;

    public Mono<SectionsIntegrationResponse> getSections() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("departamentos/arvore/filial/1/centro_distribuicao/3")
                        .build())
                .retrieve()
                .bodyToMono(SectionsIntegrationResponse.class);
    }

    public Mono<ProductIntegrationResponse> getProductsBySection(Integer sectionId, Integer page) {
        log.info("Fetching section {}, page {}", sectionId, page);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("secoes/{sectionId}/produtos/filial/1/centro_distribuicao/3/ativos")
                        .queryParam("page", page)
                        .build(sectionId))
                .retrieve()
                .bodyToMono(ProductIntegrationResponse.class);
    }

}
