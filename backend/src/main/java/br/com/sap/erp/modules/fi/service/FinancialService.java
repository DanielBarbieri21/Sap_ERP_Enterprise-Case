package br.com.sap.erp.modules.fi.service;

import br.com.sap.erp.core.audit.Auditable;
import br.com.sap.erp.core.domain.TenantContext;
import br.com.sap.erp.core.exception.ResourceNotFoundException;
import br.com.sap.erp.modules.fi.domain.entity.ChartOfAccounts;
import br.com.sap.erp.modules.fi.domain.entity.FinancialTransaction;
import br.com.sap.erp.modules.fi.repository.ChartOfAccountsRepository;
import br.com.sap.erp.modules.fi.repository.FinancialTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinancialService {

    private final FinancialTransactionRepository transactionRepository;
    private final ChartOfAccountsRepository chartOfAccountsRepository;

    @Transactional
    @Auditable("CREATE")
    public FinancialTransaction createTransaction(FinancialTransaction transaction) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId != null) {
            transaction.setTenantId(tenantId);
        }

        ChartOfAccounts account = chartOfAccountsRepository.findById(transaction.getAccount().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Conta contabil nao encontrada"));

        transaction.setAccount(account);

        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDate.now());
        }

        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public List<FinancialTransaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return List.of();
        }
        return transactionRepository.findByDateRange(tenantId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<FinancialTransaction> getPendingTransactions() {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return List.of();
        }
        return transactionRepository.findByStatus(tenantId, FinancialTransaction.TransactionStatus.PENDING);
    }

    @Transactional
    @Auditable("UPDATE")
    public FinancialTransaction payTransaction(UUID transactionId, BigDecimal amount) {
        FinancialTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transacao nao encontrada"));

        transaction.markAsPaid(amount);
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = transactionRepository.getTotalByType(
                tenantId,
                FinancialTransaction.TransactionType.RECEITA,
                startDate,
                endDate
        );
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalExpenses(LocalDate startDate, LocalDate endDate) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = transactionRepository.getTotalByType(
                tenantId,
                FinancialTransaction.TransactionType.DESPESA,
                startDate,
                endDate
        );
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal getCashFlow(LocalDate startDate, LocalDate endDate) {
        return getTotalRevenue(startDate, endDate).subtract(getTotalExpenses(startDate, endDate));
    }

    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getActiveAccounts() {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return List.of();
        }
        return chartOfAccountsRepository.findAllActiveByTenant(tenantId);
    }
}
