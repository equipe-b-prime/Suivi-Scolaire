package com.suiviScolaire.suiviScolaire.controller;

import com.suiviScolaire.suiviScolaire.dto.ProfesseurRequestDto;
import com.suiviScolaire.suiviScolaire.dto.ResponseDto;
import com.suiviScolaire.suiviScolaire.service.ProfesseurService;
import com.suiviScolaire.suiviScolaire.utils.response.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des professeurs.
 */
@RestController
@RequestMapping("/professeurs")
public class ProfesseurController {

    private final ProfesseurService professeurService;

    /**
     * Constructeur injectant le service des professeurs.
     *
     * @param professeurService le service des professeurs
     */
    public ProfesseurController(ProfesseurService professeurService) {
        this.professeurService = professeurService;
    }

    /**
     * Récupère une page de professeurs.
     *
     * @param page l'index de la page souhaitée
     * @param taille le nombre d'éléments souhaités par page
     * @return Response<ResponseDto> La page des professeurs encapsulée.
     */
    @GetMapping
    public Response<ResponseDto> getAll(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int taille) {
        return new Response<>(professeurService.getAll(page, taille).getContent());
    }

    /**
     * Récupère un professeur à partir de son identifiant.
     *
     * @param id l'identifiant du professeur
     * @return Response<ResponseDto> Le professeur trouvé encapsulé.
     * @throws Throwable si le professeur n'est pas trouvé
     */
    @GetMapping("/{id}")
    public Response<ResponseDto> getById(@PathVariable Long id) throws Throwable {
        return new Response<>(professeurService.getById(id));
    }

    /**
     * Recherche un professeur à partir de son nom.
     *
     * @param nom le nom du professeur recherché
     * @return Response<ResponseDto> Les détails du professeur trouvé.
     * @throws Throwable si aucun professeur ne porte ce nom
     */
    @GetMapping("/recherche")
    public Response<ResponseDto> getByNom(@RequestParam String nom) throws Throwable {
        return new Response<>(professeurService.getByNom(nom));
    }

    /**
     * Crée un professeur.
     *
     * @param professeurRequest les données de la requête
     * @return Response<ResponseDto> Le professeur créé encapsulé.
     */
    @PostMapping
    public Response<ResponseDto> create(@Valid @RequestBody ProfesseurRequestDto professeurRequest) {
        return new Response<>(professeurService.create(professeurRequest));
    }

    /**
     * Modifie le professeur.
     *
     * @param id l'identifiant du professeur à modifier
     * @param professeurRequest les données de la requête
     * @return Response<ResponseDto> Le professeur modifié encapsulé.
     */
    @PutMapping("/{id}")
    public Response<ResponseDto> update(@PathVariable Long id,
                                        @Valid @RequestBody ProfesseurRequestDto professeurRequest) {
        return new Response<>(professeurService.update(id, professeurRequest));
    }

    /**
     * Supprime le professeur à partir de son identifiant.
     *
     * @param id l'identifiant du professeur
     * @return Response<ResponseDto> Le professeur supprimé encapsulé.
     * @throws Throwable si le professeur n'est pas trouvé
     */
    @DeleteMapping("/{id}")
    public Response<ResponseDto> delete(@PathVariable Long id) throws Throwable {
        return new Response<>(professeurService.delete(id));
    }
}
