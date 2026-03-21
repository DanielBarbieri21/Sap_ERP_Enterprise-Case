package br.com.sap.erp.modules.fi.dto;

import br.com.sap.erp.modules.fi.domain.entity.ChartOfAccounts.AccountNature;
import br.com.sap.erp.modules.fi.domain.entity.ChartOfAccounts.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

public record ChartOfAccountsResponse(
        UUID id,
        String code,
        String name,
        String description,
        AccountType type,
        AccountNature nature,
        Integer level,
        BigDecimal balance,
        Boolean active
) {
}
