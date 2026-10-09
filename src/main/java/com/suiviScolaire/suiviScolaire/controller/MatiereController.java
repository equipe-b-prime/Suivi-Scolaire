package com.suiviScolaire.suiviScolaire.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.suiviScolaire.suiviScolaire.dto.MatiereRequestDto;
import com.suiviScolaire.suiviScolaire.dto.MatiereResponseDto;
import com.suiviScolaire.suiviScolaire.service.MatiereService;

/**
 * Contrôleur REST exposant les endpoints de gestion des matières.
 */
@RestController
@RequestMapping("/matieres")
@RequiredArgsConstructor
public class MatiereController {
    private final MatiereService matiereService;

    /**
     * Crée une nouvelle matière.
     *
     * @param matiereRequest les informations de la matière à créer
     * @return la matière créée
     */
    @PostMapping
    public MatiereResponseDto createMatiere(@RequestBody MatiereRequestDto matiereRequest) {
        return matiereService.createMatiere(matiereRequest);
    }

    /**
     * Récupère une matière à partir de son identifiant.
     *
     * @param id L'identifiant de la matière à récupérer.
     * @return MatiereResponseDto Les détails de la matière récupérée.
     */
    @GetMapping("/{id}")
    public MatiereResponseDto getMatiereById(@PathVariable Long id) {
        return matiereService.getMatiereById(id);
    }

    /**
     * Récupère la liste de toutes les matières.
     *
     * @return List<MatiereResponseDto> La liste des matières récupérées.
     */
    @GetMapping
    public List<MatiereResponseDto> getAllMatieres() {
        return matiereService.getAllMatieres();
    }

    /**
     * Modifie une matière existante.
     *
     * @param id L'identifiant de la matière à modifier.
     * @param matiereRequest Les données de la requête pour modifier la matière.
     * @return MatiereResponseDto Les détails de la matière modifiée.
     */
    @PutMapping("/{id}")
    public MatiereResponseDto updateMatiereById(@PathVariable Long id, @RequestBody MatiereRequestDto matiereRequest) {
        return matiereService.updateMatiereById(id, matiereRequest);
    }

    /**
     * Supprime une matière à partir de son identifiant.
     *
     * @param id L'identifiant de la matière à supprimer.
     * @return MatiereResponseDto Les détails de la matière supprimée.
     */
    @DeleteMapping("/{id}")
    public MatiereResponseDto deleteMatiereById(@PathVariable Long id) {
        return matiereService.deleteMatiereById(id);
    }
}