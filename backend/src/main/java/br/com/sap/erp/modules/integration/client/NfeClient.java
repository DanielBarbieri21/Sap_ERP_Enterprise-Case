package br.com.sap.erp.modules.integration.client;

import java.util.Map;

public interface NfeClient {
    Map<String, Object> issueNfe(Map<String, Object> invoiceData);
    Map<String, Object> getStatus(String nfeId);
}
