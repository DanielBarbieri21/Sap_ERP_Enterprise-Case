package br.com.sap.erp.modules.fi.repository;

import br.com.sap.erp.modules.fi.domain.entity.ChartOfAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChartOfAccountsRepository extends JpaRepository<ChartOfAccounts, UUID> {
    
    Optional<ChartOfAccounts> findByCode(String code);
    
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.tenantId = :tenantId AND c.active = true ORDER BY c.code")
    List<ChartOfAccounts> findAllActiveByTenant(@Param("tenantId") UUID tenantId);
    
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.parent.id = :parentId")
    List<ChartOfAccounts> findByParentId(@Param("parentId") UUID parentId);
}
