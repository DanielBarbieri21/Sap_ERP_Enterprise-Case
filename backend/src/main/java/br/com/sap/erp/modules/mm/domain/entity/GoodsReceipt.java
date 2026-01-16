package br.com.sap.erp.modules.mm.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "goods_receipts", indexes = {
    @Index(name = "idx_gr_number", columnList = "receipt_number"),
    @Index(name = "idx_gr_tenant", columnList = "tenant_id"),
    @Index(name = "idx_gr_order", columnList = "purchase_order_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsReceipt extends TenantEntity {

    @Column(name = "receipt_number", nullable = false, unique = true, length = 50)
    private String receiptNumber;

    @Column(name = "receipt_date", nullable = false)
    private LocalDate receiptDate;

    @Column(name = "purchase_order_id")
    private java.util.UUID purchaseOrderId;

    @Column(name = "supplier_id")
    private java.util.UUID supplierId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ReceiptStatus status = ReceiptStatus.DRAFT;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<GoodsReceiptItem> items = new ArrayList<>();

    @Column(name = "total_amount", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public enum ReceiptStatus {
        DRAFT, CONFIRMED, CANCELLED
    }

    public void addItem(GoodsReceiptItem item) {
        items.add(item);
        item.setReceipt(this);
        recalculateTotal();
    }

    public void recalculateTotal() {
        totalAmount = items.stream()
                .map(GoodsReceiptItem::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
