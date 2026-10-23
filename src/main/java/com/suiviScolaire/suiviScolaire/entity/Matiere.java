package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
/**
 * Represente une matiere enseignée dans l'école
 */
@Entity
@Table(name = "matiere", indexes = {
    @Index(name = "idx_matiere_nom", columnList = "nom")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Matiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nom;

    /**
     * Coefficient de la matière (attribut simple, sans relation).
     */
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal coefficient;
}
