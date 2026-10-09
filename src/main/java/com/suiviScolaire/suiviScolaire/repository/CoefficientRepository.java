package com.suiviScolaire.suiviScolaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.suiviScolaire.suiviScolaire.entity.Coefficient;

/**
 * Repository JPA pour l'accès aux données des coefficients.
 * Fournit les opérations CRUD standard sur la table "coefficient"
 * ainsi que des requêtes personnalisées (navigation + WHERE, sans JOIN).
 */
@Repository
public interface CoefficientRepository extends JpaRepository<Coefficient, Long> {

    /**
     * Récupère les coefficients de chaque matière pour une liste de matières (batch).
     * Le niveau est chargé dans la même requête pour éviter le N+1.
     *
     * @param matiereIds Les identifiants des matières.
     * @return List<Object[]> Chaque entrée : [matiereId, coefficient, niveau].
     */
    @Query("SELECT c.matiere.id, c, c.niveau FROM Coefficient c WHERE c.matiere.id IN :matiereIds")
    List<Object[]> findCoefficientsByMatiereIds(@Param("matiereIds") List<Long> matiereIds);
}