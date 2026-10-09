package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Represente  le niveau d'un élève dans le système de suivi scolaire
 */
@Entity
@Table(name = "niveau")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Niveau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String nom;

}
