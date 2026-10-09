package com.suiviScolaire.suiviScolaire.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.suiviScolaire.suiviScolaire.dto.NoteRequestDto;
import com.suiviScolaire.suiviScolaire.dto.NoteResponseDto;
import com.suiviScolaire.suiviScolaire.service.NoteService;

/**
 * Contrôleur REST exposant les endpoints de gestion des notes.
 */
@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    /**
     * Crée une nouvelle note.
     *
     * @param noteRequest les informations de la note à créer
     * @return la note créée
     */
    @PostMapping
    public NoteResponseDto createNote(@RequestBody NoteRequestDto noteRequest) {
        return noteService.createNote(noteRequest);
    }

    /**
     * Récupère une note à partir de son identifiant.
     *
     * @param id L'identifiant de la note à récupérer.
     * @return NoteResponseDto Les détails de la note récupérée.
     */
    @GetMapping("/{id}")
    public NoteResponseDto getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }

    /**
     * Récupère la liste de toutes les notes.
     *
     * @return List<NoteResponseDto> La liste des notes récupérées.
     */
    @GetMapping
    public List<NoteResponseDto> getAllNotes() {
        return noteService.getAllNotes();
    }

    /**
     * Modifie une note existante.
     *
     * @param id L'identifiant de la note à modifier.
     * @param noteRequest Les données de la requête pour modifier la note.
     * @return NoteResponseDto Les détails de la note modifiée.
     */
    @PutMapping("/{id}")
    public NoteResponseDto updateNoteById(@PathVariable Long id, @RequestBody NoteRequestDto noteRequest) {
        return noteService.updateNoteById(id, noteRequest);
    }

    /**
     * Supprime une note à partir de son identifiant.
     *
     * @param id L'identifiant de la note à supprimer.
     * @return NoteResponseDto Les détails de la note supprimée.
     */
    @DeleteMapping("/{id}")
    public NoteResponseDto deleteNoteById(@PathVariable Long id) {
        return noteService.deleteNoteById(id);
    }
}