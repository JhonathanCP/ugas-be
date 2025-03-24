package com.essalud.ugas.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserMediaFilePK implements Serializable{

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "id_media_file")
    private MediaFile mediaFile;

}