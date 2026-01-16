package br.com.sap.erp.modules.co.repository;

import br.com.sap.erp.modules.co.domain.entity.AccountingDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountingDocumentRepository extends JpaRepository<AccountingDocument, UUID> {
    
    Optional<AccountingDocument> findByDocumentNumber(String documentNumber);
    
    @Query("SELECT d FROM AccountingDocument d WHERE d.tenantId = :tenantId AND d.documentDate BETWEEN :startDate AND :endDate")
    List<AccountingDocument> findByDateRange(
        @Param("tenantId") UUID tenantId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT d FROM AccountingDocument d WHERE d.tenantId = :tenantId AND d.status = :status")
    List<AccountingDocument> findByStatus(
        @Param("tenantId") UUID tenantId,
        @Param("status") AccountingDocument.DocumentStatus status
    );
}
