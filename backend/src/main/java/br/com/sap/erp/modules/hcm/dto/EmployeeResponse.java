package br.com.sap.erp.modules.hcm.dto;

import br.com.sap.erp.modules.hcm.domain.entity.Employee.EmployeeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeResponse(
        UUID id,
        String employeeCode,
        String name,
        String cpf,
        LocalDate hireDate,
        String position,
        String department,
        BigDecimal salary,
        EmployeeStatus status,
        String email,
        String phone
) {
}
