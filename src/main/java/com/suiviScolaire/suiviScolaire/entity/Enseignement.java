package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Represente un enseignement, qui relie un professeur à une matière
 */
@Entity
@Table(name = "enseignement", indexes = {
    @Index(name = "idx_enseignement_professeur", columnList = "professeur_id"),
    @Index(name = "idx_enseignement_matiere", columnList = "matiere_id")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Enseignement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;
    @ManyToOne
    @JoinColumn(name = "professeur_id", nullable = false)
    private Professeur professeur;
    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;
}
