package br.com.sap.erp.modules.sd.controller;

import br.com.sap.erp.modules.sd.domain.entity.Invoice;
import br.com.sap.erp.modules.sd.domain.entity.SalesOrder;
import br.com.sap.erp.modules.sd.service.SalesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sd")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SD_VIEW')")
public class SalesController {

    private final SalesService salesService;

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('SD_CREATE')")
    public ResponseEntity<SalesOrder> createOrder(@Valid @RequestBody SalesOrder order) {
        return ResponseEntity.ok(salesService.createOrder(order));
    }

    @PostMapping("/invoices")
    @PreAuthorize("hasAuthority('SD_INVOICE')")
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice) {
        return ResponseEntity.ok(salesService.createInvoice(invoice));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<SalesOrder>> getOrders(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(salesService.getOrdersByDateRange(startDate, endDate));
        }
        return ResponseEntity.ok(List.of());
    }
}
