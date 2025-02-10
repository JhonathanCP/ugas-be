package com.essalud.ugas.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class UserReportPK implements Serializable{

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "id_report")
    private Report report;

}