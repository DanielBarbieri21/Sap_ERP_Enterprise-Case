package br.com.sap.erp.core.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('AUDIT_VIEW')")
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/logs")
    public ResponseEntity<List<AuditLog>> getLogs(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(auditService.getLogsByDateRange(startDate, endDate));
    }
}
