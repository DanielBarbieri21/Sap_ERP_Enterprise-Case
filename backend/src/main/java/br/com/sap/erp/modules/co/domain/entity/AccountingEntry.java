package br.com.sap.erp.modules.co.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import br.com.sap.erp.modules.fi.domain.entity.ChartOfAccounts;
import br.com.sap.erp.modules.fi.domain.entity.CostCenter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "accounting_entries", indexes = {
    @Index(name = "idx_entry_date", columnList = "entry_date"),
    @Index(name = "idx_entry_tenant", columnList = "tenant_id"),
    @Index(name = "idx_entry_document", columnList = "document_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountingEntry extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private AccountingDocument document;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private ChartOfAccounts account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_center_id")
    private CostCenter costCenter;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false, length = 20)
    private EntryType entryType; // DEBIT, CREDIT

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public enum EntryType {
        DEBIT, CREDIT
    }
}
