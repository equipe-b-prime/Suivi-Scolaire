package com.suiviScolaire.suiviScolaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.suiviScolaire.suiviScolaire.entity.Coefficient;
import com.suiviScolaire.suiviScolaire.entity.Matiere;

/**
 * Repository JPA pour l'accès aux données des matières.
 * Fournit les opérations CRUD standard sur la table "matiere"
 * ainsi que des requêtes personnalisées avec jointures.
 */
@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {

    /**
     * Récupère les coefficients d'une matière.
     *
     * @param matiereId L'identifiant de la matière.
     * @return List<Coefficient> Les coefficients associés.
     */
    @Query("SELECT c FROM Coefficient c WHERE c.matiere.id = :matiereId")
    List<Coefficient> findCoefficientsByMatiere(@Param("matiereId") Long matiereId);
}