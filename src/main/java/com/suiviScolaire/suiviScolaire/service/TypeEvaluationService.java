package com.suiviScolaire.suiviScolaire.service;

import org.springframework.stereotype.Service;

import com.suiviScolaire.suiviScolaire.entity.TypeEvaluation;
import com.suiviScolaire.suiviScolaire.repository.TypeEvaluationRepository;
import com.suiviScolaire.suiviScolaire.dto.TypeEvaluationResponseDto;

/**
 * Service métier gérant les types d'évaluation.
 * Fournit la conversion entité/DTO des types d'évaluation.
 */
@Service
public class TypeEvaluationService {

    private final TypeEvaluationRepository typeEvaluationRepository;

    /**
     * Constructeur injectant le repository des types d'évaluation.
     *
     * @param typeEvaluationRepository le repository des types d'évaluation
     */
    public TypeEvaluationService(TypeEvaluationRepository typeEvaluationRepository) {
        this.typeEvaluationRepository = typeEvaluationRepository;
    }

    /**
     * Convertit une entité type d'évaluation en objet de réponse.
     *
     * @param typeEvaluation L'entité type d'évaluation à convertir.
     * @return TypeEvaluationResponseDto La réponse du type d'évaluation.
     */
    public TypeEvaluationResponseDto toDto(TypeEvaluation typeEvaluation) {
        return TypeEvaluationResponseDto.builder()
                .id(typeEvaluation.getId())
                .poid(typeEvaluation.getPoid())
                .nom(typeEvaluation.getNom())
                .build();
    }
}
