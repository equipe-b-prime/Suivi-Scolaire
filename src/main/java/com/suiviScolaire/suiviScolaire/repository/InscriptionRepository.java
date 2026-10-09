package com.suiviScolaire.suiviScolaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.suiviScolaire.suiviScolaire.entity.Inscription;

/**
 * Repository JPA pour l'accès aux données des inscriptions.
 * Fournit les opérations CRUD standard sur la table "inscription"
 * ainsi que des requêtes personnalisées (navigation + WHERE, sans JOIN).
 */
@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    /**
     * Récupère les inscriptions d'un élève.
     *
     * @param eleveId L'identifiant de l'élève.
     * @return List<Inscription> Les inscriptions de l'élève.
     */
    List<Inscription> findByEleveId(Long eleveId);

    /**
     * Récupère la classe de chaque élève pour une liste d'élèves (batch).
     *
     * @param eleveIds Les identifiants des élèves.
     * @return List<Object[]> Chaque entrée : [eleveId, classe].
     */
    @Query("SELECT i.eleve.id, i.classe FROM Inscription i WHERE i.eleve.id IN :eleveIds ORDER BY i.annee.id DESC")
    List<Object[]> findClasseByEleveIds(@Param("eleveIds") List<Long> eleveIds);

    /**
     * Récupère l'élève de chaque inscription pour une liste d'inscriptions (batch).
     *
     * @param inscriptionIds Les identifiants des inscriptions.
     * @return List<Object[]> Chaque entrée : [inscriptionId, eleve].
     */
    @Query("SELECT i.id, i.eleve FROM Inscription i WHERE i.id IN :inscriptionIds")
    List<Object[]> findEleveByInscriptionIds(@Param("inscriptionIds") List<Long> inscriptionIds);
}