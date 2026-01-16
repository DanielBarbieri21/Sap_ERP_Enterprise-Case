package br.com.sap.erp.modules.wm.repository;

import br.com.sap.erp.modules.wm.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByCode(String code);
    
    @Query("SELECT p FROM Product p WHERE p.tenantId = :tenantId AND p.currentStock < p.minStock")
    List<Product> findLowStock(@Param("tenantId") UUID tenantId);
}
