package br.com.sap.erp.modules.mm.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders", indexes = {
    @Index(name = "idx_po_number", columnList = "order_number"),
    @Index(name = "idx_po_tenant", columnList = "tenant_id"),
    @Index(name = "idx_po_supplier", columnList = "supplier_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder extends TenantEntity {

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @Column(name = "supplier_id")
    private java.util.UUID supplierId;

    @Column(name = "supplier_name", length = 255)
    private String supplierName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.DRAFT;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PurchaseOrderItem> items = new ArrayList<>();

    @Column(name = "total_amount", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "final_amount", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal finalAmount = BigDecimal.ZERO;

    public enum OrderStatus {
        DRAFT, SENT, CONFIRMED, PARTIALLY_RECEIVED, RECEIVED, CANCELLED
    }

    public void addItem(PurchaseOrderItem item) {
        items.add(item);
        item.setOrder(this);
        recalculateTotals();
    }

    public void recalculateTotals() {
        totalAmount = items.stream()
                .map(PurchaseOrderItem::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        finalAmount = totalAmount.subtract(discountAmount).add(taxAmount);
    }
}
