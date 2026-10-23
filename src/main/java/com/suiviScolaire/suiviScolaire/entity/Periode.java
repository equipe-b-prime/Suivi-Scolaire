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

    /**
     * Indique si la période (trimestre) est clôturée.
     * Tant qu'elle ne l'est pas, les notes et coefficients restent modifiables.
     */
    @Column(name = "cloture", nullable = false)
    private boolean cloture;

    /**
     * Numéro du trimestre (1, 2, 3...), utilisé par les endpoints de consultation.
     */
    @Column(name = "numero", nullable = false)
    private Integer numero;
}
