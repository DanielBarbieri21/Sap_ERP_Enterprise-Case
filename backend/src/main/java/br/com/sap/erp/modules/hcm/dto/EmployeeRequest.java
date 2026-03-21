package br.com.sap.erp.modules.hcm.dto;

import br.com.sap.erp.modules.hcm.domain.entity.Employee.EmployeeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequest(
        String employeeCode,
        String name,
        String cpf,
        String rg,
        LocalDate birthDate,
        LocalDate hireDate,
        LocalDate terminationDate,
        String position,
        String department,
        BigDecimal salary,
        EmployeeStatus status,
        String email,
        String phone,
        String address
) {
}
