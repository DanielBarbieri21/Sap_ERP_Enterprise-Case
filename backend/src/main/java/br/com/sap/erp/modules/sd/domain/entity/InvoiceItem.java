package br.com.sap.erp.modules.sd.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItem extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

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
    private String unit;

    @PostPersist
    @PostUpdate
    private void calculateTotal() {
        if (quantity != null && unitPrice != null) {
            totalAmount = quantity.multiply(unitPrice);
        }
    }
}
