package com.suiviScolaire.suiviScolaire.controller;

import com.suiviScolaire.suiviScolaire.dto.NoteRequestDto;
import com.suiviScolaire.suiviScolaire.dto.ResponseDto;
import com.suiviScolaire.suiviScolaire.service.NoteService;
import com.suiviScolaire.suiviScolaire.utils.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes")
@Tag(name = "Notes", description = "Endpoints pour la saisie, la modification et la consultation des notes des élèves")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @Operation(summary = "Lister toutes les notes", description = "Récupère la liste paginée de toutes les notes enregistrées avec gestion du tri et de la pagination.")
    @GetMapping
    public Response<ResponseDto> getAll(
            @Parameter(description = "Numéro de la page (commence à 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Nombre d'éléments par page") @RequestParam(defaultValue = "30") int size,
            @Parameter(description = "Champ et sens du tri (ex: 'valeur' ou 'valeur,desc')") @RequestParam(defaultValue = "id") String sort) {
        Page<ResponseDto> resultat = noteService.getAllPage(page, size, sort);
        Response<ResponseDto> response = new Response<>(resultat.getContent());
        response.setTotalElements(resultat.getTotalElements());
        response.setTotalPages((long) resultat.getTotalPages());
        return response;
    }

    @Operation(summary = "Obtenir une note par ID", description = "Retourne les détails d'une note spécifique à partir de son identifiant.")
    @GetMapping("/{id}")
    public Response<ResponseDto> getById(
            @Parameter(description = "ID unique de la note", required = true) @PathVariable Long id) throws Throwable {
        return new Response<>(noteService.getById(id));
    }

    @Operation(summary = "Créer une nouvelle note", description = "Enregistre une nouvelle note attribuée à un élève.")
    @PostMapping
    public Response<ResponseDto> create(@Valid @RequestBody NoteRequestDto noteRequest) {
        return new Response<>(noteService.create(noteRequest));
    }

    @Operation(summary = "Mettre à jour une note", description = "Modifie la valeur ou les informations d'une note existante.")
    @PutMapping("/{id}")
    public Response<ResponseDto> update(
            @Parameter(description = "ID de la note à modifier", required = true) @PathVariable Long id,
            @Valid @RequestBody NoteRequestDto noteRequest) {
        return new Response<>(noteService.update(id, noteRequest));
    }

    @Operation(summary = "Supprimer une note", description = "Supprime une note à partir de son ID.")
    @DeleteMapping("/{id}")
    public Response<ResponseDto> delete(
            @Parameter(description = "ID de la note à supprimer", required = true) @PathVariable Long id) throws Throwable {
        return new Response<>(noteService.delete(id));
    }
}