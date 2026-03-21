package br.com.sap.erp.modules.hcm.dto;

import br.com.sap.erp.modules.hcm.domain.entity.TimeRecord.RecordType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record TimeRecordRequest(
        UUID employeeId,
        LocalDate recordDate,
        LocalTime entryTime,
        LocalTime exitTime,
        Integer breakDurationMinutes,
        java.math.BigDecimal totalHours,
        RecordType type,
        String notes
) {
}
