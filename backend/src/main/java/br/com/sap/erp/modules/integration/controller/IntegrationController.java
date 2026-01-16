package br.com.sap.erp.modules.integration.controller;

import br.com.sap.erp.modules.integration.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/integration")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('INTEGRATION_USE')")
public class IntegrationController {

    private final IntegrationService integrationService;

    @PostMapping("/pix")
    public ResponseEntity<Map<String, Object>> generatePix(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(integrationService.generatePixPayment(
            request.get("amount"),
            request.get("description")
        ));
    }

    @PostMapping("/boleto")
    public ResponseEntity<Map<String, Object>> generateBoleto(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(integrationService.generateBoleto(
            request.get("amount"),
            request.get("dueDate"),
            request.get("description")
        ));
    }

    @PostMapping("/nfe")
    public ResponseEntity<Map<String, Object>> issueNFe(@RequestBody Map<String, Object> invoiceData) {
        return ResponseEntity.ok(integrationService.issueNFe(invoiceData));
    }
}
