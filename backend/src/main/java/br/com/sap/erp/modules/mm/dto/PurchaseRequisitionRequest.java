package br.com.sap.erp.modules.mm.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PurchaseRequisitionRequest(
        String requisitionNumber,
        LocalDate requisitionDate,
        String description,
        UUID supplierId,
        String supplierName,
        List<PurchaseItemRequest> items
) {
}
