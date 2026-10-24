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
public class DetailCoefficientDto {
    private Long matiereId;
    private String matiereNom;
    private BigDecimal moyenneMatiere;
    private BigDecimal coefficient;
    private BigDecimal contribution;
}

