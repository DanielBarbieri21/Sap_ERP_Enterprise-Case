package br.com.sap.erp.modules.mm.repository;

import br.com.sap.erp.modules.mm.domain.entity.GoodsReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, UUID> {
    Optional<GoodsReceipt> findByReceiptNumber(String receiptNumber);
}
