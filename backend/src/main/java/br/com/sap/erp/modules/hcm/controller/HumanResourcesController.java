package br.com.sap.erp.modules.hcm.controller;

import br.com.sap.erp.modules.hcm.domain.entity.Employee;
import br.com.sap.erp.modules.hcm.domain.entity.Payroll;
import br.com.sap.erp.modules.hcm.domain.entity.TimeRecord;
import br.com.sap.erp.modules.hcm.service.HumanResourcesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hcm")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('HCM_VIEW')")
public class HumanResourcesController {

    private final HumanResourcesService hrService;

    @PostMapping("/employees")
    @PreAuthorize("hasAuthority('HCM_CREATE')")
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        return ResponseEntity.ok(hrService.createEmployee(employee));
    }

    @PostMapping("/payrolls")
    @PreAuthorize("hasAuthority('HCM_PAYROLL')")
    public ResponseEntity<Payroll> createPayroll(@Valid @RequestBody Payroll payroll) {
        return ResponseEntity.ok(hrService.createPayroll(payroll));
    }

    @PostMapping("/time-records")
    @PreAuthorize("hasAuthority('HCM_TIME')")
    public ResponseEntity<TimeRecord> createTimeRecord(@Valid @RequestBody TimeRecord timeRecord) {
        return ResponseEntity.ok(hrService.createTimeRecord(timeRecord));
    }

    @GetMapping("/employees/{id}/time-records")
    public ResponseEntity<List<TimeRecord>> getTimeRecords(
            @PathVariable UUID id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(hrService.getTimeRecordsByEmployee(id, startDate, endDate));
    }
}
