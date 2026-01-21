package br.com.sap.erp.modules.fi.controller;

import br.com.sap.erp.modules.fi.domain.entity.ChartOfAccounts;
import br.com.sap.erp.modules.fi.service.FinancialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fi/accounts")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('FI_VIEW')")
public class AccountController {

    private final FinancialService financialService;

    @GetMapping
    public ResponseEntity<List<ChartOfAccounts>> listActive() {
        return ResponseEntity.ok(financialService.getActiveAccounts());
    }
}
