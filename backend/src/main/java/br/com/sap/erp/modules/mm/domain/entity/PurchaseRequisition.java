package br.com.sap.erp.modules.mm.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_requisitions", indexes = {
    @Index(name = "idx_req_number", columnList = "requisition_number"),
    @Index(name = "idx_req_tenant", columnList = "tenant_id"),
    @Index(name = "idx_req_date", columnList = "requisition_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRequisition extends TenantEntity {

    @Column(name = "requisition_number", nullable = false, unique = true, length = 50)
    private String requisitionNumber;

    @Column(name = "requisition_date", nullable = false)
    private LocalDate requisitionDate;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private RequisitionStatus status = RequisitionStatus.DRAFT;

    @OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PurchaseRequisitionItem> items = new ArrayList<>();

    @Column(name = "total_amount", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public enum RequisitionStatus {
        DRAFT, APPROVED, REJECTED, CONVERTED
    }

    public void addItem(PurchaseRequisitionItem item) {
        items.add(item);
        item.setRequisition(this);
        recalculateTotal();
    }

    public void recalculateTotal() {
        totalAmount = items.stream()
                .map(PurchaseRequisitionItem::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
