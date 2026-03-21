package br.com.sap.erp.modules.fi.dto;

import br.com.sap.erp.modules.fi.domain.entity.FinancialTransaction.TransactionStatus;
import br.com.sap.erp.modules.fi.domain.entity.FinancialTransaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record FinancialTransactionResponse(
        UUID id,
        String documentNumber,
        LocalDate transactionDate,
        LocalDate dueDate,
        LocalDate paymentDate,
        TransactionType type,
        TransactionStatus status,
        UUID accountId,
        String accountCode,
        String accountName,
        String description,
        BigDecimal amount,
        BigDecimal paidAmount,
        Integer installmentNumber,
        Integer totalInstallments,
        String paymentMethod,
        String notes
) {
}
