package com.suiviScolaire.suiviScolaire.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * Réponse contenant les données d'une inscription,
 * avec l'année, l'élève et la classe sous forme d'objets complets.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InscriptionResponseDto {
    private Long id;
    private AnneeResponseDto annee;
    private EleveResponseDto eleve;
    private ClasseResponseDto classe;
}