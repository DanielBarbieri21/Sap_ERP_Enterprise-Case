package br.com.sap.erp.modules.integration.client.impl;

import br.com.sap.erp.modules.integration.client.PixClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PixClientStub implements PixClient {
    @Override
    public Map<String, Object> createCharge(String amount, String description) {
        return Map.of(
                "status", "pending",
                "provider", "pix.stub",
                "amount", amount,
                "description", description
        );
    }

    @Override
    public Map<String, Object> getCharge(String chargeId) {
        return Map.of("status", "pending", "provider", "pix.stub", "id", chargeId);
    }
}
