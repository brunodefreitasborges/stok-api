package com.forttiori.stokapi.contract.product;

import com.forttiori.stokapi.contract.product.response.AvailableDatesResponse;
import com.forttiori.stokapi.contract.product.response.ProductResponse;
import com.forttiori.stokapi.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Mono<ProductResponse> getPaginatedProducts(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        LocalDate selectedDate = date != null ? date : LocalDate.now();
        return productService.getPaginatedProducts(selectedDate, page, size)
                .map(page1 -> ProductResponse.fromDomain(page1, selectedDate));
    }

    @GetMapping("dates")
    public Mono<AvailableDatesResponse> getAvailableDates() {
        return productService.getAvailableDates()
                .map(AvailableDatesResponse::fromDomain);
    }

}
