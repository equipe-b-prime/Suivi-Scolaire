package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Représente  annee  scolaire
 */
@Entity
@Table(name = "annee")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Annee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String libelle;


}
