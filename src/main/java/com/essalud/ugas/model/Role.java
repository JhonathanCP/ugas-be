package com.essalud.ugas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private Integer idRole;

    @Column(length = 50, nullable = false)
    private String name;
    
    @Column(length = 150, nullable = false)
    private String description;

}
