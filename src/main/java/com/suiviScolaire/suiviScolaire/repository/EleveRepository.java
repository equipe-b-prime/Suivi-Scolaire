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

    boolean existsByMatricule(String matricule);

    boolean existsByMatriculeAndIdNot(String matricule, Long id);

    /**
     * Récupère les classes d'un élève via ses inscriptions,
     * de la plus récente à la plus ancienne (par année).
     *
     * @param eleveId L'identifiant de l'élève.
     * @return List<Classe> Les classes de l'élève.
     */
    @Query("SELECT i.classe FROM Inscription i WHERE i.eleve.id = :eleveId ORDER BY CASE WHEN i.dateFin IS NULL THEN 0 ELSE 1 END, i.id DESC")
    List<Classe> findClassesByEleve(@Param("eleveId") Long eleveId);

    /**
     * Récupère les inscriptions d'un élève.
     *
     * @param eleveId L'identifiant de l'élève.
     * @return List<Inscription> Les inscriptions de l'élève.
     */
    @Query("SELECT i FROM Inscription i WHERE i.eleve.id = :eleveId")
    List<Inscription> findInscriptionsByEleve(@Param("eleveId") Long eleveId);

    /**
     * Recherche les eleves portant exactement ce nom.
     * Retourne une liste car les noms ne sont pas contraints uniques.
     *
     * @param nom Le nom recherché.
     * @return List<Eleve> Les eleves portant ce nom.
     */
    @Query("SELECT e FROM Eleve e WHERE e.nom = :nom")
    List<Eleve> findByNom(@Param("nom") String nom);

    /**
     * Recherche des élèves dont le nom contient le texte fourni (insensible à la casse),
     * triés par nom puis prénom.
     *
     * @param nom Le fragment de nom recherché.
     * @return La liste des élèves correspondants.
     */
    @Query("SELECT e FROM Eleve e WHERE LOWER(e.nom) LIKE LOWER(CONCAT('%', :nom, '%')) ORDER BY e.nom, e.prenom")
    List<Eleve> findByNomContainingIgnoreCase(@Param("nom") String nom);
}
