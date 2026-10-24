package com.suiviScolaire.suiviScolaire.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
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

    /**
     * Date de réouverture de la période (null si non clôturée ou déjà rouverte).
     */
    @Column(name = "date_rouverte")
    private LocalDate dateRouvre;

    /**
     * Motif de la réouverture (obligatoire, minimum 20 caractères).
     */
    @Column(name = "motif_rouverte")
    private String motifRouvre;

    /**
     * Identifiant de l'utilisateur qui a procédé à la réouverture.
     */
    @Column(name = "utilisateur_rouverte")
    private String utilisateurRouvre;
}
