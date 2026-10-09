package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
/**
 * Represente la moyenne d'une matiere pour un etudiant dans un bulletin
 */
@Entity
@Table(name = "moyenne_matieres")
public class MoyenneMatieres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(precision = 4,scale = 2)
    private BigDecimal moyenne;
    @Column(columnDefinition = "TEXT")
    private String appreciation;
    @ManyToOne
    @JoinColumn(name = "inscription_id", nullable = false)
    private Inscription inscription;
    @ManyToOne
    @JoinColumn(name = "bulletin_id", nullable = false)
    private Bulletin bulletin;
}
