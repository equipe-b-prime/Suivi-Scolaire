package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Represente un professeur de l'ecole
 */
@Entity
@Table(name = "professeur")
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
}