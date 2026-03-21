package br.com.sap.erp.modules.fi.dto;

import br.com.sap.erp.modules.fi.domain.entity.FinancialTransaction.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record FinancialTransactionRequest(
        String documentNumber,
        @NotNull LocalDate transactionDate,
        LocalDate dueDate,
        @NotNull TransactionType type,
        @NotNull UUID accountId,
        @NotBlank String description,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        Integer installmentNumber,
        Integer totalInstallments,
        String paymentMethod,
        String notes
) {
}
