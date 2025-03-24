package com.essalud.ugas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFileType;

    @Column(length = 70, nullable = false)
    private String name;
    
    @Column(length = 200, nullable = false)
    private String description;

    @Column
    private byte[] image;

}