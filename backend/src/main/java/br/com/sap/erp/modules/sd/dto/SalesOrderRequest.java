package br.com.sap.erp.modules.sd.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record SalesOrderRequest(
        String orderNumber,
        LocalDate orderDate,
        UUID customerId,
        String customerName,
        BigDecimal discountAmount,
        BigDecimal taxAmount,
        List<SalesItemRequest> items
) {
}
