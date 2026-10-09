package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Represente une matiere enseignée dans l'école
 */
@Entity
@Table(name = "matiere")
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
}