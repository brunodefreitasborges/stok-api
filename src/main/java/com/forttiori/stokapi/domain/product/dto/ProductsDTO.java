package com.forttiori.stokapi.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsDTO {

    private List<Product> products;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Product {
        private String productName;
    }
}
