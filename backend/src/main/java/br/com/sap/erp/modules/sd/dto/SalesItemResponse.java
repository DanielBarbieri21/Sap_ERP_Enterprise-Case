package br.com.sap.erp.modules.sd.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SalesItemResponse(
        UUID id,
        String productCode,
        String productDescription,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount,
        String unit
) {
}
