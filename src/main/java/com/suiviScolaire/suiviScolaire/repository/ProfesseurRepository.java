package com.suiviScolaire.suiviScolaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.suiviScolaire.suiviScolaire.entity.Enseignement;
import com.suiviScolaire.suiviScolaire.entity.Matiere;
import com.suiviScolaire.suiviScolaire.entity.Professeur;

/**
 * Repository JPA pour l'accès aux données des professeurs.
 * Fournit les opérations CRUD standard sur la table "professeur"
 * ainsi que des requêtes personnalisées avec jointures.
 */
@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {

    /**
     * Récupère les matières enseignées par un professeur via ses enseignements.
     *
     * @param professeurId L'identifiant du professeur.
     * @return List<Matiere> Les matières enseignées.
     */
    @Query("SELECT e.matiere FROM Enseignement e WHERE e.professeur.id = :professeurId")
    List<Matiere> findMatieresByProfesseur(@Param("professeurId") Long professeurId);
}