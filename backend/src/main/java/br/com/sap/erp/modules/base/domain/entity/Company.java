package br.com.sap.erp.modules.base.domain.entity;

import br.com.sap.erp.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "companies", indexes = {
    @Index(name = "idx_company_cnpj", columnList = "cnpj"),
    @Index(name = "idx_company_active", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "trade_name", length = 255)
    private String tradeName;

    @Column(name = "cnpj", unique = true, nullable = false, length = 14)
    private String cnpj;

    @Column(name = "ie", length = 20)
    private String ie; // Inscrição Estadual

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 2)
    private String state;

    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "settings", columnDefinition = "TEXT")
    private String settings; // JSON com configurações da empresa
}
