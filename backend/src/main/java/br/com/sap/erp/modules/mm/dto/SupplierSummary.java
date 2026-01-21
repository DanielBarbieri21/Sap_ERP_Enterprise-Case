package br.com.sap.erp.modules.mm.dto;

import java.util.UUID;

public class SupplierSummary {
    private UUID supplierId;
    private String supplierName;

    public SupplierSummary(UUID supplierId, String supplierName) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }
}