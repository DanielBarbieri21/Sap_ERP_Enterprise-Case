package br.com.sap.erp.modules.dashboard.service;

import br.com.sap.erp.core.domain.TenantContext;
import br.com.sap.erp.modules.fi.repository.FinancialTransactionRepository;
import br.com.sap.erp.modules.mm.repository.PurchaseOrderRepository;
import br.com.sap.erp.modules.sd.repository.SalesOrderRepository;
import br.com.sap.erp.modules.wm.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinancialTransactionRepository financialTransactionRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardData() {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return Map.of();
        }

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        // Receitas do mês
        java.math.BigDecimal monthlyRevenue = financialTransactionRepository.getTotalByType(
            tenantId, 
            br.com.sap.erp.modules.fi.domain.entity.FinancialTransaction.TransactionType.RECEITA,
            startOfMonth,
            endOfMonth
        );
        if (monthlyRevenue == null) monthlyRevenue = java.math.BigDecimal.ZERO;

        // Despesas do mês
        java.math.BigDecimal monthlyExpenses = financialTransactionRepository.getTotalByType(
            tenantId,
            br.com.sap.erp.modules.fi.domain.entity.FinancialTransaction.TransactionType.DESPESA,
            startOfMonth,
            endOfMonth
        );
        if (monthlyExpenses == null) monthlyExpenses = java.math.BigDecimal.ZERO;

        // Vendas do mês
        long salesOrdersCount = salesOrderRepository.findByDateRange(tenantId, startOfMonth, endOfMonth).size();

        // Compras do mês
        long purchaseOrdersCount = purchaseOrderRepository.findByDateRange(tenantId, startOfMonth, endOfMonth).size();

        // Produtos com estoque baixo
        long lowStockProducts = productRepository.findLowStock(tenantId).size();

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("monthlyRevenue", monthlyRevenue);
        dashboard.put("monthlyExpenses", monthlyExpenses);
        dashboard.put("monthlyProfit", monthlyRevenue.subtract(monthlyExpenses));
        dashboard.put("salesOrdersCount", salesOrdersCount);
        dashboard.put("purchaseOrdersCount", purchaseOrdersCount);
        dashboard.put("lowStockProducts", lowStockProducts);
        dashboard.put("period", Map.of("start", startOfMonth, "end", endOfMonth));

        return dashboard;
    }
}
