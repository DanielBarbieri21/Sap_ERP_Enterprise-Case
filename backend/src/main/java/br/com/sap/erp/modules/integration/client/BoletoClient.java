package br.com.sap.erp.modules.integration.client;

import java.util.Map;

public interface BoletoClient {
    Map<String, Object> createBoleto(String amount, String dueDate, String description);
    Map<String, Object> getBoleto(String boletoId);
}
