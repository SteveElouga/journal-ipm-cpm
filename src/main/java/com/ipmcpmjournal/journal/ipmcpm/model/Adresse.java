package com.ipmcpmjournal.journal.ipmcpm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Adresse implements Serializable {

    @Column(name="adresse1")
    private String adresse1;

    @Column(name="adresse2")
    private String adresse2;

    @Column(name="codepostal")
    private String codePostal;

    @Column(name="ville")
    private String ville;

    @Column(name="pays")
    private String pays;
}
