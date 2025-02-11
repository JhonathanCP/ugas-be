package com.essalud.ugas.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true )
public class AccessAuditDTO {

    @EqualsAndHashCode.Include
    private Integer idAccessAudit;

    @NotNull
    private UserDTO user;

    @NotNull
    private ReportDTO report;

    private LocalDateTime accessDateTime;

}