package br.com.sap.erp.modules.co.service;

import br.com.sap.erp.core.domain.TenantContext;
import br.com.sap.erp.modules.co.domain.entity.AccountingDocument;
import br.com.sap.erp.modules.co.domain.entity.AccountingEntry;
import br.com.sap.erp.modules.co.repository.AccountingDocumentRepository;
import br.com.sap.erp.modules.co.repository.AccountingEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountingService {

    private final AccountingDocumentRepository documentRepository;
    private final AccountingEntryRepository entryRepository;

    @Transactional
    public AccountingDocument createDocument(AccountingDocument document) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId != null) {
            document.setTenantId(tenantId);
        }
        
        if (document.getDocumentDate() == null) {
            document.setDocumentDate(LocalDate.now());
        }
        
        document.recalculateTotals();
        
        if (!document.isBalanced()) {
            throw new RuntimeException("Documento não está balanceado. Débito: " + 
                document.getTotalDebit() + ", Crédito: " + document.getTotalCredit());
        }
        
        return documentRepository.save(document);
    }

    @Transactional
    public AccountingDocument postDocument(UUID documentId, UUID userId) {
        AccountingDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));
        
        if (document.getStatus() != AccountingDocument.DocumentStatus.DRAFT) {
            throw new RuntimeException("Documento já foi lançado ou cancelado");
        }
        
        if (!document.isBalanced()) {
            throw new RuntimeException("Documento não está balanceado");
        }
        
        document.setStatus(AccountingDocument.DocumentStatus.POSTED);
        document.setPostedAt(java.time.LocalDateTime.now());
        document.setPostedBy(userId);
        
        return documentRepository.save(document);
    }

    @Transactional(readOnly = true)
    public List<AccountingDocument> getDocumentsByDateRange(LocalDate startDate, LocalDate endDate) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return List.of();
        }
        return documentRepository.findByDateRange(tenantId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getTrialBalance(LocalDate startDate, LocalDate endDate) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return Map.of();
        }
        
        List<AccountingEntry> entries = entryRepository.findByDateRange(tenantId, startDate, endDate);
        
        Map<String, BigDecimal> balances = new java.util.HashMap<>();
        
        for (AccountingEntry entry : entries) {
            String accountCode = entry.getAccount().getCode();
            BigDecimal currentDebit = balances.getOrDefault(accountCode + "_DEBIT", BigDecimal.ZERO);
            BigDecimal currentCredit = balances.getOrDefault(accountCode + "_CREDIT", BigDecimal.ZERO);
            
            if (entry.getEntryType() == AccountingEntry.EntryType.DEBIT) {
                balances.put(accountCode + "_DEBIT", currentDebit.add(entry.getAmount()));
            } else {
                balances.put(accountCode + "_CREDIT", currentCredit.add(entry.getAmount()));
            }
        }
        
        return balances;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getBalanceSheet(LocalDate date) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return Map.of();
        }
        
        // Implementação simplificada - em produção, usar consultas SQL otimizadas
        List<AccountingEntry> entries = entryRepository.findByDateRange(
            tenantId, 
            LocalDate.of(date.getYear(), 1, 1), 
            date
        );
        
        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal totalLiabilities = BigDecimal.ZERO;
        BigDecimal totalEquity = BigDecimal.ZERO;
        
        for (AccountingEntry entry : entries) {
            BigDecimal amount = entry.getAmount();
            String accountType = entry.getAccount().getType().name();
            
            if (accountType.equals("ATIVO")) {
                if (entry.getEntryType() == AccountingEntry.EntryType.DEBIT) {
                    totalAssets = totalAssets.add(amount);
                } else {
                    totalAssets = totalAssets.subtract(amount);
                }
            } else if (accountType.equals("PASSIVO")) {
                if (entry.getEntryType() == AccountingEntry.EntryType.CREDIT) {
                    totalLiabilities = totalLiabilities.add(amount);
                } else {
                    totalLiabilities = totalLiabilities.subtract(amount);
                }
            } else if (accountType.equals("PATRIMONIO")) {
                if (entry.getEntryType() == AccountingEntry.EntryType.CREDIT) {
                    totalEquity = totalEquity.add(amount);
                } else {
                    totalEquity = totalEquity.subtract(amount);
                }
            }
        }
        
        return Map.of(
            "assets", totalAssets,
            "liabilities", totalLiabilities,
            "equity", totalEquity,
            "totalLiabilitiesAndEquity", totalLiabilities.add(totalEquity)
        );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getIncomeStatement(LocalDate startDate, LocalDate endDate) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return Map.of();
        }
        
        List<AccountingEntry> entries = entryRepository.findByDateRange(tenantId, startDate, endDate);
        
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;
        
        for (AccountingEntry entry : entries) {
            String accountType = entry.getAccount().getType().name();
            BigDecimal amount = entry.getAmount();
            
            if (accountType.equals("RECEITA")) {
                if (entry.getEntryType() == AccountingEntry.EntryType.CREDIT) {
                    totalRevenue = totalRevenue.add(amount);
                } else {
                    totalRevenue = totalRevenue.subtract(amount);
                }
            } else if (accountType.equals("DESPESA")) {
                if (entry.getEntryType() == AccountingEntry.EntryType.DEBIT) {
                    totalExpenses = totalExpenses.add(amount);
                } else {
                    totalExpenses = totalExpenses.subtract(amount);
                }
            }
        }
        
        BigDecimal netIncome = totalRevenue.subtract(totalExpenses);
        
        return Map.of(
            "revenue", totalRevenue,
            "expenses", totalExpenses,
            "netIncome", netIncome
        );
    }
}
