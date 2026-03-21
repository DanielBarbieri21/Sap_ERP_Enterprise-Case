package br.com.sap.erp.modules.wm.controller;

import br.com.sap.erp.modules.wm.dto.ProductRequest;
import br.com.sap.erp.modules.wm.dto.ProductResponse;
import br.com.sap.erp.modules.wm.dto.StockMovementRequest;
import br.com.sap.erp.modules.wm.dto.StockMovementResponse;
import br.com.sap.erp.modules.wm.mapper.WarehouseMapper;
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
    private final WarehouseMapper warehouseMapper;

    @PostMapping("/products")
    @PreAuthorize("hasAuthority('WM_CREATE')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(
                warehouseMapper.toResponse(
                        warehouseService.createProduct(warehouseMapper.toEntity(request))
                )
        );
    }

    @PostMapping("/movements")
    @PreAuthorize("hasAuthority('WM_MOVE')")
    public ResponseEntity<StockMovementResponse> createMovement(@Valid @RequestBody StockMovementRequest request) {
        return ResponseEntity.ok(
                warehouseMapper.toResponse(
                        warehouseService.createMovement(warehouseMapper.toEntity(request))
                )
        );
    }

    @GetMapping("/products/low-stock")
    public ResponseEntity<List<ProductResponse>> getLowStockProducts() {
        return ResponseEntity.ok(
                warehouseService.getLowStockProducts().stream()
                        .map(warehouseMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> listProducts() {
        return ResponseEntity.ok(
                warehouseService.getAllActiveProducts().stream()
                        .map(warehouseMapper::toResponse)
                        .toList()
        );
    }
}
