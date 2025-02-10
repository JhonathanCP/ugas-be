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
public class AccessAudit {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAccessAudit;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false, foreignKey = @ForeignKey(name = "FK_ACCESS_AUDIT_USER"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_report", nullable = false, foreignKey = @ForeignKey(name = "FK_ACCESS_AUDIT_REPORT"))
    private Report report;

    @Column(nullable = false)
    private LocalDateTime accessDateTime;

    @PrePersist
    protected void onCreate() {
        this.accessDateTime = LocalDateTime.now();
    }

}