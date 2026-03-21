package br.com.sap.erp.modules.mm.dto;

import br.com.sap.erp.modules.mm.domain.entity.PurchaseOrder.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PurchaseOrderResponse(
        UUID id,
        String orderNumber,
        LocalDate orderDate,
        LocalDate expectedDeliveryDate,
        UUID supplierId,
        String supplierName,
        OrderStatus status,
        BigDecimal totalAmount,
        BigDecimal discountAmount,
        BigDecimal taxAmount,
        BigDecimal finalAmount,
        List<PurchaseItemResponse> items
) {
}
