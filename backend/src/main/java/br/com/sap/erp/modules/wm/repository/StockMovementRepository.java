package br.com.sap.erp.modules.wm.repository;

import br.com.sap.erp.modules.wm.domain.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {
    
    @Query("SELECT m FROM StockMovement m WHERE m.tenantId = :tenantId AND m.movementDate BETWEEN :startDate AND :endDate")
    List<StockMovement> findByDateRange(
        @Param("tenantId") UUID tenantId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT m FROM StockMovement m WHERE m.product.id = :productId")
    List<StockMovement> findByProductId(@Param("productId") UUID productId);
}
