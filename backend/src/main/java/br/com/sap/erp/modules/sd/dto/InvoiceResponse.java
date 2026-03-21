package br.com.sap.erp.modules.sd.dto;

import br.com.sap.erp.modules.sd.domain.entity.Invoice.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record InvoiceResponse(
        UUID id,
        String invoiceNumber,
        LocalDate invoiceDate,
        UUID customerId,
        String customerName,
        UUID salesOrderId,
        InvoiceStatus status,
        BigDecimal totalAmount,
        BigDecimal taxAmount,
        BigDecimal finalAmount,
        List<SalesItemResponse> items
) {
}
