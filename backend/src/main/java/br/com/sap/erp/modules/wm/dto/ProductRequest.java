package br.com.sap.erp.modules.wm.dto;

import br.com.sap.erp.modules.wm.domain.entity.Product.ProductType;

import java.math.BigDecimal;

public record ProductRequest(
        String code,
        String name,
        String description,
        String unit,
        ProductType type,
        BigDecimal costPrice,
        BigDecimal salePrice,
        BigDecimal minStock,
        BigDecimal maxStock,
        BigDecimal currentStock,
        Boolean requiresBatchControl,
        Boolean requiresExpiryControl
) {
}
