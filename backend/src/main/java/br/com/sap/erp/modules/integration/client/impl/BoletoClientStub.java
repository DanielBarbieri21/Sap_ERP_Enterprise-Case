package br.com.sap.erp.modules.integration.client.impl;

import br.com.sap.erp.modules.integration.client.BoletoClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BoletoClientStub implements BoletoClient {
    @Override
    public Map<String, Object> createBoleto(String amount, String dueDate, String description) {
        return Map.of(
                "status", "pending",
                "provider", "boleto.stub",
                "amount", amount,
                "dueDate", dueDate,
                "description", description
        );
    }

    @Override
    public Map<String, Object> getBoleto(String boletoId) {
        return Map.of("status", "pending", "provider", "boleto.stub", "id", boletoId);
    }
}
