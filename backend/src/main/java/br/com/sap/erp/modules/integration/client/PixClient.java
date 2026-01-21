package br.com.sap.erp.modules.integration.client;

import java.util.Map;

public interface PixClient {
    Map<String, Object> createCharge(String amount, String description);
    Map<String, Object> getCharge(String chargeId);
}
