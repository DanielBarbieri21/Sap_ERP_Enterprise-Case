package br.com.sap.erp.modules.sd.repository;

import br.com.sap.erp.modules.sd.domain.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, UUID> {
    Optional<SalesOrder> findByOrderNumber(String orderNumber);
    
    @Query("SELECT so FROM SalesOrder so WHERE so.tenantId = :tenantId AND so.orderDate BETWEEN :startDate AND :endDate")
    List<SalesOrder> findByDateRange(
        @Param("tenantId") UUID tenantId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
