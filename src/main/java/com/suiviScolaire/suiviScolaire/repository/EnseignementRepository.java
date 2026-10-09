package com.suiviScolaire.suiviScolaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.suiviScolaire.suiviScolaire.entity.Enseignement;

/**
 * Repository JPA pour l'accès aux données des enseignements.
 * Fournit les opérations CRUD standard sur la table "enseignement"
 * ainsi que des requêtes personnalisées (navigation + WHERE, sans JOIN).
 */
@Repository
public interface EnseignementRepository extends JpaRepository<Enseignement, Long> {

    /**
     * Récupère les matières de chaque professeur pour une liste de professeurs (batch).
     *
     * @param professeurIds Les identifiants des professeurs.
     * @return List<Object[]> Chaque entrée : [professeurId, matiere].
     */
    @Query("SELECT e.professeur.id, e.matiere FROM Enseignement e WHERE e.professeur.id IN :professeurIds")
    List<Object[]> findMatiereByProfesseurIds(@Param("professeurIds") List<Long> professeurIds);

    /**
     * Récupère la matière de chaque enseignement pour une liste d'enseignements (batch).
     *
     * @param enseignementIds Les identifiants des enseignements.
     * @return List<Object[]> Chaque entrée : [enseignementId, matiere].
     */
    @Query("SELECT e.id, e.matiere FROM Enseignement e WHERE e.id IN :enseignementIds")
    List<Object[]> findMatiereByEnseignementIds(@Param("enseignementIds") List<Long> enseignementIds);
}