package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
/**
 * Represente un type d'evaluation (examen, devoir, interro, etc.)
 */
@Entity
@Table(name = "type_evaluation")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypeEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String code;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal poid;

    @Column(nullable = false, length = 50)
    private String nom;
}
