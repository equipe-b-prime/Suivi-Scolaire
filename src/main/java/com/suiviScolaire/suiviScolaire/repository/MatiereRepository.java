package com.suiviScolaire.suiviScolaire.repository;

import com.suiviScolaire.suiviScolaire.entity.Coefficient;
import com.suiviScolaire.suiviScolaire.entity.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository JPA de l'entité Matiere.
 * Fournit les opérations CRUD ainsi que les requêtes de jointure sur les coefficients.
 */
@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {

    /**
     * Récupère les coefficients d'une matière.
     *
     * @param matiereId L'identifiant de la matière.
     * @return La liste des coefficients de la matière.
     */
    @Query("SELECT c FROM Coefficient c WHERE c.matiere.id = :matiereId")
    List<Coefficient> findCoefficientsByMatiere(@Param("matiereId") Long matiereId);

    /**
     * Recherche les matières portant un nom exact.
     *
     * @param nom Le nom recherché.
     * @return La liste des matières homonymes.
     */
    @Query("SELECT e FROM Matiere e WHERE e.nom = :nom")
    List<Matiere> findByNom(@Param("nom") String nom);

    /**
     * Vérifie si une matière porte déjà ce libellé.
     *
     * @param nom Le libellé recherché.
     * @return true si une matière porte déjà ce libellé.
     */
    boolean existsByNom(String nom);
}
