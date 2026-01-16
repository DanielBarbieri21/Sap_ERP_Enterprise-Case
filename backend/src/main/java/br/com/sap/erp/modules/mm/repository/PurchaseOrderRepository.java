package br.com.sap.erp.modules.mm.repository;

import br.com.sap.erp.modules.mm.domain.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {
    
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);
    
    @Query("SELECT po FROM PurchaseOrder po WHERE po.tenantId = :tenantId AND po.orderDate BETWEEN :startDate AND :endDate")
    List<PurchaseOrder> findByDateRange(
        @Param("tenantId") UUID tenantId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT po FROM PurchaseOrder po WHERE po.tenantId = :tenantId AND po.status = :status")
    List<PurchaseOrder> findByStatus(
        @Param("tenantId") UUID tenantId,
        @Param("status") PurchaseOrder.OrderStatus status
    );
}
