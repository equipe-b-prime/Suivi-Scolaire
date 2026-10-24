package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Represente une inscription d'un élève à une classe pour une année scolaire donnée
 */
@Entity
@Table(name = "inscription", indexes = {
    @Index(name = "idx_inscription_eleve", columnList = "eleve_id"),
    @Index(name = "idx_inscription_classe", columnList = "classe_id"),
    @Index(name = "idx_inscription_annee", columnList = "annee_id")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "annee_id", nullable = false)
    private Annee annee;
    @ManyToOne
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;
    @ManyToOne
    @JoinColumn(name = "classe_id", nullable = false)
    private Classe classe;
    @Column(name = "date_debut")
    private LocalDate dateDebut;
    @Column(name = "date_fin")
    private LocalDate dateFin;
}