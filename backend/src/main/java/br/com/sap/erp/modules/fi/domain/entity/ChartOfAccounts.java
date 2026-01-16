package br.com.sap.erp.modules.fi.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "chart_of_accounts", indexes = {
    @Index(name = "idx_chart_account_code", columnList = "code"),
    @Index(name = "idx_chart_account_tenant", columnList = "tenant_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartOfAccounts extends TenantEntity {

    @Column(name = "code", nullable = false, length = 20)
    private String code; // Ex: 1.1.01.001

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private AccountType type; // ATIVO, PASSIVO, RECEITA, DESPESA, PATRIMONIO

    @Enumerated(EnumType.STRING)
    @Column(name = "nature", nullable = false, length = 20)
    private AccountNature nature; // DEVEDORA, CREDORA

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ChartOfAccounts parent;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "balance", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    public enum AccountType {
        ATIVO, PASSIVO, RECEITA, DESPESA, PATRIMONIO
    }

    public enum AccountNature {
        DEVEDORA, CREDORA
    }
}
