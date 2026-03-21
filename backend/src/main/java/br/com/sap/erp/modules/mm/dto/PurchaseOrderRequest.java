package br.com.sap.erp.modules.mm.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PurchaseOrderRequest(
        String orderNumber,
        LocalDate orderDate,
        LocalDate expectedDeliveryDate,
        UUID supplierId,
        String supplierName,
        BigDecimal discountAmount,
        BigDecimal taxAmount,
        List<PurchaseItemRequest> items
) {
}
