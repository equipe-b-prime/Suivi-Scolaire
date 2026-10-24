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
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des classes.
 */
@RestController
@RequestMapping("/classes")
public class ClasseController {

    private final ClasseService classeService;
    private final EleveService eleveService;
    private final NoteService noteService;

    /**
     * Constructeur injectant le service des classes.
     *
     * @param classeService le service des classes
     */
    public ClasseController(ClasseService classeService, EleveService eleveService, NoteService noteService) {
        this.classeService = classeService;
        this.eleveService = eleveService;
        this.noteService = noteService;
    }

    /**
     * Récupère une page de classes.
     *
     * @param page l'index de la page souhaitée
     * @param taille le nombre d'éléments souhaités par page
     * @return Response<ResponseDto> La page des classes encapsulée.
     */
    @GetMapping
    public Response<ResponseDto> getAll(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int taille) {
        return new Response<>(classeService.getAll(page, taille).getContent());
    }

    /**
     * Récupère un classe à partir de son identifiant.
     *
     * @param id l'identifiant du classe
     * @return Response<ResponseDto> Le classe trouvé encapsulé.
     * @throws Throwable si le classe n'est pas trouvé
     */
    /**
     * Récupère l'effectif d'une classe : ses effectifs et ses élèves triés par nom puis prénom.
     *
     * @param id l'identifiant de la classe
     * @return Response<ResponseDto> L'effectif de la classe encapsulé.
     */
    @GetMapping("/{id}/eleves")
    public Response<ClasseElevesResponseDto> getElevesByClasse(@PathVariable Long id) {
        return new Response<>(eleveService.getElevesByClasse(id));
    }

    /**
     * Récupère les notes d'une classe dans une matière, pour un trimestre donné.
     *
     * @param id l'identifiant de la classe
     * @param matiere l'identifiant de la matière (obligatoire)
     * @param trimestre le numéro du trimestre (obligatoire : 1, 2 ou 3)
     * @return Response<ResponseDto> Les résultats de la classe encapsulés.
     */
    @GetMapping("/{id}/notes")
    public Response<ClasseNotesResponseDto> getNotesByClasse(@PathVariable Long id,
                                                             @RequestParam Long matiere,
                                                             @RequestParam Integer trimestre) {
        if (trimestre < 1 || trimestre > 3) {
            throw new IllegalArgumentException("Le trimestre doit être 1, 2 ou 3.");
        }
        return new Response<>(noteService.getNotesByClasse(id, matiere, trimestre));
    }

    @GetMapping("/{id}")
    public Response<ResponseDto> getById(@PathVariable Long id) throws Throwable {
        return new Response<>(classeService.getById(id));
    }

    /**
     * Recherche un classe à partir de son nom.
     *
     * @param nom le nom du classe recherché
     * @return Response<ResponseDto> Les détails du classe trouvé.
     * @throws Throwable si aucun classe ne porte ce nom
     */
    @GetMapping("/recherche")
    public Response<ResponseDto> getByNom(@RequestParam String nom) throws Throwable {
        return new Response<>(classeService.getByNom(nom));
    }

    /**
     * Crée un classe.
     *
     * @param classeRequest les données de la requête
     * @return Response<ResponseDto> Le classe créé encapsulé.
     */
    @PostMapping
    public Response<ResponseDto> create(@Valid @RequestBody ClasseRequestDto classeRequest) {
        return new Response<>(classeService.create(classeRequest));
    }

    /**
     * Modifie le classe.
     *
     * @param id l'identifiant du classe à modifier
     * @param classeRequest les données de la requête
     * @return Response<ResponseDto> Le classe modifié encapsulé.
     */
    @PutMapping("/{id}")
    public Response<ResponseDto> update(@PathVariable Long id,
                                        @Valid @RequestBody ClasseRequestDto classeRequest) {
        return new Response<>(classeService.update(id, classeRequest));
    }

    /**
     * Supprime le classe à partir de son identifiant.
     *
     * @param id l'identifiant du classe
     * @return Response<ResponseDto> Le classe supprimé encapsulé.
     * @throws Throwable si le classe n'est pas trouvé
     */
    @DeleteMapping("/{id}")
    public Response<ResponseDto> delete(@PathVariable Long id) throws Throwable {
        return new Response<>(classeService.delete(id));
    }

    @GetMapping("/{id}/classement")
    public Response<ClassementResponseDto> getClassement(
            @PathVariable Long id,
            @RequestParam Integer trimestre) {
        if (trimestre < 1 || trimestre > 3) {
            throw new RequeteInvalideException("Le trimestre doit être 1, 2 ou 3.");
        }
        return new Response<>(noteService.getClassementClasse(id, trimestre));
    }
}
