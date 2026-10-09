package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
/**
 * Represente  la note d'un etudiant pour une matiere dans un semestre
 */
@Entity
@Table(name = "note")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal valeur;
    @ManyToOne
    @JoinColumn(name = "inscription_id", nullable = false)
    private Inscription inscription;
    @ManyToOne
    @JoinColumn(name = "enseignement_id", nullable = false)
    private Enseignement enseignement;
    @ManyToOne
    @JoinColumn(name = "periode_id", nullable = false)
    private Periode periode;
    @ManyToOne
    @JoinColumn(name = "type_evaluation_id", nullable = false)
    private TypeEvaluation typeEvaluation;
}