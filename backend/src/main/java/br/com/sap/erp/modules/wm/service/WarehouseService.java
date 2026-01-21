package br.com.sap.erp.modules.wm.service;

import br.com.sap.erp.core.domain.TenantContext;
import br.com.sap.erp.modules.wm.domain.entity.Inventory;
import br.com.sap.erp.modules.wm.domain.entity.Product;
import br.com.sap.erp.modules.wm.domain.entity.StockMovement;
import br.com.sap.erp.modules.wm.repository.InventoryRepository;
import br.com.sap.erp.modules.wm.repository.ProductRepository;
import br.com.sap.erp.modules.wm.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final ProductRepository productRepository;
    private final StockMovementRepository movementRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public Product createProduct(Product product) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId != null) {
            product.setTenantId(tenantId);
        }
        return productRepository.save(product);
    }

    @Transactional
    public StockMovement createMovement(StockMovement movement) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId != null) {
            movement.setTenantId(tenantId);
        }
        if (movement.getMovementDate() == null) {
            movement.setMovementDate(LocalDate.now());
        }
        
        // Atualizar estoque do produto
        Product product = movement.getProduct();
        if (movement.getMovementType() == StockMovement.MovementType.ENTRADA) {
            product.setCurrentStock(product.getCurrentStock().add(movement.getQuantity()));
        } else if (movement.getMovementType() == StockMovement.MovementType.SAIDA) {
            product.setCurrentStock(product.getCurrentStock().subtract(movement.getQuantity()));
        }
        productRepository.save(product);
        
        return movementRepository.save(movement);
    }

    @Transactional
    public Inventory createInventory(Inventory inventory) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId != null) {
            inventory.setTenantId(tenantId);
        }
        if (inventory.getInventoryDate() == null) {
            inventory.setInventoryDate(LocalDate.now());
        }
        return inventoryRepository.save(inventory);
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts() {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return List.of();
        }
        return productRepository.findLowStock(tenantId);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllActiveProducts() {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return List.of();
        }
        return productRepository.findAllActiveByTenant(tenantId);
    }
}
