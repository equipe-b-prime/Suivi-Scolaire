package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
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
     * Identifiant de la matière principale du professeur.
     * Stocké comme attribut simple (sans relation JPA) : la matière est résolue par jointure.
     */
    @Column(name = "matiere_id")
    private Long matiereId;
}
