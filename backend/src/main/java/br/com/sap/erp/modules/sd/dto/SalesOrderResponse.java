package br.com.sap.erp.modules.sd.dto;

import br.com.sap.erp.modules.sd.domain.entity.SalesOrder.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record SalesOrderResponse(
        UUID id,
        String orderNumber,
        LocalDate orderDate,
        UUID customerId,
        String customerName,
        OrderStatus status,
        BigDecimal totalAmount,
        BigDecimal discountAmount,
        BigDecimal taxAmount,
        BigDecimal finalAmount,
        List<SalesItemResponse> items
) {
}
