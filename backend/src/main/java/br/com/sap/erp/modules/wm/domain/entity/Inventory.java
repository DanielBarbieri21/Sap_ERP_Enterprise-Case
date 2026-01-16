package br.com.sap.erp.modules.wm.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inventories", indexes = {
    @Index(name = "idx_inv_number", columnList = "inventory_number"),
    @Index(name = "idx_inv_tenant", columnList = "tenant_id"),
    @Index(name = "idx_inv_date", columnList = "inventory_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory extends TenantEntity {

    @Column(name = "inventory_number", nullable = false, unique = true, length = 50)
    private String inventoryNumber;

    @Column(name = "inventory_date", nullable = false)
    private LocalDate inventoryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private InventoryStatus status = InventoryStatus.DRAFT;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InventoryItem> items = new ArrayList<>();

    public enum InventoryStatus {
        DRAFT, IN_PROGRESS, COMPLETED, CANCELLED
    }

    public void addItem(InventoryItem item) {
        items.add(item);
        item.setInventory(this);
    }
}
