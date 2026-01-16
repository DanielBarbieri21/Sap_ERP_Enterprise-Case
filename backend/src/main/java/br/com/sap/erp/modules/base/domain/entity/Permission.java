package br.com.sap.erp.modules.base.domain.entity;

import br.com.sap.erp.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions", indexes = {
    @Index(name = "idx_permission_code", columnList = "code", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code; // Ex: USER_CREATE, USER_UPDATE, FINANCIAL_VIEW, etc.

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "module", length = 50)
    private String module; // Ex: BASE, FI, CO, MM, SD, etc.
}
