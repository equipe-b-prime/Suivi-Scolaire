package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Represente un enseignement, qui relie un professeur à une matière
 */
@Entity
@Table(name = "enseignement")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Enseignement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;
    @ManyToOne
    @JoinColumn(name = "professeur_id", nullable = false)
    private Professeur professeur;
}
