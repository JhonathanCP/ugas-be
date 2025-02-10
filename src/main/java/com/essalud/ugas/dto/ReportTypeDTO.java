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
public class ReportTypeDTO {

    @EqualsAndHashCode.Include
    private Integer idReportType;

    @NotNull
    @Size(max = 70)
    private String name;

    @NotNull
    @Size(max = 200)
    private String description;

}