package br.com.sap.erp.modules.mm.repository;

import br.com.sap.erp.modules.mm.domain.entity.PurchaseRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseRequisitionRepository extends JpaRepository<PurchaseRequisition, UUID> {
    Optional<PurchaseRequisition> findByRequisitionNumber(String requisitionNumber);
}
