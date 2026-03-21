package br.com.sap.erp.modules.wm.dto;

import br.com.sap.erp.modules.wm.domain.entity.StockMovement.MovementType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record StockMovementRequest(
        UUID productId,
        LocalDate movementDate,
        MovementType movementType,
        BigDecimal quantity,
        BigDecimal unitCost,
        String documentType,
        UUID documentId,
        String documentNumber,
        String batchNumber,
        LocalDate expiryDate,
        String location,
        String notes
) {
}
