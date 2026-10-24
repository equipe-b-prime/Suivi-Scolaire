package com.suiviScolaire.suiviScolaire.controller;

import com.suiviScolaire.suiviScolaire.dto.ResponseDto;
import com.suiviScolaire.suiviScolaire.service.TypeEvaluationService;
import com.suiviScolaire.suiviScolaire.utils.response.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Contrôleur REST pour la gestion des types d'évaluation.
 */
@RestController
@RequestMapping("/types-evaluation")
public class TypeEvaluationController {

    private final TypeEvaluationService typeEvaluationService;

    /**
     * Constructeur injectant le service des types d'évaluation.
     *
     * @param typeEvaluationService le service des types d'évaluation
     */
    public TypeEvaluationController(TypeEvaluationService typeEvaluationService) {
        this.typeEvaluationService = typeEvaluationService;
    }

    /**
     * Modifie le poids d'un type d'évaluation.
     *
     * @param code Le code du type d'évaluation (ex: "devoir", "interrogation", "oral").
     * @param poids Le nouveau poids (doit être strictement positif).
     * @return Response<ResponseDto> Le type d'évaluation mis à jour.
     * @throws NotFoundException si le type d'évaluation n'existe pas.
     * @throws IllegalArgumentException si le poids n'est pas positif.
     */
    @PutMapping("/{code}/poids")
    public Response<ResponseDto> modifierPoids(@PathVariable String code,
                                              @RequestParam BigDecimal poids) {
        return new Response<>(typeEvaluationService.modifierPoids(code, poids));
    }
}