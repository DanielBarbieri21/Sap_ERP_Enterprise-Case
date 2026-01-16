package br.com.sap.erp.modules.sd.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices", indexes = {
    @Index(name = "idx_invoice_number", columnList = "invoice_number"),
    @Index(name = "idx_invoice_tenant", columnList = "tenant_id"),
    @Index(name = "idx_invoice_customer", columnList = "customer_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice extends TenantEntity {

    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @Column(name = "customer_id")
    private java.util.UUID customerId;

    @Column(name = "customer_name", length = 255)
    private String customerName;

    @Column(name = "sales_order_id")
    private java.util.UUID salesOrderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();

    @Column(name = "total_amount", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "final_amount", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal finalAmount = BigDecimal.ZERO;

    public enum InvoiceStatus {
        DRAFT, ISSUED, CANCELLED
    }

    public void addItem(InvoiceItem item) {
        items.add(item);
        item.setInvoice(this);
        recalculateTotals();
    }

    public void recalculateTotals() {
        totalAmount = items.stream()
                .map(InvoiceItem::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        finalAmount = totalAmount.add(taxAmount);
    }
}
