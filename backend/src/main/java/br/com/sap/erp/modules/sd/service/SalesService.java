package br.com.sap.erp.modules.sd.service;

import br.com.sap.erp.core.domain.TenantContext;
import br.com.sap.erp.modules.sd.domain.entity.Invoice;
import br.com.sap.erp.modules.sd.domain.entity.SalesOrder;
import br.com.sap.erp.modules.sd.repository.InvoiceRepository;
import br.com.sap.erp.modules.sd.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesOrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public SalesOrder createOrder(SalesOrder order) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId != null) {
            order.setTenantId(tenantId);
        }
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDate.now());
        }
        order.recalculateTotals();
        return orderRepository.save(order);
    }

    @Transactional
    public Invoice createInvoice(Invoice invoice) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId != null) {
            invoice.setTenantId(tenantId);
        }
        if (invoice.getInvoiceDate() == null) {
            invoice.setInvoiceDate(LocalDate.now());
        }
        invoice.recalculateTotals();
        return invoiceRepository.save(invoice);
    }

    @Transactional(readOnly = true)
    public List<SalesOrder> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return List.of();
        }
        return orderRepository.findByDateRange(tenantId, startDate, endDate);
    }
}
