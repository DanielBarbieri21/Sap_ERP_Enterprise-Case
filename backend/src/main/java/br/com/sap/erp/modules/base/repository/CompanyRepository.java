package br.com.sap.erp.modules.base.repository;

import br.com.sap.erp.modules.base.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    
    Optional<Company> findByCnpj(String cnpj);
    
    @Query("SELECT c FROM Company c WHERE c.active = true")
    java.util.List<Company> findAllActive();
    
    boolean existsByCnpj(String cnpj);
}
