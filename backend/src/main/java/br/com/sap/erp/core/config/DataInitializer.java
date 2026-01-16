package br.com.sap.erp.core.config;

import br.com.sap.erp.modules.base.domain.entity.Company;
import br.com.sap.erp.modules.base.domain.entity.Permission;
import br.com.sap.erp.modules.base.domain.entity.Role;
import br.com.sap.erp.modules.base.domain.entity.User;
import br.com.sap.erp.modules.base.repository.CompanyRepository;
import br.com.sap.erp.modules.base.repository.PermissionRepository;
import br.com.sap.erp.modules.base.repository.RoleRepository;
import br.com.sap.erp.modules.base.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Inicializador de dados para desenvolvimento
 * Cria dados iniciais apenas se o banco estiver vazio
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (companyRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        // Criar empresa demo
        Company company = Company.builder()
                .name("Empresa Demo")
                .tradeName("Demo Ltda")
                .cnpj("12345678000190")
                .email("contato@demo.com")
                .build();
        company.setActive(true);
        company = companyRepository.save(company);

        // Criar permissões
        List<Permission> permissions = Arrays.asList(
                createPermission("USER_CREATE", "Criar Usuário", "Permissão para criar usuários", "BASE"),
                createPermission("USER_UPDATE", "Atualizar Usuário", "Permissão para atualizar usuários", "BASE"),
                createPermission("USER_DELETE", "Deletar Usuário", "Permissão para deletar usuários", "BASE"),
                createPermission("USER_VIEW", "Visualizar Usuário", "Permissão para visualizar usuários", "BASE"),
                createPermission("FI_VIEW", "Visualizar Financeiro", "Permissão para visualizar módulo financeiro", "FI"),
                createPermission("FI_CREATE", "Criar Transação", "Permissão para criar transações financeiras", "FI"),
                createPermission("FI_UPDATE", "Atualizar Transação", "Permissão para atualizar transações", "FI"),
                createPermission("FI_PAY", "Pagar Transação", "Permissão para pagar transações", "FI"),
                createPermission("FI_DELETE", "Deletar Transação", "Permissão para deletar transações", "FI")
        );
        permissionRepository.saveAll(permissions);

        // Criar role Admin
        Role adminRole = Role.builder()
                .name("ADMIN")
                .description("Administrador do sistema")
                .build();
        permissions.forEach(adminRole::addPermission);
        adminRole = roleRepository.save(adminRole);

        // Criar usuário admin
        User admin = User.builder()
                .name("Administrador")
                .email("admin@demo.com")
                .password(passwordEncoder.encode("admin123"))
                .company(company)
                .mustChangePassword(false)
                .build();
        admin.setTenantId(company.getId());
        admin.setActive(true);
        admin.addRole(adminRole);
        userRepository.save(admin);

        System.out.println("==========================================");
        System.out.println("Dados iniciais criados com sucesso!");
        System.out.println("Email: admin@demo.com");
        System.out.println("Senha: admin123");
        System.out.println("==========================================");
    }

    private Permission createPermission(String code, String name, String description, String module) {
        Permission permission = Permission.builder()
                .code(code)
                .name(name)
                .description(description)
                .module(module)
                .build();
        permission.setActive(true);
        return permission;
    }
}
