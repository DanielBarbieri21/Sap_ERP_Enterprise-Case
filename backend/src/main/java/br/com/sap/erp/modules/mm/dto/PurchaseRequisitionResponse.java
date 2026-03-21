package br.com.sap.erp.modules.mm.dto;

import br.com.sap.erp.modules.mm.domain.entity.PurchaseRequisition.RequisitionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PurchaseRequisitionResponse(
        UUID id,
        String requisitionNumber,
        LocalDate requisitionDate,
        String description,
        RequisitionStatus status,
        BigDecimal totalAmount,
        List<PurchaseItemResponse> items
) {
}
