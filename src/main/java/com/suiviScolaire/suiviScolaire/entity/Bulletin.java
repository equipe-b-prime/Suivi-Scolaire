package com.suiviScolaire.suiviScolaire.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
/**
 * Représente  le bulletin d'un étudiant, qui contient la moyenne générale et le rang de l'étudiant dans sa classe
 */
@Entity
@Table(name = "bulettin")
public class Bulletin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(precision = 5, scale = 2)
    private BigDecimal moyenneGenerale;
    private Integer rang;

}
