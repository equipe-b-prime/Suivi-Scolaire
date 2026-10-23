package com.suiviScolaire.suiviScolaire.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Réponse des résultats d'une classe dans une matière, pour un trimestre donné.
 * Les notes sont regroupées par élève et la moyenne de la classe est fournie.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClasseNotesResponseDto implements ResponseDto {
    private Long classeId;
    private String classeNom;
    private MatiereResponseDto matiere;
    private Integer trimestre;
    private BigDecimal moyenneClasse;
    private List<NotesParEleveResponseDto> eleves;
}
