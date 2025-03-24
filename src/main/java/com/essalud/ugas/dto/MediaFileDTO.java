package com.essalud.ugas.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.essalud.ugas.model.FileType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true )
public class MediaFileDTO {

    @EqualsAndHashCode.Include
    private Integer idMediaFile;

    @NotNull
    @Size(max = 70)
    private String name;
    
    @NotNull
    @Size(max = 200)
    private String description;

    @NotNull
    private FileType fileType;

    @NotNull
    private Boolean active;

    private LocalDateTime createTime;

    @Column
    private byte[] file;

}