package br.com.sap.erp.modules.fi.service;

import br.com.sap.erp.modules.fi.domain.entity.ChartOfAccounts;
import br.com.sap.erp.modules.fi.domain.entity.FinancialTransaction;
import br.com.sap.erp.modules.fi.repository.ChartOfAccountsRepository;
import br.com.sap.erp.modules.fi.repository.FinancialTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancialServiceTest {

    @Mock
    private FinancialTransactionRepository transactionRepository;

    @Mock
    private ChartOfAccountsRepository chartOfAccountsRepository;

    @InjectMocks
    private FinancialService financialService;

    private ChartOfAccounts account;
    private FinancialTransaction transaction;

    @BeforeEach
    void setUp() {
        account = ChartOfAccounts.builder()
                .code("1.1.01.001")
                .name("Caixa")
                .type(ChartOfAccounts.AccountType.ATIVO)
                .nature(ChartOfAccounts.AccountNature.DEVEDORA)
                .level(4)
                .build();
        account.setId(UUID.randomUUID());

        transaction = FinancialTransaction.builder()
                .documentNumber("DOC001")
                .transactionDate(LocalDate.now())
                .type(FinancialTransaction.TransactionType.RECEITA)
                .description("Test transaction")
                .amount(new BigDecimal("1000.00"))
                .build();
        transaction.setId(UUID.randomUUID());
    }

    @Test
    void testCreateTransaction() {
        when(chartOfAccountsRepository.findById(any(UUID.class))).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(FinancialTransaction.class))).thenReturn(transaction);

        FinancialTransaction created = financialService.createTransaction(transaction);

        assertNotNull(created);
        verify(transactionRepository, times(1)).save(any(FinancialTransaction.class));
    }
}
