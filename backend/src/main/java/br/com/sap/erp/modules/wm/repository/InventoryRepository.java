package br.com.sap.erp.modules.wm.repository;

import br.com.sap.erp.modules.wm.domain.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    Optional<Inventory> findByInventoryNumber(String inventoryNumber);
}
