package br.com.sap.erp.modules.sd.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SalesItemRequest(
        UUID productId,
        BigDecimal quantity,
        BigDecimal unitPrice
) {
}
