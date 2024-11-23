package com.forttiori.stokapi.contract;

import com.forttiori.stokapi.domain.ProductService;
import com.forttiori.stokapi.infrastructure.integration.stok.response.ProductIntegrationResponse;
import com.forttiori.stokapi.infrastructure.repository.product.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("v1/test")
@RequiredArgsConstructor
public class TestController {

    private final ProductService productService;

    @GetMapping
    public Flux<ProductEntity> hello() {
        return productService.getProducts();
    }

}
