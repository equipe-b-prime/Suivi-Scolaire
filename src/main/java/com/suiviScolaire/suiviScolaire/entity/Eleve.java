package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
/**
 * Represente un élève dans le système de suivi scolaire
 */
@Entity
@Table(name = "eleve", indexes = {
    @Index(name = "idx_eleve_nom_prenom", columnList = "nom, prenom")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Eleve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false)
    private LocalDate dateNaissance;

    @Column(name = "moyenne_trimestre_courant", precision = 5, scale = 2)
    private BigDecimal moyenneTrimestreCourant;

    @Column(name = "ecart_trimestre_precedent", precision = 5, scale = 2)
    private BigDecimal ecartTrimestrePrec;

    @Column(name = "est_sigle")
    private Boolean estSigle = false;

    @Column(name = "trimestre_sigle")
    private Integer trimestreSigle;
}
