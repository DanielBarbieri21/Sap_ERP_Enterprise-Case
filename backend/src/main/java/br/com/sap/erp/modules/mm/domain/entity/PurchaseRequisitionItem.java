package br.com.sap.erp.modules.mm.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "purchase_requisition_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRequisitionItem extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requisition_id", nullable = false)
    private PurchaseRequisition requisition;

    @Column(name = "product_code", length = 50)
    private String productCode;

    @Column(name = "product_description", nullable = false, length = 500)
    private String productDescription;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 3)
    private BigDecimal quantity;

    @Column(name = "unit_price", precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "unit", length = 10)
    private String unit; // UN, KG, M, etc.

    @PostPersist
    @PostUpdate
    private void calculateTotal() {
        if (quantity != null && unitPrice != null) {
            totalAmount = quantity.multiply(unitPrice);
        }
    }
}
