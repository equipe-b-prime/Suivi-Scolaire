package com.suiviScolaire.suiviScolaire.dto;

import lombok.*;

import java.util.List;

/**
 * Réponse de l'effectif d'une classe : la classe, ses effectifs et la liste de ses élèves.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClasseElevesResponseDto implements ResponseDto {
    private Long classeId;
    private String nom;
    private String niveau;
    private Integer effectifMax;
    private Long effectifReel;
    private List<EleveResponseDto> eleves;
}
