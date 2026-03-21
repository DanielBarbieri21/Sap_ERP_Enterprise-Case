package br.com.sap.erp.modules.wm.dto;

import br.com.sap.erp.modules.wm.domain.entity.Product.ProductType;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String code,
        String name,
        String description,
        String unit,
        ProductType type,
        BigDecimal costPrice,
        BigDecimal salePrice,
        BigDecimal minStock,
        BigDecimal maxStock,
        BigDecimal currentStock
) {
}
