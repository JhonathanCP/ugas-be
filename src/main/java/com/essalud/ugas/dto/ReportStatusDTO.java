package com.essalud.ugas.dto;

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
public class ReportStatusDTO {

    @EqualsAndHashCode.Include
    private Integer idReportStatus;

    @NotNull
    @Size(max = 70)
    private String name;

    @NotNull
    @Size(max = 200)
    private String description;

}