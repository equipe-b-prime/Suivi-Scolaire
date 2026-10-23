package com.suiviScolaire.suiviScolaire.dto;

import lombok.*;

import java.util.List;

/**
 * Réponse des notes d'un élève dans une matière donnée.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotesParEleveResponseDto implements ResponseDto {
    private EleveResponseDto eleve;
    private List<NoteResponseDto> notes;
}
