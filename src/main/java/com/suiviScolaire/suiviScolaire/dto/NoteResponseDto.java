package com.suiviScolaire.suiviScolaire.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Réponse contenant les données d'une note, avec l'inscription, l'enseignement,
 * la période et le type d'évaluation associés.
 * Les champs eleve et matiere ne sont renseignés que pour les vues détaillées.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteResponseDto implements ResponseDto {
    private Long id;
    private BigDecimal valeur;
    private LocalDate dateSaisie;
    private InscriptionResponseDto inscription;
    private EnseignementResponseDto enseignement;
    private PeriodeResponseDto periode;
    private TypeEvaluationResponseDto typeEvaluation;
    private EleveResponseDto eleve;
    private MatiereResponseDto matiere;
}