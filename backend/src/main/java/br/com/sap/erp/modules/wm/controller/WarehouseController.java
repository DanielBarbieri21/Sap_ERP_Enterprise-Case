package br.com.sap.erp.modules.wm.controller;

import br.com.sap.erp.modules.wm.domain.entity.Inventory;
import br.com.sap.erp.modules.wm.domain.entity.Product;
import br.com.sap.erp.modules.wm.domain.entity.StockMovement;
import br.com.sap.erp.modules.wm.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wm")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('WM_VIEW')")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping("/products")
    @PreAuthorize("hasAuthority('WM_CREATE')")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.ok(warehouseService.createProduct(product));
    }

    @PostMapping("/movements")
    @PreAuthorize("hasAuthority('WM_MOVE')")
    public ResponseEntity<StockMovement> createMovement(@Valid @RequestBody StockMovement movement) {
        return ResponseEntity.ok(warehouseService.createMovement(movement));
    }

    @PostMapping("/inventories")
    @PreAuthorize("hasAuthority('WM_INVENTORY')")
    public ResponseEntity<Inventory> createInventory(@Valid @RequestBody Inventory inventory) {
        return ResponseEntity.ok(warehouseService.createInventory(inventory));
    }

    @GetMapping("/products/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        return ResponseEntity.ok(warehouseService.getLowStockProducts());
    }
}
