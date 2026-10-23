package com.suiviScolaire.suiviScolaire.dto;

import lombok.*;

import java.util.List;

/**
 * Réponse des notes d'un élève, regroupées par matière.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotesParMatiereResponseDto implements ResponseDto {
    private MatiereResponseDto matiere;
    private List<NoteResponseDto> notes;
}
