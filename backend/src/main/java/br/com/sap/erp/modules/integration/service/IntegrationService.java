package br.com.sap.erp.modules.integration.service;

import br.com.sap.erp.modules.integration.client.BoletoClient;
import br.com.sap.erp.modules.integration.client.NfeClient;
import br.com.sap.erp.modules.integration.client.PixClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class IntegrationService {

    private final PixClient pixClient;
    private final BoletoClient boletoClient;
    private final NfeClient nfeClient;
    
    public Map<String, Object> generatePixPayment(String amount, String description) {
        return pixClient.createCharge(amount, description);
    }

    public Map<String, Object> generateBoleto(String amount, String dueDate, String description) {
        return boletoClient.createBoleto(amount, dueDate, description);
    }

    public Map<String, Object> issueNFe(Map<String, Object> invoiceData) {
        return nfeClient.issueNfe(invoiceData);
    }
}
