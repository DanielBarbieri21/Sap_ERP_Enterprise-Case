package br.com.sap.erp.core.domain;

import java.util.UUID;

public class TenantContext {

    private static final ThreadLocal<UUID> TENANT_ID = new ThreadLocal<>();

    public static void setCurrentTenantId(UUID tenantId) {
        TENANT_ID.set(tenantId);
    }

    public static UUID getCurrentTenantId() {
        return TENANT_ID.get();
    }

    public static void clear() {
        TENANT_ID.remove();
    }
}
