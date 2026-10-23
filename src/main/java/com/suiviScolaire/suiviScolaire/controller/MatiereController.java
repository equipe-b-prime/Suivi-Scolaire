package com.suiviScolaire.suiviScolaire.controller;

import com.suiviScolaire.suiviScolaire.dto.MatiereRequestDto;
import com.suiviScolaire.suiviScolaire.dto.ResponseDto;
import com.suiviScolaire.suiviScolaire.service.MatiereService;
import com.suiviScolaire.suiviScolaire.utils.response.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des matieres.
 */
@RestController
@RequestMapping("/matieres")
public class MatiereController {

    private final MatiereService matiereService;

    /**
     * Constructeur injectant le service des matieres.
     *
     * @param matiereService le service des matieres
     */
    public MatiereController(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    /**
     * Récupère une page de matieres.
     *
     * @param page l'index de la page souhaitée
     * @param taille le nombre d'éléments souhaités par page
     * @return Response<ResponseDto> La page des matieres encapsulée.
     */
    @GetMapping
    public Response<ResponseDto> getAll(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int taille) {
        return new Response<>(matiereService.getAll(page, taille).getContent());
    }

    /**
     * Récupère un matiere à partir de son identifiant.
     *
     * @param id l'identifiant du matiere
     * @return Response<ResponseDto> Le matiere trouvé encapsulé.
     * @throws Throwable si le matiere n'est pas trouvé
     */
    @GetMapping("/{id}")
    public Response<ResponseDto> getById(@PathVariable Long id) throws Throwable {
        return new Response<>(matiereService.getById(id));
    }

    /**
     * Recherche un matiere à partir de son nom.
     *
     * @param nom le nom du matiere recherché
     * @return Response<ResponseDto> Les détails du matiere trouvé.
     * @throws Throwable si aucun matiere ne porte ce nom
     */
    @GetMapping("/recherche")
    public Response<ResponseDto> getByNom(@RequestParam String nom) throws Throwable {
        return new Response<>(matiereService.getByNom(nom));
    }

    /**
     * Crée un matiere.
     *
     * @param matiereRequest les données de la requête
     * @return Response<ResponseDto> Le matiere créé encapsulé.
     */
    @PostMapping
    public Response<ResponseDto> create(@Valid @RequestBody MatiereRequestDto matiereRequest) {
        return new Response<>(matiereService.create(matiereRequest));
    }

    /**
     * Modifie le matiere.
     *
     * @param id l'identifiant du matiere à modifier
     * @param matiereRequest les données de la requête
     * @return Response<ResponseDto> Le matiere modifié encapsulé.
     */
    @PutMapping("/{id}")
    public Response<ResponseDto> update(@PathVariable Long id,
                                        @Valid @RequestBody MatiereRequestDto matiereRequest) {
        return new Response<>(matiereService.update(id, matiereRequest));
    }

    /**
     * Supprime le matiere à partir de son identifiant.
     *
     * @param id l'identifiant du matiere
     * @return Response<ResponseDto> Le matiere supprimé encapsulé.
     * @throws Throwable si le matiere n'est pas trouvé
     */
    @DeleteMapping("/{id}")
    public Response<ResponseDto> delete(@PathVariable Long id) throws Throwable {
        return new Response<>(matiereService.delete(id));
    }
}
