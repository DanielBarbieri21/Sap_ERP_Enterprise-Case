package br.com.sap.erp.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class TenantEntity extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @PrePersist
    @PreUpdate
    protected void setTenantId() {
        // Será preenchido pelo TenantContext
        if (this.tenantId == null) {
            this.tenantId = TenantContext.getCurrentTenantId();
        }
    }
}
