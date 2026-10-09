package com.suiviScolaire.suiviScolaire.entity;


import jakarta.persistence.*;
import lombok.*;
/**
 * Represente une periode scolaire (ex: Trimestre, Semestre, etc.)
 */
@Entity
@Table(name = "periode")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Periode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nom;
}
