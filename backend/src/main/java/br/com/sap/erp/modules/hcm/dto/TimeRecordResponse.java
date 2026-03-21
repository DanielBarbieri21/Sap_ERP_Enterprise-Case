package br.com.sap.erp.modules.hcm.dto;

import br.com.sap.erp.modules.hcm.domain.entity.TimeRecord.RecordType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record TimeRecordResponse(
        UUID id,
        UUID employeeId,
        String employeeCode,
        String employeeName,
        LocalDate recordDate,
        LocalTime entryTime,
        LocalTime exitTime,
        Integer breakDurationMinutes,
        BigDecimal totalHours,
        RecordType type,
        String notes
) {
}
