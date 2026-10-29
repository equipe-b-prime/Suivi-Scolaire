package com.suiviScolaire.suiviScolaire.controller;

import com.suiviScolaire.suiviScolaire.dto.MatiereRequestDto;
import com.suiviScolaire.suiviScolaire.dto.ResponseDto;
import com.suiviScolaire.suiviScolaire.service.MatiereService;
import com.suiviScolaire.suiviScolaire.utils.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matieres")
@Tag(name = "Matières", description = "Endpoints pour la gestion du référentiel des matières enseignées")
public class MatiereController {

    private final MatiereService matiereService;

    public MatiereController(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    @Operation(summary = "Lister toutes les matières", description = "Récupère la liste paginée de toutes les matières enregistrées.")
    @GetMapping
    public Response<ResponseDto> getAll(
            @Parameter(description = "Numéro de la page (commence à 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Nombre d'éléments par page") @RequestParam(defaultValue = "10") int taille) {
        return new Response<>(matiereService.getAll(page, taille).getContent());
    }

    @Operation(summary = "Obtenir une matière par ID", description = "Retourne les détails d'une matière spécifique à partir de son identifiant.")
    @GetMapping("/{id}")
    public Response<ResponseDto> getById(
            @Parameter(description = "ID unique de la matière", required = true) @PathVariable Long id) throws Throwable {
        return new Response<>(matiereService.getById(id));
    }

    @Operation(summary = "Rechercher une matière par nom", description = "Permet de rechercher une matière en fonction de son nom ou intitule.")
    @GetMapping("/recherche")
    public Response<ResponseDto> getByNom(
            @Parameter(description = "Nom ou partie du nom de la matière", required = true) @RequestParam String nom) throws Throwable {
        return new Response<>(matiereService.getByNom(nom));
    }

    @Operation(summary = "Créer une nouvelle matière", description = "Enregistre une nouvelle matière dans le système.")
    @PostMapping
    public Response<ResponseDto> create(@Valid @RequestBody MatiereRequestDto matiereRequest) {
        return new Response<>(matiereService.create(matiereRequest));
    }

    @Operation(summary = "Mettre à jour une matière", description = "Modifie les informations d'une matière existante.")
    @PutMapping("/{id}")
    public Response<ResponseDto> update(
            @Parameter(description = "ID de la matière à modifier", required = true) @PathVariable Long id,
            @Valid @RequestBody MatiereRequestDto matiereRequest) {
        return new Response<>(matiereService.update(id, matiereRequest));
    }

    @Operation(summary = "Supprimer une matière", description = "Supprime une matière à partir de son ID.")
    @DeleteMapping("/{id}")
    public Response<ResponseDto> delete(
            @Parameter(description = "ID de la matière à supprimer", required = true) @PathVariable Long id) throws Throwable {
        return new Response<>(matiereService.delete(id));
    }
}