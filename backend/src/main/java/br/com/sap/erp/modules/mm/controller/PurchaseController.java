package br.com.sap.erp.modules.mm.controller;

import br.com.sap.erp.modules.mm.domain.entity.GoodsReceipt;
import br.com.sap.erp.modules.mm.domain.entity.PurchaseOrder;
import br.com.sap.erp.modules.mm.domain.entity.PurchaseRequisition;
import br.com.sap.erp.modules.mm.dto.SupplierSummary;
import br.com.sap.erp.modules.mm.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/mm")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('MM_VIEW')")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("/requisitions")
    @PreAuthorize("hasAuthority('MM_CREATE')")
    public ResponseEntity<PurchaseRequisition> createRequisition(@Valid @RequestBody PurchaseRequisition requisition) {
        return ResponseEntity.ok(purchaseService.createRequisition(requisition));
    }

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('MM_CREATE')")
    public ResponseEntity<PurchaseOrder> createOrder(@Valid @RequestBody PurchaseOrder order) {
        return ResponseEntity.ok(purchaseService.createOrder(order));
    }

    @PostMapping("/receipts")
    @PreAuthorize("hasAuthority('MM_RECEIVE')")
    public ResponseEntity<GoodsReceipt> createReceipt(@Valid @RequestBody GoodsReceipt receipt) {
        return ResponseEntity.ok(purchaseService.createReceipt(receipt));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<PurchaseOrder>> getOrders(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(purchaseService.getOrdersByDateRange(startDate, endDate));
        }
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/suppliers")
    public ResponseEntity<List<SupplierSummary>> listSuppliers() {
        return ResponseEntity.ok(purchaseService.getSuppliers());
    }
}
