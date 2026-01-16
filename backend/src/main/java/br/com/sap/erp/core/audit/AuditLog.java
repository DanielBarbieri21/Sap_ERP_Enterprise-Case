package br.com.sap.erp.core.audit;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs", indexes = {
    @Index(name = "idx_audit_user", columnList = "user_id"),
    @Index(name = "idx_audit_date", columnList = "action_date"),
    @Index(name = "idx_audit_entity", columnList = "entity_type, entity_id"),
    @Index(name = "idx_audit_tenant", columnList = "tenant_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog extends TenantEntity {

    @Column(name = "user_id", nullable = false)
    private java.util.UUID userId;

    @Column(name = "user_name", length = 255)
    private String userName;

    @Column(name = "action", nullable = false, length = 50)
    private String action; // CREATE, UPDATE, DELETE, VIEW, LOGIN, LOGOUT

    @Column(name = "entity_type", length = 100)
    private String entityType; // User, Company, PurchaseOrder, etc.

    @Column(name = "entity_id")
    private java.util.UUID entityId;

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "old_values", columnDefinition = "TEXT")
    private String oldValues; // JSON com valores antigos

    @Column(name = "new_values", columnDefinition = "TEXT")
    private String newValues; // JSON com valores novos

    @Column(name = "description", length = 1000)
    private String description;
}
