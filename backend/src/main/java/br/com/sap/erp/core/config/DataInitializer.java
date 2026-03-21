package br.com.sap.erp.core.config;

import br.com.sap.erp.modules.base.domain.entity.Company;
import br.com.sap.erp.modules.base.domain.entity.Permission;
import br.com.sap.erp.modules.base.domain.entity.Role;
import br.com.sap.erp.modules.base.domain.entity.User;
import br.com.sap.erp.modules.base.repository.CompanyRepository;
import br.com.sap.erp.modules.base.repository.PermissionRepository;
import br.com.sap.erp.modules.base.repository.RoleRepository;
import br.com.sap.erp.modules.base.repository.UserRepository;
import br.com.sap.erp.modules.hcm.domain.entity.Employee;
import br.com.sap.erp.modules.hcm.repository.EmployeeRepository;
import br.com.sap.erp.modules.fi.domain.entity.ChartOfAccounts;
import br.com.sap.erp.modules.fi.domain.entity.FinancialTransaction;
import br.com.sap.erp.modules.fi.repository.ChartOfAccountsRepository;
import br.com.sap.erp.modules.fi.repository.FinancialTransactionRepository;
import br.com.sap.erp.modules.mm.domain.entity.PurchaseOrder;
import br.com.sap.erp.modules.mm.domain.entity.PurchaseOrderItem;
import br.com.sap.erp.modules.mm.repository.PurchaseOrderRepository;
import br.com.sap.erp.modules.sd.domain.entity.SalesOrder;
import br.com.sap.erp.modules.sd.domain.entity.SalesOrderItem;
import br.com.sap.erp.modules.sd.repository.SalesOrderRepository;
import br.com.sap.erp.modules.wm.domain.entity.Product;
import br.com.sap.erp.modules.wm.domain.entity.StockMovement;
import br.com.sap.erp.modules.wm.repository.ProductRepository;
import br.com.sap.erp.modules.wm.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ChartOfAccountsRepository chartOfAccountsRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Company company = ensureDemoCompany();
        List<Permission> permissions = ensurePermissions();
        Role adminRole = ensureAdminRole(permissions);
        ensureAdminUser(company, adminRole);
        ensureFinancialDemoData(company);
        ensureOperationalDemoData(company);

        System.out.println("==========================================");
        System.out.println("Ambiente demo inicializado com sucesso");
        System.out.println("Email: admin@demo.com");
        System.out.println("Senha: admin123");
        System.out.println("Tenant: " + company.getName());
        System.out.println("==========================================");
    }

    private Company ensureDemoCompany() {
        return companyRepository.findByCnpj("12345678000190")
                .orElseGet(() -> {
                    Company company = Company.builder()
                            .name("Empresa Demo")
                            .tradeName("Demo Auto Parts")
                            .cnpj("12345678000190")
                            .email("contato@demo.com")
                            .phone("+55 11 4002-8922")
                            .city("Sao Paulo")
                            .state("SP")
                            .country("Brasil")
                            .build();
                    company.setActive(true);
                    return companyRepository.save(company);
                });
    }

    private List<Permission> ensurePermissions() {
        List<Permission> basePermissions = Arrays.asList(
                createPermission("USER_CREATE", "Criar Usuario", "Permissao para criar usuarios", "BASE"),
                createPermission("USER_UPDATE", "Atualizar Usuario", "Permissao para atualizar usuarios", "BASE"),
                createPermission("USER_DELETE", "Deletar Usuario", "Permissao para deletar usuarios", "BASE"),
                createPermission("USER_VIEW", "Visualizar Usuario", "Permissao para visualizar usuarios", "BASE"),
                createPermission("FI_VIEW", "Visualizar Financeiro", "Permissao para visualizar modulo financeiro", "FI"),
                createPermission("FI_CREATE", "Criar Transacao", "Permissao para criar transacoes financeiras", "FI"),
                createPermission("FI_UPDATE", "Atualizar Transacao", "Permissao para atualizar transacoes", "FI"),
                createPermission("FI_PAY", "Pagar Transacao", "Permissao para pagar transacoes", "FI"),
                createPermission("FI_DELETE", "Deletar Transacao", "Permissao para deletar transacoes", "FI")
        );

        return basePermissions.stream()
                .map(permission -> permissionRepository.findByCode(permission.getCode()).orElseGet(() -> {
                    permission.setActive(true);
                    return permissionRepository.save(permission);
                }))
                .toList();
    }

    private Role ensureAdminRole(List<Permission> permissions) {
        return roleRepository.findAll().stream()
                .filter(role -> "ADMIN".equals(role.getName()))
                .findFirst()
                .map(role -> {
                    permissions.forEach(role::addPermission);
                    return roleRepository.save(role);
                })
                .orElseGet(() -> {
                    Role adminRole = Role.builder()
                            .name("ADMIN")
                            .description("Administrador do sistema")
                            .build();
                    adminRole.setActive(true);
                    permissions.forEach(adminRole::addPermission);
                    return roleRepository.save(adminRole);
                });
    }

    private void ensureAdminUser(Company company, Role adminRole) {
        if (userRepository.existsByEmail("admin@demo.com")) {
            return;
        }

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
    }

    private void ensureFinancialDemoData(Company company) {
        if (!chartOfAccountsRepository.findAllActiveByTenant(company.getId()).isEmpty()) {
            return;
        }

        ChartOfAccounts revenueAccount = createAccount(
                company,
                "3.1.01",
                "Receita de vendas",
                ChartOfAccounts.AccountType.RECEITA,
                ChartOfAccounts.AccountNature.CREDORA,
                3
        );

        ChartOfAccounts expenseAccount = createAccount(
                company,
                "4.1.01",
                "Despesas operacionais",
                ChartOfAccounts.AccountType.DESPESA,
                ChartOfAccounts.AccountNature.DEVEDORA,
                3
        );

        chartOfAccountsRepository.saveAll(List.of(revenueAccount, expenseAccount));

        FinancialTransaction revenue = FinancialTransaction.builder()
                .documentNumber("REC-2026-001")
                .transactionDate(LocalDate.now().minusDays(5))
                .dueDate(LocalDate.now().minusDays(1))
                .type(FinancialTransaction.TransactionType.RECEITA)
                .status(FinancialTransaction.TransactionStatus.PAID)
                .account(revenueAccount)
                .description("Receita demo de pedidos do mes")
                .amount(new BigDecimal("18500.00"))
                .paidAmount(new BigDecimal("18500.00"))
                .paymentDate(LocalDate.now().minusDays(1))
                .paymentMethod("PIX")
                .build();
        revenue.setTenantId(company.getId());
        revenue.setActive(true);

        FinancialTransaction expense = FinancialTransaction.builder()
                .documentNumber("DES-2026-001")
                .transactionDate(LocalDate.now().minusDays(3))
                .dueDate(LocalDate.now().plusDays(2))
                .type(FinancialTransaction.TransactionType.DESPESA)
                .status(FinancialTransaction.TransactionStatus.PENDING)
                .account(expenseAccount)
                .description("Despesa demo com compras e reposicao")
                .amount(new BigDecimal("7420.35"))
                .paidAmount(BigDecimal.ZERO)
                .paymentMethod("BOLETO")
                .build();
        expense.setTenantId(company.getId());
        expense.setActive(true);

        financialTransactionRepository.saveAll(List.of(revenue, expense));
    }

    private void ensureOperationalDemoData(Company company) {
        if (productRepository.findAllActiveByTenant(company.getId()).isEmpty()) {
            Product product = Product.builder()
                    .code("ROL-001")
                    .name("Rolamento dianteiro premium")
                    .description("Produto demo para portfolio com estoque e movimentacao.")
                    .unit("UN")
                    .type(Product.ProductType.MATERIAL)
                    .costPrice(new BigDecimal("120.00"))
                    .salePrice(new BigDecimal("199.90"))
                    .minStock(new BigDecimal("5"))
                    .maxStock(new BigDecimal("50"))
                    .currentStock(new BigDecimal("18"))
                    .build();
            product.setTenantId(company.getId());
            product.setActive(true);
            product = productRepository.save(product);

            StockMovement movement = StockMovement.builder()
                    .product(product)
                    .movementDate(LocalDate.now().minusDays(2))
                    .movementType(StockMovement.MovementType.ENTRADA)
                    .quantity(new BigDecimal("18"))
                    .unitCost(new BigDecimal("120.00"))
                    .documentType("ORDEM_COMPRA")
                    .documentNumber("PO-2026-001")
                    .notes("Carga inicial demo")
                    .build();
            movement.setTenantId(company.getId());
            movement.setActive(true);
            stockMovementRepository.save(movement);

            PurchaseOrderItem poItem = PurchaseOrderItem.builder()
                    .productCode(product.getCode())
                    .productDescription(product.getName())
                    .quantity(new BigDecimal("18"))
                    .unitPrice(new BigDecimal("120.00"))
                    .unit("UN")
                    .build();
            poItem.setTenantId(company.getId());
            poItem.setActive(true);

            PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                    .orderNumber("PO-2026-001")
                    .orderDate(LocalDate.now().minusDays(4))
                    .expectedDeliveryDate(LocalDate.now().plusDays(3))
                    .supplierName("Fornecedor Demo Auto Parts")
                    .status(PurchaseOrder.OrderStatus.SENT)
                    .build();
            purchaseOrder.setTenantId(company.getId());
            purchaseOrder.setActive(true);
            purchaseOrder.addItem(poItem);
            purchaseOrderRepository.save(purchaseOrder);

            SalesOrderItem soItem = SalesOrderItem.builder()
                    .productCode(product.getCode())
                    .productDescription(product.getName())
                    .quantity(new BigDecimal("3"))
                    .unitPrice(new BigDecimal("199.90"))
                    .unit("UN")
                    .build();
            soItem.setTenantId(company.getId());
            soItem.setActive(true);

            SalesOrder salesOrder = SalesOrder.builder()
                    .orderNumber("SO-2026-001")
                    .orderDate(LocalDate.now().minusDays(1))
                    .customerName("Cliente Demo Oficina")
                    .status(SalesOrder.OrderStatus.CONFIRMED)
                    .build();
            salesOrder.setTenantId(company.getId());
            salesOrder.setActive(true);
            salesOrder.addItem(soItem);
            salesOrderRepository.save(salesOrder);
        }

        if (employeeRepository.findAll().stream().noneMatch(employee -> company.getId().equals(employee.getTenantId()))) {
            Employee employee = Employee.builder()
                    .employeeCode("EMP-001")
                    .name("Analista de Compras Demo")
                    .cpf("12345678901")
                    .hireDate(LocalDate.now().minusMonths(8))
                    .position("Analista de Compras")
                    .department("Suprimentos")
                    .salary(new BigDecimal("4800.00"))
                    .status(Employee.EmployeeStatus.ACTIVE)
                    .email("compras@demo.com")
                    .phone("+55 11 99888-0000")
                    .build();
            employee.setTenantId(company.getId());
            employee.setActive(true);
            employeeRepository.save(employee);
        }
    }

    private ChartOfAccounts createAccount(
            Company company,
            String code,
            String name,
            ChartOfAccounts.AccountType type,
            ChartOfAccounts.AccountNature nature,
            int level
    ) {
        ChartOfAccounts account = ChartOfAccounts.builder()
                .code(code)
                .name(name)
                .description("Conta criada para ambiente demo de portfolio")
                .type(type)
                .nature(nature)
                .level(level)
                .balance(BigDecimal.ZERO)
                .active(true)
                .build();
        account.setTenantId(company.getId());
        account.setActive(true);
        return account;
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
