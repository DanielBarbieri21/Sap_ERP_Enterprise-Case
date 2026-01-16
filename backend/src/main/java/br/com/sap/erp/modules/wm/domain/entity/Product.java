package br.com.sap.erp.modules.wm.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_product_code", columnList = "code"),
    @Index(name = "idx_product_tenant", columnList = "tenant_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends TenantEntity {

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "unit", length = 10)
    private String unit; // UN, KG, M, etc.

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private ProductType type; // MATERIAL, SERVICE, FINISHED_GOOD

    @Column(name = "cost_price", precision = 19, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "sale_price", precision = 19, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "min_stock", precision = 19, scale = 3)
    private BigDecimal minStock;

    @Column(name = "max_stock", precision = 19, scale = 3)
    private BigDecimal maxStock;

    @Column(name = "current_stock", precision = 19, scale = 3)
    @Builder.Default
    private BigDecimal currentStock = BigDecimal.ZERO;

    @Column(name = "requires_batch_control")
    @Builder.Default
    private Boolean requiresBatchControl = false;

    @Column(name = "requires_expiry_control")
    @Builder.Default
    private Boolean requiresExpiryControl = false;

    public enum ProductType {
        MATERIAL, SERVICE, FINISHED_GOOD
    }
}
