package br.com.sap.erp.modules.hcm.repository;

import br.com.sap.erp.modules.hcm.domain.entity.TimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, UUID> {
    
    @Query("SELECT t FROM TimeRecord t WHERE t.employee.id = :employeeId AND t.recordDate BETWEEN :startDate AND :endDate")
    List<TimeRecord> findByEmployeeAndDateRange(
        @Param("employeeId") UUID employeeId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
