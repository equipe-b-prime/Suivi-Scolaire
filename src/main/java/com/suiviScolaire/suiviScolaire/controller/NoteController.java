package com.suiviScolaire.suiviScolaire.controller;

import com.suiviScolaire.suiviScolaire.dto.NoteRequestDto;
import com.suiviScolaire.suiviScolaire.dto.ResponseDto;
import com.suiviScolaire.suiviScolaire.service.NoteService;
import com.suiviScolaire.suiviScolaire.utils.response.Response;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des notes.
 */
@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    /**
     * Constructeur injectant le service des notes.
     *
     * @param noteService le service des notes
     */
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Récupère une page de notes, avec le nombre total d'éléments et de pages.
     *
     * @param page l'index de la page souhaitée
     * @param size le nombre d'éléments par page (30 par défaut, 100 au maximum)
     * @param sort le tri appliqué ("valeur" ou "valeur,desc")
     * @return Response<ResponseDto> La page des notes encapsulée.
     */
    @GetMapping
    public Response<ResponseDto> getAll(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "30") int size,
                                        @RequestParam(defaultValue = "id") String sort) {
        Page<ResponseDto> resultat = noteService.getAllPage(page, size, sort);
        Response<ResponseDto> response = new Response<>(resultat.getContent());
        response.setTotalElements(resultat.getTotalElements());
        response.setTotalPages((long) resultat.getTotalPages());
        return response;
    }

    /**
     * Récupère un note à partir de son identifiant.
     *
     * @param id l'identifiant du note
     * @return Response<ResponseDto> Le note trouvé encapsulé.
     * @throws Throwable si le note n'est pas trouvé
     */
    @GetMapping("/{id}")
    public Response<ResponseDto> getById(@PathVariable Long id) throws Throwable {
        return new Response<>(noteService.getById(id));
    }

    /**
     * Crée un note.
     *
     * @param noteRequest les données de la requête
     * @return Response<ResponseDto> Le note créé encapsulé.
     */
    @PostMapping
    public Response<ResponseDto> create(@Valid @RequestBody NoteRequestDto noteRequest) {
        return new Response<>(noteService.create(noteRequest));
    }

    /**
     * Modifie le note.
     *
     * @param id l'identifiant du note à modifier
     * @param noteRequest les données de la requête
     * @return Response<ResponseDto> Le note modifié encapsulé.
     */
    @PutMapping("/{id}")
    public Response<ResponseDto> update(@PathVariable Long id,
                                        @Valid @RequestBody NoteRequestDto noteRequest) {
        return new Response<>(noteService.update(id, noteRequest));
    }

    /**
     * Supprime le note à partir de son identifiant.
     *
     * @param id l'identifiant du note
     * @return Response<ResponseDto> Le note supprimé encapsulé.
     * @throws Throwable si le note n'est pas trouvé
     */
    @DeleteMapping("/{id}")
    public Response<ResponseDto> delete(@PathVariable Long id) throws Throwable {
        return new Response<>(noteService.delete(id));
    }
}
