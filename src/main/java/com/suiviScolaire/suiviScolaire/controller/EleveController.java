package com.suiviScolaire.suiviScolaire.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.suiviScolaire.suiviScolaire.dto.EleveRequestDto;
import com.suiviScolaire.suiviScolaire.dto.EleveResponseDto;
import com.suiviScolaire.suiviScolaire.service.EleveService;

/**
 * Contrôleur REST exposant les endpoints de gestion des élèves.
 */
@RestController
@RequestMapping("/eleves")
@RequiredArgsConstructor
public class  EleveController {
    private final EleveService eleveService;

    /**
     * Crée un nouvel élève.
     *
     * @param eleveRequest les informations de l'élève à créer
     * @return l'élève créé
     */
    @PostMapping
    public EleveResponseDto createEleve(@RequestBody EleveRequestDto eleveRequest) {
        return eleveService.createEleve(eleveRequest);
    }

    /**
     * Récupère un élève à partir de son identifiant.
     *
     * @param id L'identifiant de l'élève à récupérer.
     * @return EleveResponseDto Les détails de l'élève récupéré.
     */
    @GetMapping("/{id}")
    public EleveResponseDto getEleveById(@PathVariable Long id) {
        return eleveService.getEleveById(id);
    }

    /**
     * Récupère la liste de tous les élèves.
     *
     * @return List<EleveResponseDto> La liste des élèves récupérés.
     */
    @GetMapping
    public List<EleveResponseDto> getAllEleves() {
        return eleveService.getAllEleves();
    }

    /**
     * Modifie un élève existant.
     *
     * @param id L'identifiant de l'élève à modifier.
     * @param eleveRequest Les données de la requête pour modifier l'élève.
     * @return EleveResponseDto Les détails de l'élève modifié.
     */
    @PutMapping("/{id}")
    public EleveResponseDto updateEleveById(@PathVariable Long id, @RequestBody EleveRequestDto eleveRequest) {
        return eleveService.updateEleveById(id, eleveRequest);
    }

    /**
     * Supprime un élève à partir de son identifiant.
     *
     * @param id L'identifiant de l'élève à supprimer.
     * @return EleveResponseDto Les détails de l'élève supprimé.
     */
    @DeleteMapping("/{id}")
    public EleveResponseDto deleteEleveById(@PathVariable Long id) {
        return eleveService.deleteEleveById(id);
    }
    //d
}