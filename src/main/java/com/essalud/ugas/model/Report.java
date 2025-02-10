package com.essalud.ugas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReport;

    @Column(length = 70, nullable = false)
    private String name;
    
    @Column(length = 200, nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_report_type", nullable = false, foreignKey = @ForeignKey(name = "FK_REPORT_REPORT_TYPE"))
    private ReportType reportType;

    @ManyToOne
    @JoinColumn(name = "id_report_status", nullable = false, foreignKey = @ForeignKey(name = "FK_REPORT_REPORT_STATUS"))
    private ReportStatus reportStatus;

    @ManyToOne
    @JoinColumn(name = "id_module", nullable = false, foreignKey = @ForeignKey(name = "FK_REPORT_MODULE"))
    private Module module;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }

}