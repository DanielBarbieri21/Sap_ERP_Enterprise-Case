package br.com.sap.erp.modules.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    @Builder.Default
    private String type = "Bearer";
    private UUID userId;
    private String name;
    private String email;
    private UUID companyId;
    private String companyName;
    private UUID tenantId;
}
