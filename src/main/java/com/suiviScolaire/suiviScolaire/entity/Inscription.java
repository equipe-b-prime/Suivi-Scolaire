package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Represente une inscription d'un élève à une classe pour une année scolaire donnée
 */
@Entity
@Table(name = "inscription")
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
}
