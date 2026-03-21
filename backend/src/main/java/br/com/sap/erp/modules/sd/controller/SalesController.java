package br.com.sap.erp.modules.sd.controller;

import br.com.sap.erp.modules.sd.dto.InvoiceRequest;
import br.com.sap.erp.modules.sd.dto.InvoiceResponse;
import br.com.sap.erp.modules.sd.dto.SalesOrderRequest;
import br.com.sap.erp.modules.sd.dto.SalesOrderResponse;
import br.com.sap.erp.modules.sd.mapper.SalesMapper;
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
    private final SalesMapper salesMapper;

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('SD_CREATE')")
    public ResponseEntity<SalesOrderResponse> createOrder(@Valid @RequestBody SalesOrderRequest request) {
        return ResponseEntity.ok(
                salesMapper.toResponse(
                        salesService.createOrder(salesMapper.toEntity(request))
                )
        );
    }

    @PostMapping("/invoices")
    @PreAuthorize("hasAuthority('SD_INVOICE')")
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(
                salesMapper.toResponse(
                        salesService.createInvoice(salesMapper.toEntity(request))
                )
        );
    }

    @GetMapping("/orders")
    public ResponseEntity<List<SalesOrderResponse>> getOrders(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(
                    salesService.getOrdersByDateRange(startDate, endDate).stream()
                            .map(salesMapper::toResponse)
                            .toList()
            );
        }
        return ResponseEntity.ok(List.of());
    }
}
