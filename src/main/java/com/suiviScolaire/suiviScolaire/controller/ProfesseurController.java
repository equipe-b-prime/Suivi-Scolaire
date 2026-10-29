package com.suiviScolaire.suiviScolaire.controller;

import com.suiviScolaire.suiviScolaire.dto.ProfesseurRequestDto;
import com.suiviScolaire.suiviScolaire.dto.ResponseDto;
import com.suiviScolaire.suiviScolaire.service.ProfesseurService;
import com.suiviScolaire.suiviScolaire.utils.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/professeurs")
@Tag(name = "Professeurs", description = "Endpoints pour la gestion des professeurs et de leurs affectations pédagogiques")
public class ProfesseurController {

    private final ProfesseurService professeurService;

    public ProfesseurController(ProfesseurService professeurService) {
        this.professeurService = professeurService;
    }

    @Operation(summary = "Lister tous les professeurs", description = "Récupère la liste paginée de tous les professeurs enregistrés.")
    @GetMapping
    public Response<ResponseDto> getAll(
            @Parameter(description = "Numéro de la page (commence à 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Nombre d'éléments par page") @RequestParam(defaultValue = "10") int taille) {
        return new Response<>(professeurService.getAll(page, taille).getContent());
    }

    @Operation(summary = "Obtenir un professeur par ID", description = "Retourne les détails d'un professeur spécifique à partir de son identifiant.")
    @GetMapping("/{id}")
    public Response<ResponseDto> getById(
            @Parameter(description = "ID unique du professeur", required = true) @PathVariable Long id) throws Throwable {
        return new Response<>(professeurService.getById(id));
    }

    @Operation(summary = "Rechercher un professeur par nom", description = "Permet de rechercher un professeur en fonction de son nom ou prénom.")
    @GetMapping("/recherche")
    public Response<ResponseDto> getByNom(
            @Parameter(description = "Nom ou partie du nom du professeur", required = true) @RequestParam String nom) throws Throwable {
        return new Response<>(professeurService.getByNom(nom));
    }

    @Operation(summary = "Créer un nouveau professeur", description = "Enregistre un nouveau professeur dans le système.")
    @PostMapping
    public Response<ResponseDto> create(@Valid @RequestBody ProfesseurRequestDto professeurRequest) {
        return new Response<>(professeurService.create(professeurRequest));
    }

    @Operation(summary = "Mettre à jour un professeur", description = "Modifie les informations d'un professeur existant.")
    @PutMapping("/{id}")
    public Response<ResponseDto> update(
            @Parameter(description = "ID du professeur à modifier", required = true) @PathVariable Long id,
            @Valid @RequestBody ProfesseurRequestDto professeurRequest) {
        return new Response<>(professeurService.update(id, professeurRequest));
    }

    @Operation(summary = "Supprimer un professeur", description = "Supprime un professeur à partir de son ID.")
    @DeleteMapping("/{id}")
    public Response<ResponseDto> delete(
            @Parameter(description = "ID du professeur à supprimer", required = true) @PathVariable Long id) throws Throwable {
        return new Response<>(professeurService.delete(id));
    }

    @Operation(summary = "Affecter une matière et une classe à un professeur", description = "Associe un professeur à une matière spécifique dans une classe donnée.")
    @PostMapping("/{id}/affectations")
    public Response<ResponseDto> affecterMatiereAProfesseurDansClasse(
            @Parameter(description = "ID unique du professeur", required = true) @PathVariable Long id,
            @Parameter(description = "ID unique de la matière", required = true) @RequestParam Long matiereId,
            @Parameter(description = "ID unique de la classe", required = true) @RequestParam Long classeId) {
        return new Response<>(professeurService.affecterMatiereAProfesseurDansClasse(id, matiereId, classeId));
    }
}