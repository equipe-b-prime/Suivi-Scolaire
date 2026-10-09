package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Représente  la classe dun etudiant
 */
@Entity
@Table(name = "classe")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nom;

    @Column(nullable = false, length = 50)
    private String niveau;

    @Column(name = "effectif_max", nullable = false)
    private Integer effectifMax;
}
