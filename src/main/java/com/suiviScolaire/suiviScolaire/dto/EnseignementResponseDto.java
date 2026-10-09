package com.suiviScolaire.suiviScolaire.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * Réponse contenant les données d'un enseignement,
 * avec la matière et le professeur sous forme d'objets complets.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnseignementResponseDto {
    private Long id;
    private MatiereResponseDto matiere;
    private ProfesseurResponseDto professeur;
}