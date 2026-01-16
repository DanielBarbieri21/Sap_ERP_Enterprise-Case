package br.com.sap.erp.modules.co.controller;

import br.com.sap.erp.modules.co.domain.entity.AccountingDocument;
import br.com.sap.erp.modules.co.service.AccountingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/co/documents")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('CO_VIEW')")
public class AccountingController {

    private final AccountingService accountingService;

    @PostMapping
    @PreAuthorize("hasAuthority('CO_CREATE')")
    public ResponseEntity<AccountingDocument> create(@Valid @RequestBody AccountingDocument document) {
        return ResponseEntity.ok(accountingService.createDocument(document));
    }

    @PostMapping("/{id}/post")
    @PreAuthorize("hasAuthority('CO_POST')")
    public ResponseEntity<AccountingDocument> post(
            @PathVariable UUID id,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(accountingService.postDocument(id, userId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<AccountingDocument>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(accountingService.getDocumentsByDateRange(startDate, endDate));
    }

    @GetMapping("/trial-balance")
    @PreAuthorize("hasAuthority('CO_VIEW')")
    public ResponseEntity<Map<String, java.math.BigDecimal>> getTrialBalance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(accountingService.getTrialBalance(startDate, endDate));
    }

    @GetMapping("/balance-sheet")
    @PreAuthorize("hasAuthority('CO_VIEW')")
    public ResponseEntity<Map<String, Object>> getBalanceSheet(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(accountingService.getBalanceSheet(date));
    }

    @GetMapping("/income-statement")
    @PreAuthorize("hasAuthority('CO_VIEW')")
    public ResponseEntity<Map<String, Object>> getIncomeStatement(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(accountingService.getIncomeStatement(startDate, endDate));
    }
}
