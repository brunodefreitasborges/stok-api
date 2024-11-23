package com.forttiori.stokapi.contract;

import com.forttiori.stokapi.contract.response.AvailableDatesResponse;
import com.forttiori.stokapi.contract.response.ProductResponse;
import com.forttiori.stokapi.domain.ProductService;
import com.forttiori.stokapi.infrastructure.repository.product.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Mono<ProductResponse> getProducts(@RequestParam(required = false) LocalDate date) {
        LocalDate selectedDate = date != null ? date : LocalDate.now();
        return productService.getProducts(selectedDate)
                .map(entities -> ProductResponse.fromDomain(entities, selectedDate));
    }

    @GetMapping("dates")
    public Mono<AvailableDatesResponse> getAvailableDates() {
        return productService.getAvailableDates()
                .map(AvailableDatesResponse::fromDomain);
    }

}
