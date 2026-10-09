package com.suiviScolaire.suiviScolaire.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

/**
 * Réponse contenant les données d'un type d'évaluation.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypeEvaluationResponseDto {
    private Long id;
    private BigDecimal poid;
    private String nom;
}