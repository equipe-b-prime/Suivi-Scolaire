package com.suiviScolaire.suiviScolaire.repository;

import com.suiviScolaire.suiviScolaire.entity.Enseignement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository JPA de l'entité Enseignement.
 * Fournit les opérations CRUD ainsi que les requêtes de jointure sur les professeurs et les matières.
 */
@Repository
public interface EnseignementRepository extends JpaRepository<Enseignement, Long> {

    /**
     * Récupère les matières associées à plusieurs professeurs.
     *
     * @param professeurIds Les identifiants des professeurs.
     * @return Une liste de paires (identifiant de professeur, matière).
     */
    @Query("SELECT e.professeur.id, e.matiere FROM Enseignement e WHERE e.professeur.id IN :professeurIds")
    List<Object[]> findMatiereByProfesseurIds(@Param("professeurIds") List<Long> professeurIds);

    /**
     * Récupère les matières associées à plusieurs enseignements.
     *
     * @param enseignementIds Les identifiants des enseignements.
     * @return Une liste de paires (identifiant d'enseignement, matière).
     */
    @Query("SELECT e.id, e.matiere FROM Enseignement e WHERE e.id IN :enseignementIds")
    List<Object[]> findMatiereByEnseignementIds(@Param("enseignementIds") List<Long> enseignementIds);

    /**
     * Vérifie si une matière est enseignée par au moins un professeur.
     *
     * @param matiereId L'identifiant de la matière.
     * @return true si la matière est affectée à un professeur.
     */
    boolean existsByMatiereId(Long matiereId);

    /**
     * Vérifie si un professeur est affecté à au moins un enseignement.
     *
     * @param professeurId L'identifiant du professeur.
     * @return true si le professeur est affecté à une matière.
     */
    boolean existsByProfesseurId(Long professeurId);
}
