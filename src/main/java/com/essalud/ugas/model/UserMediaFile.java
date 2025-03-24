package com.essalud.ugas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@IdClass(UserMediaFilePK.class)
public class UserMediaFile {


    @Id
    private User user;

    @Id
    private MediaFile mediaFile;

}