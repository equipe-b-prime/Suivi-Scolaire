package com.suiviScolaire.suiviScolaire.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassementEleveDto {
    private Long eleveId;
    private String nom;
    private String prenom;
    private Integer rang;
    private BigDecimal moyenneGenerale;
}

