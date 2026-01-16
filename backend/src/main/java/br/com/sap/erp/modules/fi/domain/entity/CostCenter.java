package br.com.sap.erp.modules.fi.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cost_centers", indexes = {
    @Index(name = "idx_cost_center_code", columnList = "code"),
    @Index(name = "idx_cost_center_tenant", columnList = "tenant_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostCenter extends TenantEntity {

    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CostCenter parent;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;
}
