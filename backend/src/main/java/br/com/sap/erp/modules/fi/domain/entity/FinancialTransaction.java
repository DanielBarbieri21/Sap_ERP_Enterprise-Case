package br.com.sap.erp.modules.fi.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_transactions", indexes = {
    @Index(name = "idx_transaction_date", columnList = "transaction_date"),
    @Index(name = "idx_transaction_tenant", columnList = "tenant_id"),
    @Index(name = "idx_transaction_type", columnList = "type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialTransaction extends TenantEntity {

    @Column(name = "document_number", length = 50)
    private String documentNumber;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private TransactionType type; // RECEITA, DESPESA

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private ChartOfAccounts account;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "paid_amount", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "installment_number")
    private Integer installmentNumber;

    @Column(name = "total_installments")
    private Integer totalInstallments;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod; // DINHEIRO, PIX, BOLETO, CARTAO, etc.

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public enum TransactionType {
        RECEITA, DESPESA
    }

    public enum TransactionStatus {
        PENDING, PAID, PARTIAL, CANCELLED, OVERDUE
    }

    public void markAsPaid(BigDecimal amount) {
        this.paidAmount = amount;
        if (this.paidAmount.compareTo(this.amount) >= 0) {
            this.status = TransactionStatus.PAID;
            this.paymentDate = LocalDate.now();
        } else if (this.paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.status = TransactionStatus.PARTIAL;
        }
    }
}
