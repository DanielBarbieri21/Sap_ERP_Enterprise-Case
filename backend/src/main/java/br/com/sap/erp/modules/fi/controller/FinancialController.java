package br.com.sap.erp.modules.fi.controller;

import br.com.sap.erp.modules.fi.domain.entity.FinancialTransaction;
import br.com.sap.erp.modules.fi.service.FinancialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/fi/transactions")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('FI_VIEW')")
public class FinancialController {

    private final FinancialService financialService;

    @PostMapping
    @PreAuthorize("hasAuthority('FI_CREATE')")
    public ResponseEntity<FinancialTransaction> create(@Valid @RequestBody FinancialTransaction transaction) {
        return ResponseEntity.ok(financialService.createTransaction(transaction));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<FinancialTransaction>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(financialService.getTransactionsByDateRange(startDate, endDate));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FinancialTransaction>> getPending() {
        return ResponseEntity.ok(financialService.getPendingTransactions());
    }

    @PostMapping("/{id}/pay")
    @PreAuthorize("hasAuthority('FI_PAY')")
    public ResponseEntity<FinancialTransaction> pay(
            @PathVariable UUID id,
            @RequestBody Map<String, BigDecimal> request) {
        BigDecimal amount = request.get("amount");
        return ResponseEntity.ok(financialService.payTransaction(id, amount));
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, BigDecimal>> getSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        LocalDate start = startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        
        return ResponseEntity.ok(Map.of(
            "revenue", financialService.getTotalRevenue(start, end),
            "expenses", financialService.getTotalExpenses(start, end),
            "cashFlow", financialService.getCashFlow(start, end)
        ));
    }
}
