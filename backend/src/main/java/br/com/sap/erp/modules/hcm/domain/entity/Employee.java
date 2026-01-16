package br.com.sap.erp.modules.hcm.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employees", indexes = {
    @Index(name = "idx_emp_code", columnList = "employee_code"),
    @Index(name = "idx_emp_tenant", columnList = "tenant_id"),
    @Index(name = "idx_emp_cpf", columnList = "cpf")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee extends TenantEntity {

    @Column(name = "employee_code", nullable = false, unique = true, length = 20)
    private String employeeCode;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "cpf", unique = true, length = 11)
    private String cpf;

    @Column(name = "rg", length = 20)
    private String rg;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "salary", precision = 19, scale = 2)
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 500)
    private String address;

    public enum EmployeeStatus {
        ACTIVE, INACTIVE, ON_LEAVE, TERMINATED
    }
}
