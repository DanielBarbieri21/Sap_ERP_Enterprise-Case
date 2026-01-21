package br.com.sap.erp.modules.integration.client.impl;

import br.com.sap.erp.modules.integration.client.NfeClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NfeClientStub implements NfeClient {
    @Override
    public Map<String, Object> issueNfe(Map<String, Object> invoiceData) {
        return Map.of("status", "pending", "provider", "nfe.stub", "invoice", invoiceData);
    }

    @Override
    public Map<String, Object> getStatus(String nfeId) {
        return Map.of("status", "pending", "provider", "nfe.stub", "id", nfeId);
    }
}
