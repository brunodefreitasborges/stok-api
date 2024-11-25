package com.forttiori.stokapi.contract.product.response;

import com.forttiori.stokapi.infrastructure.repository.product.ProductEntity;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Builder
public record ProductResponse(
        LocalDate date,
        List<Product> products,
         Integer totalPages,
         Integer totalElements,
         Integer page,
         Integer size
) {
    @Builder
    public record Product(
            String id,
            Integer sectionId,
            String description,
            String image,
            Boolean available,
            BigDecimal price,
            String barcode,
            String sku,
            Boolean onSale,
            Sale sale
    ) {
        @Builder
        public record Sale(
                BigDecimal originalPrice,
                BigDecimal salePrice
        ) {
            public static Sale fromDomain(ProductEntity.Sale sale) {
                return Sale.builder()
                        .originalPrice(sale.originalPrice())
                        .salePrice(sale.salePrice())
                        .build();
            }
        }
        public static Product fromDomain(ProductEntity entity) {
            return Product.builder()
                    .id(entity.getId())
                    .barcode(entity.getBarcode())
                    .available(entity.getAvailable())
                    .sku(entity.getSku())
                    .description(entity.getDescription())
                    .image(entity.getImage())
                    .onSale(entity.getOnSale())
                    .price(entity.getPrice())
                    .sectionId(entity.getSectionId())
                    .sale(Optional.ofNullable(entity.getSale()).map(Sale::fromDomain).orElse(null))
                    .build();
        }

    }
    public static ProductResponse fromDomain(Page<ProductEntity> page, LocalDate date) {
        return ProductResponse.builder()
                .date(date)
                .products(page.getContent().stream().map(Product::fromDomain).toList())
                .size(page.getSize())
                .page(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements((int) page.getTotalElements())
                .build();
    }
}
