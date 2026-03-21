package br.com.sap.erp.modules.dashboard.service;

import br.com.sap.erp.core.domain.TenantContext;
import br.com.sap.erp.modules.dashboard.dto.DashboardResponse;
import br.com.sap.erp.modules.fi.domain.entity.FinancialTransaction;
import br.com.sap.erp.modules.fi.repository.FinancialTransactionRepository;
import br.com.sap.erp.modules.mm.repository.PurchaseOrderRepository;
import br.com.sap.erp.modules.sd.repository.SalesOrderRepository;
import br.com.sap.erp.modules.wm.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinancialTransactionRepository financialTransactionRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getDashboardData(LocalDate startDate, LocalDate endDate) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return new DashboardResponse(
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    0L,
                    0L,
                    0L,
                    new DashboardResponse.Period(LocalDate.now(), LocalDate.now())
            );
        }

        LocalDate start = startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        BigDecimal monthlyRevenue = financialTransactionRepository.getTotalByType(
                tenantId,
                FinancialTransaction.TransactionType.RECEITA,
                start,
                end
        );
        if (monthlyRevenue == null) {
            monthlyRevenue = BigDecimal.ZERO;
        }

        BigDecimal monthlyExpenses = financialTransactionRepository.getTotalByType(
                tenantId,
                FinancialTransaction.TransactionType.DESPESA,
                start,
                end
        );
        if (monthlyExpenses == null) {
            monthlyExpenses = BigDecimal.ZERO;
        }

        long salesOrdersCount = salesOrderRepository.findByDateRange(tenantId, start, end).size();
        long purchaseOrdersCount = purchaseOrderRepository.findByDateRange(tenantId, start, end).size();
        long lowStockProducts = productRepository.findLowStock(tenantId).size();

        return new DashboardResponse(
                monthlyRevenue,
                monthlyExpenses,
                monthlyRevenue.subtract(monthlyExpenses),
                salesOrdersCount,
                purchaseOrdersCount,
                lowStockProducts,
                new DashboardResponse.Period(start, end)
        );
    }
}
