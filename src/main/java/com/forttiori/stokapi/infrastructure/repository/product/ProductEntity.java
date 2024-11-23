package com.forttiori.stokapi.infrastructure.repository.product;

import com.forttiori.stokapi.infrastructure.integration.stok.response.ProductIntegrationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ProductEntity {

    @Id
    private String id;

    private Integer sectionId;

    @Indexed(unique = true)
    private String description;

    private String image;

    private Boolean available;

    private BigDecimal price;

    private String barcode;

    private String sku;

    private Boolean onSale;

    private Sale sale;

    @Builder
    public record Sale (
            BigDecimal originalPrice,
            BigDecimal salePrice
    ) {
        public static Sale toEntity(ProductIntegrationResponse.Product.Sale sale) {
            return Sale.builder()
                    .originalPrice(Optional.ofNullable(sale).map(ProductIntegrationResponse.Product.Sale::originalPrice).orElse(null))
                    .salePrice(Optional.ofNullable(sale).map(ProductIntegrationResponse.Product.Sale::salePrice).orElse(null))
                    .build();
        }
    }

    public static ProductEntity toEntity(ProductIntegrationResponse.Product product) {
        return ProductEntity.builder()
                .sectionId(product.sectionId())
                .description(product.description())
                .image(product.image())
                .available(product.available())
                .price(product.price())
                .barcode(product.barcode())
                .sku(product.sku())
                .onSale(product.onSale())
                .sale(Sale.toEntity(product.sale()))
                .build();
    }
}
