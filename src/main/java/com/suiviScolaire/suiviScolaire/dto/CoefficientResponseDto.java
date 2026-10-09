package com.suiviScolaire.suiviScolaire.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

/**
 * Réponse contenant les données d'un coefficient de matière.
 * Le coefficient étant imbriqué dans sa matière, la matière n'est pas répétée ;
 * le niveau est exposé en objet complet.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoefficientResponseDto {
    private Long id;
    private BigDecimal valeur;
    private NiveauResponseDto niveau;
}