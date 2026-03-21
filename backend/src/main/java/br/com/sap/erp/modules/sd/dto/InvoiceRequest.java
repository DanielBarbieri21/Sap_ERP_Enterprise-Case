package br.com.sap.erp.modules.sd.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record InvoiceRequest(
        String invoiceNumber,
        LocalDate invoiceDate,
        UUID customerId,
        String customerName,
        UUID salesOrderId,
        BigDecimal taxAmount,
        List<SalesItemRequest> items
) {
}
