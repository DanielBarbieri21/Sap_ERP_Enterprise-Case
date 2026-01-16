package br.com.sap.erp.modules.hcm.service;

import br.com.sap.erp.core.domain.TenantContext;
import br.com.sap.erp.modules.hcm.domain.entity.Employee;
import br.com.sap.erp.modules.hcm.domain.entity.Payroll;
import br.com.sap.erp.modules.hcm.domain.entity.TimeRecord;
import br.com.sap.erp.modules.hcm.repository.EmployeeRepository;
import br.com.sap.erp.modules.hcm.repository.PayrollRepository;
import br.com.sap.erp.modules.hcm.repository.TimeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HumanResourcesService {

    private final EmployeeRepository employeeRepository;
    private final PayrollRepository payrollRepository;
    private final TimeRecordRepository timeRecordRepository;

    @Transactional
    public Employee createEmployee(Employee employee) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId != null) {
            employee.setTenantId(tenantId);
        }
        return employeeRepository.save(employee);
    }

    @Transactional
    public Payroll createPayroll(Payroll payroll) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId != null) {
            payroll.setTenantId(tenantId);
        }
        payroll.recalculateTotals();
        return payrollRepository.save(payroll);
    }

    @Transactional
    public TimeRecord createTimeRecord(TimeRecord timeRecord) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId != null) {
            timeRecord.setTenantId(tenantId);
        }
        if (timeRecord.getRecordDate() == null) {
            timeRecord.setRecordDate(LocalDate.now());
        }
        return timeRecordRepository.save(timeRecord);
    }

    @Transactional(readOnly = true)
    public List<TimeRecord> getTimeRecordsByEmployee(UUID employeeId, LocalDate startDate, LocalDate endDate) {
        return timeRecordRepository.findByEmployeeAndDateRange(employeeId, startDate, endDate);
    }
}
