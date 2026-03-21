package br.com.sap.erp.modules.fi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialSummaryResponse(
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal revenue,
        BigDecimal expenses,
        BigDecimal cashFlow
) {
}
