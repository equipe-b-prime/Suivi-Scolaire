package com.suiviScolaire.suiviScolaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.suiviScolaire.suiviScolaire.entity.Classe;
import com.suiviScolaire.suiviScolaire.entity.Eleve;
import com.suiviScolaire.suiviScolaire.entity.Inscription;

/**
 * Repository JPA pour l'accès aux données des élèves.
 * Fournit les opérations CRUD standard sur la table "eleve"
 * ainsi que des requêtes personnalisées (navigation + WHERE, sans JOIN).
 */
@Repository
public interface EleveRepository extends JpaRepository<Eleve, Long> {

    /**
     * Récupère les classes d'un élève via ses inscriptions,
     * de la plus récente à la plus ancienne (par année).
     *
     * @param eleveId L'identifiant de l'élève.
     * @return List<Classe> Les classes de l'élève.
     */
    @Query("SELECT i.classe FROM Inscription i WHERE i.eleve.id = :eleveId ORDER BY i.annee.id DESC")
    List<Classe> findClassesByEleve(@Param("eleveId") Long eleveId);

    /**
     * Récupère les inscriptions d'un élève.
     *
     * @param eleveId L'identifiant de l'élève.
     * @return List<Inscription> Les inscriptions de l'élève.
     */
    @Query("SELECT i FROM Inscription i WHERE i.eleve.id = :eleveId")
    List<Inscription> findInscriptionsByEleve(@Param("eleveId") Long eleveId);
}