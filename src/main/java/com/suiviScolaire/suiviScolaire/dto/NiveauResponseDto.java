package com.suiviScolaire.suiviScolaire.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * Réponse contenant les données d'un niveau.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NiveauResponseDto {
    private Long id;
    private String nom;
}