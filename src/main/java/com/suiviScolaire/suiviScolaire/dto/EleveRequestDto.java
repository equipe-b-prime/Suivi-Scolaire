package com.suiviScolaire.suiviScolaire.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Requête de création ou de modification d'un élève.
 * La classe de rattachement (et l'année) sont optionnelles à la création :
 * elles servent à créer l'inscription correspondante.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EleveRequestDto implements RequestDto {
    private Long id;
    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule;
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    @NotBlank(message = "Le prenom est obligatoire")
    private String prenom;
    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateNaissance;
    @NotNull(message = "La classe est obligatoire")
    @Positive(message = "L identifiant de la classe doit être positif")
    private Long classeId;
    private Long anneeId;
}