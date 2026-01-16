package br.com.sap.erp.modules.hcm.domain.entity;

import br.com.sap.erp.core.domain.TenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "time_records", indexes = {
    @Index(name = "idx_time_employee", columnList = "employee_id"),
    @Index(name = "idx_time_date", columnList = "record_date"),
    @Index(name = "idx_time_tenant", columnList = "tenant_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeRecord extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "entry_time")
    private LocalTime entryTime;

    @Column(name = "exit_time")
    private LocalTime exitTime;

    @Column(name = "break_duration_minutes")
    private Integer breakDurationMinutes;

    @Column(name = "total_hours")
    private java.math.BigDecimal totalHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private RecordType type; // NORMAL, OVERTIME, ABSENCE

    @Column(name = "notes", length = 500)
    private String notes;
    
    public enum RecordType {
        NORMAL, OVERTIME, ABSENCE, VACATION, SICK_LEAVE
    }
}
