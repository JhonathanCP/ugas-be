package com.essalud.ugas.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idModule")
public class ModuleDTO {

    @EqualsAndHashCode.Include
    private Integer idModule;

    @NotNull
    @Size(max = 70)
    private String name;

    @NotNull
    @Size(max = 200)
    private String description;

    @NotNull
    private Boolean active;

    @JsonManagedReference
    private List<ReportDTO> reports;

    private byte[] image;

}