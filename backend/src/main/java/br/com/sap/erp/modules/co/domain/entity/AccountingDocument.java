package br.com.sap.erp.modules.co.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounting_documents", indexes = {
    @Index(name = "idx_doc_number", columnList = "document_number"),
    @Index(name = "idx_doc_tenant", columnList = "tenant_id"),
    @Index(name = "idx_doc_date", columnList = "document_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountingDocument extends TenantEntity {

    @Column(name = "document_number", nullable = false, unique = true, length = 50)
    private String documentNumber;

    @Column(name = "document_date", nullable = false)
    private LocalDate documentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 50)
    private DocumentType documentType; // DIARIO, APURACAO, ENCERRAMENTO

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private DocumentStatus status = DocumentStatus.DRAFT;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AccountingEntry> entries = new ArrayList<>();

    @Column(name = "total_debit", precision = 19, scale = 2)
    @Builder.Default
    private java.math.BigDecimal totalDebit = java.math.BigDecimal.ZERO;

    @Column(name = "total_credit", precision = 19, scale = 2)
    @Builder.Default
    private java.math.BigDecimal totalCredit = java.math.BigDecimal.ZERO;

    @Column(name = "posted_at")
    private java.time.LocalDateTime postedAt;

    @Column(name = "posted_by")
    private java.util.UUID postedBy;

    public enum DocumentType {
        DIARIO, APURACAO, ENCERRAMENTO, REABERTURA
    }

    public enum DocumentStatus {
        DRAFT, POSTED, CANCELLED
    }

    public void addEntry(AccountingEntry entry) {
        entries.add(entry);
        entry.setDocument(this);
        recalculateTotals();
    }

    public void recalculateTotals() {
        totalDebit = entries.stream()
                .filter(e -> e.getEntryType() == AccountingEntry.EntryType.DEBIT)
                .map(AccountingEntry::getAmount)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        totalCredit = entries.stream()
                .filter(e -> e.getEntryType() == AccountingEntry.EntryType.CREDIT)
                .map(AccountingEntry::getAmount)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }

    public boolean isBalanced() {
        return totalDebit.compareTo(totalCredit) == 0;
    }
}
