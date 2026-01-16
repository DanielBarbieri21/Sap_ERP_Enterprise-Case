package br.com.sap.erp.modules.hcm.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payrolls", indexes = {
    @Index(name = "idx_payroll_ref", columnList = "reference_month, reference_year"),
    @Index(name = "idx_payroll_tenant", columnList = "tenant_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll extends TenantEntity {

    @Column(name = "reference_month", nullable = false)
    private Integer referenceMonth;

    @Column(name = "reference_year", nullable = false)
    private Integer referenceYear;

    @Column(name = "payroll_date", nullable = false)
    private LocalDate payrollDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private PayrollStatus status = PayrollStatus.DRAFT;

    @OneToMany(mappedBy = "payroll", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PayrollItem> items = new ArrayList<>();

    @Column(name = "total_gross", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal totalGross = BigDecimal.ZERO;

    @Column(name = "total_deductions", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal totalDeductions = BigDecimal.ZERO;

    @Column(name = "total_net", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal totalNet = BigDecimal.ZERO;

    public enum PayrollStatus {
        DRAFT, CALCULATED, APPROVED, PAID, CANCELLED
    }

    public void addItem(PayrollItem item) {
        items.add(item);
        item.setPayroll(this);
        recalculateTotals();
    }

    public void recalculateTotals() {
        totalGross = items.stream()
                .filter(i -> i.getType() == PayrollItem.ItemType.EARNING)
                .map(PayrollItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        totalDeductions = items.stream()
                .filter(i -> i.getType() == PayrollItem.ItemType.DEDUCTION)
                .map(PayrollItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        totalNet = totalGross.subtract(totalDeductions);
    }
}
