package com.suiviScolaire.suiviScolaire.controller;

import com.suiviScolaire.suiviScolaire.dto.ClasseElevesResponseDto;
import com.suiviScolaire.suiviScolaire.dto.ClasseNotesResponseDto;
import com.suiviScolaire.suiviScolaire.dto.ClasseRequestDto;
import com.suiviScolaire.suiviScolaire.dto.ClassementResponseDto;
import com.suiviScolaire.suiviScolaire.dto.ResponseDto;
import com.suiviScolaire.suiviScolaire.exception.RequeteInvalideException;
import com.suiviScolaire.suiviScolaire.service.ClasseService;
import com.suiviScolaire.suiviScolaire.service.EleveService;
import com.suiviScolaire.suiviScolaire.service.NoteService;
import com.suiviScolaire.suiviScolaire.utils.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classes")
@Tag(name = "Classes", description = "Endpoints pour la gestion des classes, des effectifs, des notes et des classements scolaires")
public class ClasseController {

    private final ClasseService classeService;
    private final EleveService eleveService;
    private final NoteService noteService;

    public ClasseController(ClasseService classeService, EleveService eleveService, NoteService noteService) {
        this.classeService = classeService;
        this.eleveService = eleveService;
        this.noteService = noteService;
    }

    @Operation(summary = "Lister toutes les classes", description = "Récupère la liste paginée de toutes les classes enregistrées.")
    @GetMapping
    public Response<ResponseDto> getAll(
            @Parameter(description = "Numéro de la page (commence à 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Nombre d'éléments par page") @RequestParam(defaultValue = "10") int taille) {
        return new Response<>(classeService.getAll(page, taille).getContent());
    }

    @Operation(summary = "Obtenir les élèves d'une classe", description = "Récupère l'effectif et la liste des élèves d'une classe triés par nom puis prénom.")
    @GetMapping("/{id}/eleves")
    public Response<ClasseElevesResponseDto> getElevesByClasse(
            @Parameter(description = "ID unique de la classe", required = true) @PathVariable Long id) {
        return new Response<>(eleveService.getElevesByClasse(id));
    }

    @Operation(summary = "Obtenir les notes d'une classe par matière et trimestre", description = "Récupère les notes obtenues par les élèves d'une classe pour une matière et un trimestre donnés.")
    @GetMapping("/{id}/notes")
    public Response<ClasseNotesResponseDto> getNotesByClasse(
            @Parameter(description = "ID unique de la classe", required = true) @PathVariable Long id,
            @Parameter(description = "ID unique de la matière", required = true) @RequestParam Long matiere,
            @Parameter(description = "Numéro du trimestre (1, 2 ou 3)", example = "1", required = true) @RequestParam Integer trimestre) {
        if (trimestre < 1 || trimestre > 3) {
            throw new IllegalArgumentException("Le trimestre doit être 1, 2 ou 3.");
        }
        return new Response<>(noteService.getNotesByClasse(id, matiere, trimestre));
    }

    @Operation(summary = "Obtenir une classe par ID", description = "Retourne les détails d'une classe spécifique à partir de son identifiant.")
    @GetMapping("/{id}")
    public Response<ResponseDto> getById(
            @Parameter(description = "ID unique de la classe", required = true) @PathVariable Long id) throws Throwable {
        return new Response<>(classeService.getById(id));
    }

    @Operation(summary = "Rechercher une classe par nom", description = "Permet de rechercher une classe en fonction de son nom ou libellé.")
    @GetMapping("/recherche")
    public Response<ResponseDto> getByNom(
            @Parameter(description = "Nom ou partie du nom de la classe", required = true) @RequestParam String nom) throws Throwable {
        return new Response<>(classeService.getByNom(nom));
    }

    @Operation(summary = "Créer une nouvelle classe", description = "Enregistre une nouvelle classe dans le système.")
    @PostMapping
    public Response<ResponseDto> create(@Valid @RequestBody ClasseRequestDto classeRequest) {
        return new Response<>(classeService.create(classeRequest));
    }

    @Operation(summary = "Mettre à jour une classe", description = "Modifie les informations d'une classe existante.")
    @PutMapping("/{id}")
    public Response<ResponseDto> update(
            @Parameter(description = "ID de la classe à modifier", required = true) @PathVariable Long id,
            @Valid @RequestBody ClasseRequestDto classeRequest) {
        return new Response<>(classeService.update(id, classeRequest));
    }

    @Operation(summary = "Supprimer une classe", description = "Supprime une classe à partir de son ID.")
    @DeleteMapping("/{id}")
    public Response<ResponseDto> delete(
            @Parameter(description = "ID de la classe à supprimer", required = true) @PathVariable Long id) throws Throwable {
        return new Response<>(classeService.delete(id));
    }

    @Operation(summary = "Obtenir le classement des élèves d'une classe", description = "Génère le classement général des élèves d'une classe pour un trimestre spécifique.")
    @GetMapping("/{id}/classement")
    public Response<ClassementResponseDto> getClassement(
            @Parameter(description = "ID unique de la classe", required = true) @PathVariable Long id,
            @Parameter(description = "Numéro du trimestre (1, 2 ou 3)", example = "1", required = true) @RequestParam Integer trimestre) {
        if (trimestre < 1 || trimestre > 3) {
            throw new RequeteInvalideException("Le trimestre doit être 1, 2 ou 3.");
        }
        return new Response<>(noteService.getClassementClasse(id, trimestre));
    }
}