package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.*;

import java.math.BigDecimal;
/**
 * Represente  la note d'un etudiant pour une matiere dans un semestre
 */
@Entity
@Table(name = "note", indexes = {
    @Index(name = "idx_note_inscription", columnList = "inscription_id"),
    @Index(name = "idx_note_enseignement", columnList = "enseignement_id"),
    @Index(name = "idx_note_periode", columnList = "periode_id"),
    @Index(name = "idx_note_type_evaluation", columnList = "type_evaluation_id")
})
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

    /**
     * Date de saisie de la note (attribut simple, sans relation).
     */
    @Column(name = "date_saisie", nullable = false)
    private LocalDate dateSaisie;
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