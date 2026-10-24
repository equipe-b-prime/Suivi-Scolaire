package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
/**
 * Represente un professeur de l'ecole
 */
@Entity
@Table(name = "professeur", indexes = {
    @Index(name = "idx_professeur_nom_prenom", columnList = "nom, prenom")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Professeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(length = 180)
    private String email;

    /**
     * Matiere principale du professeur (attribut simple).
     */
    @Column(name = "matiere_id")
    private Long matiereId;

    /**
     * Classe à laquelle le professeur est affecté pour enseigner sa matière.
     * Une matière ne peut avoir qu'un seul professeur par classe.
     */
    @Column(name = "classe_id")
    private Long classeId;
}
