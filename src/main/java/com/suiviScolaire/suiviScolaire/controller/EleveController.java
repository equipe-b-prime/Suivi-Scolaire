package com.suiviScolaire.suiviScolaire.controller;

import com.suiviScolaire.suiviScolaire.dto.EleveRequestDto;
import com.suiviScolaire.suiviScolaire.dto.NotesParMatiereResponseDto;
import com.suiviScolaire.suiviScolaire.dto.ResponseDto;
import com.suiviScolaire.suiviScolaire.service.EleveService;
import com.suiviScolaire.suiviScolaire.exception.RequeteInvalideException;
import com.suiviScolaire.suiviScolaire.service.NoteService;
import com.suiviScolaire.suiviScolaire.utils.response.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des eleves.
 */
@RestController
@RequestMapping("/eleves")
public class EleveController {

    private final EleveService eleveService;
    private final NoteService noteService;

    /**
     * Constructeur injectant le service des eleves.
     *
     * @param eleveService le service des eleves
     */
    public EleveController(EleveService eleveService, NoteService noteService) {
        this.eleveService = eleveService;
        this.noteService = noteService;
    }

    /**
     * Récupère une page de eleves.
     *
     * @param page l'index de la page souhaitée
     * @param taille le nombre d'éléments souhaités par page
     * @return Response<ResponseDto> La page des eleves encapsulée.
     */
    @GetMapping
    public Response<ResponseDto> getAll(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int taille,
                                        @RequestParam(required = false) String nom) {
        if (nom != null) {
            if (nom.isBlank()) {
                throw new RequeteInvalideException("Le nom de recherche ne peut pas être vide.");
            }
            return new Response<>(eleveService.searchByNom(nom));
        }
        return new Response<>(eleveService.getAll(page, taille).getContent());
    }

    /**
     * Récupère les notes d'un élève, regroupées par matière.
     *
     * @param id l'identifiant de l'élève
     * @param trimestre le numéro du trimestre (optionnel : 1, 2 ou 3)
     * @return Response<ResponseDto> Les notes de l'élève regroupées par matière.
     */
    @GetMapping("/{id}/notes")
    public Response<NotesParMatiereResponseDto> getNotesByEleve(@PathVariable Long id,
                                                               @RequestParam(required = false) Integer trimestre) {
        if (trimestre != null && (trimestre < 1 || trimestre > 3)) {
            throw new RequeteInvalideException("Le trimestre doit être 1, 2 ou 3.");
        }
        return new Response<>(noteService.getNotesByEleve(id, trimestre));
    }

    /**
     * Récupère un eleve à partir de son identifiant.
     *
     * @param id l'identifiant du eleve
     * @return Response<ResponseDto> Le eleve trouvé encapsulé.
     * @throws Throwable si le eleve n'est pas trouvé
     */
    @GetMapping("/{id}")
    public Response<ResponseDto> getById(@PathVariable Long id) throws Throwable {
        return new Response<>(eleveService.getById(id));
    }

    /**
     * Recherche un eleve à partir de son nom.
     *
     * @param nom le nom du eleve recherché
     * @return Response<ResponseDto> Les détails du eleve trouvé.
     * @throws Throwable si aucun eleve ne porte ce nom
     */
    @GetMapping("/recherche")
    public Response<ResponseDto> getByNom(@RequestParam String nom) throws Throwable {
        return new Response<>(eleveService.getByNom(nom));
    }

    /**
     * Crée un eleve.
     *
     * @param eleveRequest les données de la requête
     * @return Response<ResponseDto> Le eleve créé encapsulé.
     */
    @PostMapping
    public Response<ResponseDto> create(@Valid @RequestBody EleveRequestDto eleveRequest) {
        return new Response<>(eleveService.create(eleveRequest));
    }

    /**
     * Modifie le eleve.
     *
     * @param id l'identifiant du eleve à modifier
     * @param eleveRequest les données de la requête
     * @return Response<ResponseDto> Le eleve modifié encapsulé.
     */
    @PutMapping("/{id}")
    public Response<ResponseDto> update(@PathVariable Long id,
                                        @Valid @RequestBody EleveRequestDto eleveRequest) {
        return new Response<>(eleveService.update(id, eleveRequest));
    }

    /**
     * Supprime le eleve à partir de son identifiant.
     *
     * @param id l'identifiant du eleve
     * @return Response<ResponseDto> Le eleve supprimé encapsulé.
     * @throws Throwable si le eleve n'est pas trouvé
     */
    @DeleteMapping("/{id}")
    public Response<ResponseDto> delete(@PathVariable Long id) throws Throwable {
        return new Response<>(eleveService.delete(id));
    }
}
