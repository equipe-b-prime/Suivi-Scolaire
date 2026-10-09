package com.suiviScolaire.suiviScolaire.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.suiviScolaire.suiviScolaire.dto.ProfesseurRequestDto;
import com.suiviScolaire.suiviScolaire.dto.ProfesseurResponseDto;
import com.suiviScolaire.suiviScolaire.service.ProfesseurService;

/**
 * Contrôleur REST exposant les endpoints de gestion des professeurs.
 */
@RestController
@RequestMapping("/professeurs")
@RequiredArgsConstructor
public class ProfesseurController {
    private final ProfesseurService professeurService;

    /**
     * Crée un nouveau professeur.
     *
     * @param professeurRequest les informations du professeur à créer
     * @return le professeur créé
     */
    @PostMapping
    public ProfesseurResponseDto createProfesseur(@RequestBody ProfesseurRequestDto professeurRequest) {
        return professeurService.createProfesseur(professeurRequest);
    }

    /**
     * Récupère un professeur à partir de son identifiant.
     *
     * @param id L'identifiant du professeur à récupérer.
     * @return ProfesseurResponseDto Les détails du professeur récupéré.
     */
    @GetMapping("/{id}")
    public ProfesseurResponseDto getProfesseurById(@PathVariable Long id) {
        return professeurService.getProfesseurById(id);
    }

    /**
     * Récupère la liste de tous les professeurs.
     *
     * @return List<ProfesseurResponseDto> La liste des professeurs récupérés.
     */
    @GetMapping
    public List<ProfesseurResponseDto> getAllProfesseurs() {
        return professeurService.getAllProfesseurs();
    }

    /**
     * Modifie un professeur existant.
     *
     * @param id L'identifiant du professeur à modifier.
     * @param professeurRequest Les données de la requête pour modifier le professeur.
     * @return ProfesseurResponseDto Les détails du professeur modifié.
     */
    @PutMapping("/{id}")
    public ProfesseurResponseDto updateProfesseurById(@PathVariable Long id, @RequestBody ProfesseurRequestDto professeurRequest) {
        return professeurService.updateProfesseurById(id, professeurRequest);
    }

    /**
     * Supprime un professeur à partir de son identifiant.
     *
     * @param id L'identifiant du professeur à supprimer.
     * @return ProfesseurResponseDto Les détails du professeur supprimé.
     */
    @DeleteMapping("/{id}")
    public ProfesseurResponseDto deleteProfesseurById(@PathVariable Long id) {
        return professeurService.deleteProfesseurById(id);
    }
}