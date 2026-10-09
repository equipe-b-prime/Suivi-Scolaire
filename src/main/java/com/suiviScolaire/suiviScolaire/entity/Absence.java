package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Représente une absence d'un étudiant
 */
@Entity
@Table(name = "absence")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean estJustifiee = false;

    @ManyToOne
    @JoinColumn(name = "inscription_id", nullable = false)
    private Inscription inscription;

}
