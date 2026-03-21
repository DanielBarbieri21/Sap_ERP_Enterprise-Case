package br.com.sap.erp.modules.fi.mapper;

import br.com.sap.erp.modules.fi.domain.entity.ChartOfAccounts;
import br.com.sap.erp.modules.fi.domain.entity.FinancialTransaction;
import br.com.sap.erp.modules.fi.dto.ChartOfAccountsResponse;
import br.com.sap.erp.modules.fi.dto.FinancialTransactionRequest;
import br.com.sap.erp.modules.fi.dto.FinancialTransactionResponse;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface FinancialTransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "paidAmount", ignore = true)
    @Mapping(target = "account", expression = "java(toAccountReference(request.accountId()))")
    FinancialTransaction toEntity(FinancialTransactionRequest request);

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "accountCode", source = "account.code")
    @Mapping(target = "accountName", source = "account.name")
    FinancialTransactionResponse toResponse(FinancialTransaction entity);

    ChartOfAccountsResponse toResponse(ChartOfAccounts entity);

    default ChartOfAccounts toAccountReference(java.util.UUID accountId) {
        if (accountId == null) {
            return null;
        }
        ChartOfAccounts account = new ChartOfAccounts();
        account.setId(accountId);
        return account;
    }
}
