package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;

/**
 * Représente les aménagements pour un élève
 */
@Entity
@Table(name = "amenagement")
public class Amenagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String libelle;

    @ManyToOne
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;

}
