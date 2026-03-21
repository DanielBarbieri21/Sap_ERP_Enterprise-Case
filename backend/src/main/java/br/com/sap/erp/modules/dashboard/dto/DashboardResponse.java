package br.com.sap.erp.modules.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DashboardResponse(
        BigDecimal monthlyRevenue,
        BigDecimal monthlyExpenses,
        BigDecimal monthlyProfit,
        long salesOrdersCount,
        long purchaseOrdersCount,
        long lowStockProducts,
        Period period
) {
    public record Period(LocalDate start, LocalDate end) {
    }
}
