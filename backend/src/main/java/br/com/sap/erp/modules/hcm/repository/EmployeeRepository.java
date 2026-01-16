package br.com.sap.erp.modules.hcm.repository;

import br.com.sap.erp.modules.hcm.domain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmployeeCode(String employeeCode);
    Optional<Employee> findByCpf(String cpf);
}
