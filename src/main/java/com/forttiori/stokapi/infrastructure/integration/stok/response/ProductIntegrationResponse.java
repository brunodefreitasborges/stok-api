package com.forttiori.stokapi.infrastructure.integration.stok.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public record ProductIntegrationResponse(
        Boolean success,
        List<Product> data,
        Paginator paginator
) {
    public record Product(
            @JsonProperty("classificacao_mercadologica_id")
            Integer sectionId,
            @JsonProperty("descricao")
            String description,
            @JsonProperty("imagem")
            String image,
            @JsonProperty("disponivel")
            Boolean available,
            @JsonProperty("preco")
            BigDecimal price,
            @JsonProperty("codigo_barras")
            String barcode,
            @JsonProperty("sku")
            String sku,
            @JsonProperty("em_oferta")
            Boolean onSale,
            @JsonProperty("oferta")
            Sale sale
    ) {
        public record Sale(
                @JsonProperty("preco_antigo")
                BigDecimal originalPrice,
                @JsonProperty("preco_oferta")
                BigDecimal salePrice
        ) {
        }
    }

    public record Paginator(
            Integer page,
            Integer items_per_page,
            Integer total_pages,
            Integer total_items
    ) {}

}