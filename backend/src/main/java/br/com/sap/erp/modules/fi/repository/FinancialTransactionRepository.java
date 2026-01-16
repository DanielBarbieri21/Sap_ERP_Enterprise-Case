package br.com.sap.erp.modules.fi.repository;

import br.com.sap.erp.modules.fi.domain.entity.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, UUID> {
    
    @Query("SELECT t FROM FinancialTransaction t WHERE t.tenantId = :tenantId AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<FinancialTransaction> findByDateRange(
        @Param("tenantId") UUID tenantId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT t FROM FinancialTransaction t WHERE t.tenantId = :tenantId AND t.status = :status")
    List<FinancialTransaction> findByStatus(
        @Param("tenantId") UUID tenantId,
        @Param("status") FinancialTransaction.TransactionStatus status
    );
    
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM FinancialTransaction t WHERE t.tenantId = :tenantId AND t.type = :type AND t.transactionDate >= :startDate AND t.transactionDate <= :endDate")
    java.math.BigDecimal getTotalByType(
        @Param("tenantId") UUID tenantId,
        @Param("type") FinancialTransaction.TransactionType type,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
