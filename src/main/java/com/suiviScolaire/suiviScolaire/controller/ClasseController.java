package com.suiviScolaire.suiviScolaire.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.suiviScolaire.suiviScolaire.dto.ClasseRequestDto;
import com.suiviScolaire.suiviScolaire.dto.ClasseResponseDto;
import com.suiviScolaire.suiviScolaire.service.ClasseService;

/**
 * Contrôleur REST exposant les endpoints de gestion des classes.
 */
@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
public class ClasseController {
    private final ClasseService classeService;

    /**
     * Crée une nouvelle classe.
     *
     * @param classeRequest les informations de la classe à créer
     * @return la classe créée
     */
    @PostMapping
    public ClasseResponseDto createClasse(@RequestBody ClasseRequestDto classeRequest) {
        return classeService.createClasse(classeRequest);
    }

    /**
     * Récupère une classe à partir de son identifiant.
     *
     * @param id L'identifiant de la classe à récupérer.
     * @return ClasseResponseDto Les détails de la classe récupérée.
     */
    @GetMapping("/{id}")
    public ClasseResponseDto getClasseById(@PathVariable Long id) {
        return classeService.getClasseById(id);
    }

    /**
     * Récupère la liste de toutes les classes.
     *
     * @return List<ClasseResponseDto> La liste des classes récupérées.
     */
    @GetMapping
    public List<ClasseResponseDto> getAllClasses() {
        return classeService.getAllClasses();
    }

    /**
     * Modifie une classe existante.
     *
     * @param id L'identifiant de la classe à modifier.
     * @param classeRequest Les données de la requête pour modifier la classe.
     * @return ClasseResponseDto Les détails de la classe modifiée.
     */
    @PutMapping("/{id}")
    public ClasseResponseDto updateClasseById(@PathVariable Long id, @RequestBody ClasseRequestDto classeRequest) {
        return classeService.updateClasseById(id, classeRequest);
    }

    /**
     * Supprime une classe à partir de son identifiant.
     *
     * @param id L'identifiant de la classe à supprimer.
     * @return ClasseResponseDto Les détails de la classe supprimée.
     */
    @DeleteMapping("/{id}")
    public ClasseResponseDto deleteClasseById(@PathVariable Long id) {
        return classeService.deleteClasseById(id);
    }
}