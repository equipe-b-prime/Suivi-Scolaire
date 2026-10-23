package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
/**
 * Représente  le coefficient dune matiere
 */
@Entity
@Table(name = "coefficient", indexes = {
    @Index(name = "idx_coefficient_niveau", columnList = "niveau_id"),
    @Index(name = "idx_coefficient_matiere", columnList = "matiere_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coefficient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal valeur;

    @ManyToOne
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;

    @ManyToOne
    @JoinColumn(name = "niveau_id", nullable = false)
    private Niveau niveau;
}
