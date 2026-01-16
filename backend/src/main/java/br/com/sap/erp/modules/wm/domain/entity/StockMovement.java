package br.com.sap.erp.modules.wm.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock_movements", indexes = {
    @Index(name = "idx_movement_date", columnList = "movement_date"),
    @Index(name = "idx_movement_tenant", columnList = "tenant_id"),
    @Index(name = "idx_movement_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovement extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "movement_date", nullable = false)
    private LocalDate movementDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private MovementType movementType; // ENTRADA, SAIDA, AJUSTE

    @Column(name = "quantity", nullable = false, precision = 19, scale = 3)
    private BigDecimal quantity;

    @Column(name = "unit_cost", precision = 19, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "total_cost", precision = 19, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "document_type", length = 50)
    private String documentType; // ORDEM_COMPRA, ORDEM_VENDA, INVENTARIO, etc.

    @Column(name = "document_id")
    private java.util.UUID documentId;

    @Column(name = "document_number", length = 50)
    private String documentNumber;

    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "location", length = 100)
    private String location; // Endereçamento

    @Column(name = "notes", length = 500)
    private String notes;

    public enum MovementType {
        ENTRADA, SAIDA, AJUSTE
    }

    @PostPersist
    @PostUpdate
    private void calculateTotal() {
        if (quantity != null && unitCost != null) {
            totalCost = quantity.multiply(unitCost);
        }
    }
}
