package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
/**
 * Represente un creneau horaire pour les cours
 */
@Entity
@Table(name = "creneaux")
public class Creneaux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String chreno;

    private LocalTime heureDebut;

    private LocalTime heureFin;
}

