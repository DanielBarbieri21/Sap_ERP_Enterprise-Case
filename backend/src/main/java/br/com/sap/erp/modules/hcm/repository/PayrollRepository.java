package br.com.sap.erp.modules.hcm.repository;

import br.com.sap.erp.modules.hcm.domain.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, UUID> {
    
    @Query("SELECT p FROM Payroll p WHERE p.tenantId = :tenantId AND p.referenceMonth = :month AND p.referenceYear = :year")
    Optional<Payroll> findByReference(
        @Param("tenantId") UUID tenantId,
        @Param("month") Integer month,
        @Param("year") Integer year
    );
}
