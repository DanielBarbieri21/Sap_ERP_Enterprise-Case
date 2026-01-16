package br.com.sap.erp.core.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    
    @Query("SELECT a FROM AuditLog a WHERE a.tenantId = :tenantId AND a.actionDate BETWEEN :startDate AND :endDate ORDER BY a.actionDate DESC")
    List<AuditLog> findByDateRange(
        @Param("tenantId") UUID tenantId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT a FROM AuditLog a WHERE a.userId = :userId ORDER BY a.actionDate DESC")
    List<AuditLog> findByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType AND a.entityId = :entityId ORDER BY a.actionDate DESC")
    List<AuditLog> findByEntity(
        @Param("entityType") String entityType,
        @Param("entityId") UUID entityId
    );
}
