package br.com.sap.erp.modules.sd.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales_orders", indexes = {
    @Index(name = "idx_so_number", columnList = "order_number"),
    @Index(name = "idx_so_tenant", columnList = "tenant_id"),
    @Index(name = "idx_so_customer", columnList = "customer_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrder extends TenantEntity {

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "customer_id")
    private java.util.UUID customerId;

    @Column(name = "customer_name", length = 255)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.DRAFT;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SalesOrderItem> items = new ArrayList<>();

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
        DRAFT, CONFIRMED, INVOICED, DELIVERED, CANCELLED
    }

    public void addItem(SalesOrderItem item) {
        items.add(item);
        item.setOrder(this);
        recalculateTotals();
    }

    public void recalculateTotals() {
        totalAmount = items.stream()
                .map(SalesOrderItem::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        finalAmount = totalAmount.subtract(discountAmount).add(taxAmount);
    }
}
