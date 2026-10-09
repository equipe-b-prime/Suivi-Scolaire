package com.suiviScolaire.suiviScolaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.suiviScolaire.suiviScolaire.entity.TypeEvaluation;

/**
 * Repository JPA pour l'accès aux données des types d'évaluation.
 * Fournit les opérations CRUD standard sur la table "type_evaluation".
 */
@Repository
public interface TypeEvaluationRepository extends JpaRepository<TypeEvaluation, Long> {
}