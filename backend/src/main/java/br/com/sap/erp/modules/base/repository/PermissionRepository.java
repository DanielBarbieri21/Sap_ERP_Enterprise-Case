package br.com.sap.erp.modules.base.repository;

import br.com.sap.erp.modules.base.domain.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    
    Optional<Permission> findByCode(String code);
    
    java.util.List<Permission> findByModule(String module);
}
