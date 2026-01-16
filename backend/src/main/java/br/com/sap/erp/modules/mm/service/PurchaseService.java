package br.com.sap.erp.modules.mm.service;

import br.com.sap.erp.core.domain.TenantContext;
import br.com.sap.erp.modules.mm.domain.entity.GoodsReceipt;
import br.com.sap.erp.modules.mm.domain.entity.PurchaseOrder;
import br.com.sap.erp.modules.mm.domain.entity.PurchaseRequisition;
import br.com.sap.erp.modules.mm.repository.GoodsReceiptRepository;
import br.com.sap.erp.modules.mm.repository.PurchaseOrderRepository;
import br.com.sap.erp.modules.mm.repository.PurchaseRequisitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRequisitionRepository requisitionRepository;
    private final PurchaseOrderRepository orderRepository;
    private final GoodsReceiptRepository receiptRepository;

    @Transactional
    public PurchaseRequisition createRequisition(PurchaseRequisition requisition) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId != null) {
            requisition.setTenantId(tenantId);
        }
        if (requisition.getRequisitionDate() == null) {
            requisition.setRequisitionDate(LocalDate.now());
        }
        requisition.recalculateTotal();
        return requisitionRepository.save(requisition);
    }

    @Transactional
    public PurchaseOrder createOrder(PurchaseOrder order) {
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
    public GoodsReceipt createReceipt(GoodsReceipt receipt) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId != null) {
            receipt.setTenantId(tenantId);
        }
        if (receipt.getReceiptDate() == null) {
            receipt.setReceiptDate(LocalDate.now());
        }
        receipt.recalculateTotal();
        return receiptRepository.save(receipt);
    }

    @Transactional(readOnly = true)
    public List<PurchaseOrder> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return List.of();
        }
        return orderRepository.findByDateRange(tenantId, startDate, endDate);
    }
}
