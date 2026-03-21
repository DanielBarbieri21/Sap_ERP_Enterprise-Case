package br.com.sap.erp.modules.wm.dto;

import br.com.sap.erp.modules.wm.domain.entity.StockMovement.MovementType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record StockMovementResponse(
        UUID id,
        UUID productId,
        String productCode,
        String productName,
        LocalDate movementDate,
        MovementType movementType,
        BigDecimal quantity,
        BigDecimal unitCost,
        BigDecimal totalCost,
        String documentType,
        String documentNumber,
        String location,
        String notes
) {
}
