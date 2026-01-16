package br.com.sap.erp.modules.co.repository;

import br.com.sap.erp.modules.co.domain.entity.AccountingEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AccountingEntryRepository extends JpaRepository<AccountingEntry, UUID> {
    
    @Query("SELECT e FROM AccountingEntry e WHERE e.tenantId = :tenantId AND e.entryDate BETWEEN :startDate AND :endDate")
    List<AccountingEntry> findByDateRange(
        @Param("tenantId") UUID tenantId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT e FROM AccountingEntry e WHERE e.document.id = :documentId")
    List<AccountingEntry> findByDocumentId(@Param("documentId") UUID documentId);
}
