package com.suiviScolaire.suiviScolaire.service;

import com.suiviScolaire.suiviScolaire.exception.PoidsInvalideException;
import com.suiviScolaire.suiviScolaire.exception.TrimestreClotureException;
import org.springframework.stereotype.Service;

import com.suiviScolaire.suiviScolaire.dto.TypeEvaluationResponseDto;
import com.suiviScolaire.suiviScolaire.exception.NotFoundException;
import com.suiviScolaire.suiviScolaire.repository.PeriodeRepository;
import com.suiviScolaire.suiviScolaire.repository.TypeEvaluationRepository;
import com.suiviScolaire.suiviScolaire.dto.TypeEvaluationResponseDto;
import com.suiviScolaire.suiviScolaire.entity.TypeEvaluation;
import java.math.BigDecimal;

/**
 * Service métier gérant les types d'évaluation.
 * Fournit la conversion entité/DTO des types d'évaluation.
 */
@Service
public class TypeEvaluationService {

    private final TypeEvaluationRepository typeEvaluationRepository;
    private final PeriodeRepository periodeRepository;

    /**
     * Constructeur injectant le repository des types d'évaluation et des périodes.
     *
     * @param typeEvaluationRepository le repository des types d'évaluation
     * @param periodeRepository le repository des périodes (trimestres)
     */
    public TypeEvaluationService(TypeEvaluationRepository typeEvaluationRepository,
                                 PeriodeRepository periodeRepository) {
        this.typeEvaluationRepository = typeEvaluationRepository;
        this.periodeRepository = periodeRepository;
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
                .code(typeEvaluation.getCode())
                .poid(typeEvaluation.getPoid())
                .nom(typeEvaluation.getNom())
                .build();
    }

    /**
     * Modifie le poids d'un type d'évaluation identifié par son code.
     * Règle : la modification est refusée si un trimestre est déjà clôturé.
     *
     * @param code Le code du type d'évaluation.
     * @param poids Le nouveau poids (doit être strictement positif).
     * @return TypeEvaluationResponseDto Le type d'évaluation mis à jour.
     * @throws NotFoundException si le type d'évaluation n'existe pas.
     * @throws PoidsInvalideException si le poids n'est pas positif.
     * @throws TrimestreClotureException si un trimestre est clôturé.
     */
    public TypeEvaluationResponseDto modifierPoids(String code, BigDecimal poids) {
        TypeEvaluation typeEvaluation = typeEvaluationRepository.findByCode(code);
        if (typeEvaluation == null) {
            throw new NotFoundException("Type d'évaluation non trouvé");
        }
        // Vérifier si un trimestre est clôturé
        if (periodeRepository.existsByClotureTrue()) {
            throw new TrimestreClotureException("Impossible de modifier le poids : un trimestre est déjà clôturé.");
        }
        if (poids == null || poids.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PoidsInvalideException("Le poids doit être strictement positif");
        }
        typeEvaluation.setPoid(poids);
        typeEvaluationRepository.save(typeEvaluation);
        // TODO : Recalculer les moyennes par matière avec les nouveaux poids
        return toDto(typeEvaluation);
    }
}
