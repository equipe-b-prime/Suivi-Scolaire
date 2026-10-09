package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Represente un cours
 */
@Entity
@Table(name = "cours")
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dateCours;

    @ManyToOne
    @JoinColumn(name = "enseignement_id", nullable = false)
    private Enseignement enseignement;

    @ManyToOne
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    @ManyToOne
    @JoinColumn(name = "creneau_id", nullable = false)
    private Creneaux creneau;
}
