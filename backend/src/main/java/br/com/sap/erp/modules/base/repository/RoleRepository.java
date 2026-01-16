package br.com.sap.erp.modules.base.repository;

import br.com.sap.erp.modules.base.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    
    Optional<Role> findByName(String name);
    
    boolean existsByName(String name);
}
