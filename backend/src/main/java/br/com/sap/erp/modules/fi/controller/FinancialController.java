package br.com.sap.erp.modules.fi.controller;

import br.com.sap.erp.modules.fi.dto.FinancialSummaryResponse;
import br.com.sap.erp.modules.fi.dto.FinancialTransactionRequest;
import br.com.sap.erp.modules.fi.dto.FinancialTransactionResponse;
import br.com.sap.erp.modules.fi.dto.PaymentRequest;
import br.com.sap.erp.modules.fi.mapper.FinancialTransactionMapper;
import br.com.sap.erp.modules.fi.service.FinancialService;
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
@RequestMapping("/fi/transactions")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('FI_VIEW')")
public class FinancialController {

    private final FinancialService financialService;
    private final FinancialTransactionMapper financialTransactionMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('FI_CREATE')")
    public ResponseEntity<FinancialTransactionResponse> create(@Valid @RequestBody FinancialTransactionRequest request) {
        return ResponseEntity.ok(
                financialTransactionMapper.toResponse(
                        financialService.createTransaction(financialTransactionMapper.toEntity(request))
                )
        );
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<FinancialTransactionResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(
                financialService.getTransactionsByDateRange(startDate, endDate).stream()
                        .map(financialTransactionMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FinancialTransactionResponse>> getPending() {
        return ResponseEntity.ok(
                financialService.getPendingTransactions().stream()
                        .map(financialTransactionMapper::toResponse)
                        .toList()
        );
    }

    @PostMapping("/{id}/pay")
    @PreAuthorize("hasAuthority('FI_PAY')")
    public ResponseEntity<FinancialTransactionResponse> pay(
            @PathVariable UUID id,
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(
                financialTransactionMapper.toResponse(financialService.payTransaction(id, request.amount()))
        );
    }

    @GetMapping("/summary")
    public ResponseEntity<FinancialSummaryResponse> getSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        LocalDate start = startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now();

        return ResponseEntity.ok(
                new FinancialSummaryResponse(
                        start,
                        end,
                        financialService.getTotalRevenue(start, end),
                        financialService.getTotalExpenses(start, end),
                        financialService.getCashFlow(start, end)
                )
        );
    }
}
