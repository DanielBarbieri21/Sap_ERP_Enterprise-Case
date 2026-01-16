package br.com.sap.erp.modules.wm.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "booked_quantity", precision = 19, scale = 3)
    private BigDecimal bookedQuantity; // Quantidade no sistema

    @Column(name = "counted_quantity", precision = 19, scale = 3)
    private BigDecimal countedQuantity; // Quantidade contada

    @Column(name = "difference", precision = 19, scale = 3)
    private BigDecimal difference; // Diferença

    @Column(name = "location", length = 100)
    private String location;
}
