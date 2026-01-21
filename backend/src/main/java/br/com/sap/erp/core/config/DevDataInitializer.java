package br.com.sap.erp.core.config;

import br.com.sap.erp.core.domain.TenantContext;
import br.com.sap.erp.modules.base.domain.entity.Company;
import br.com.sap.erp.modules.base.domain.entity.User;
import br.com.sap.erp.modules.base.repository.CompanyRepository;
import br.com.sap.erp.modules.base.repository.UserRepository;
import br.com.sap.erp.modules.base.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.util.UUID;

@Component
public class DevDataInitializer {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public DevDataInitializer(
            CompanyRepository companyRepository,
            UserRepository userRepository,
            UserService userService) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seedDemoData() {
        // CNPJ demo
        String demoCnpj = "12345678000190";

        Company company = companyRepository.findByCnpj(demoCnpj).orElseGet(() -> {
            Company c = Company.builder()
                    .name("Empresa Demo")
                    .tradeName("Demo Ltda")
                    .cnpj(demoCnpj)
                    .email("contato@demo.com")
                    .build();
            return companyRepository.save(c);
        });

        // Usuário admin demo
        String adminEmail = "admin@demo.com";
        if (!userRepository.existsByEmail(adminEmail)) {
            UUID companyId = company.getId();

            // Define tenantId para criação correta
            TenantContext.setCurrentTenantId(companyId);

            User admin = User.builder()
                    .name("Administrador")
                    .email(adminEmail)
                    .password("admin123")
                    .company(company)
                    .ice.create(admin, companyId);
}
