package com.forttiori.stokapi.domain.product;

import com.forttiori.stokapi.infrastructure.repository.product.ProductEntity;
import com.forttiori.stokapi.infrastructure.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public Mono<Page<ProductEntity>> getPaginatedProducts(LocalDate selectedDate, int page, int size) {
        return productRepository.getPaginatedProducts(String.valueOf(selectedDate), page, size);
    }

    public Mono<List<LocalDate>> getAvailableDates() {
        return productRepository.getAvailableDates().collectList()
                .map(date -> date.stream().map(LocalDate::parse).toList());
    }

}
