package br.com.sap.erp.modules.mm.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PurchaseItemResponse(
        UUID id,
        String productCode,
        String productDescription,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount,
        String unit
) {
}
