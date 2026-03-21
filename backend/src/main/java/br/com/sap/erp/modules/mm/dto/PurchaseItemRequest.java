package br.com.sap.erp.modules.mm.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PurchaseItemRequest(
        UUID productId,
        BigDecimal quantity,
        BigDecimal unitPrice
) {
}
