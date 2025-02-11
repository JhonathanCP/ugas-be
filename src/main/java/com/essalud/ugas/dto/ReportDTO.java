package com.essalud.ugas.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true )
public class ReportDTO {

    @EqualsAndHashCode.Include
    private Integer idReport;

    @NotNull
    @Size(max = 70)
    private String name;

    @NotNull
    @Size(max = 200)
    private String description;

    @NotNull
    private ReportTypeDTO reportType;

    @NotNull
    private ReportStatusDTO reportStatus;

    @NotNull
    private ModuleDTO module;

    @NotNull
    private Boolean active;

    private LocalDateTime createTime;

}