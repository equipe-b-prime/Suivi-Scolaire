package com.suiviScolaire.suiviScolaire.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * Réponse contenant les données d'une période.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeriodeResponseDto {
    private Long id;
    private String nom;
}