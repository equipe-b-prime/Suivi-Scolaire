package com.suiviScolaire.suiviScolaire.repository;

import com.suiviScolaire.suiviScolaire.entity.Eleve;
import com.suiviScolaire.suiviScolaire.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository JPA de l'entité Inscription.
 * Fournit les opérations CRUD ainsi que les requêtes de jointure sur les élèves et les classes.
 */
@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    /**
     * Récupère les inscriptions d'un élève.
     *
     * @param eleveId L'identifiant de l'élève.
     * @return La liste des inscriptions de l'élève.
     */
    List<Inscription> findByEleveId(Long eleveId);

    /**
     * Récupère la classe et l'élève de plusieurs inscriptions.
     *
     * @param eleveIds Les identifiants des élèves.
     * @return Une liste de paires (identifiant d'élève, classe).
     */
    @Query("SELECT i.eleve.id, i.classe FROM Inscription i WHERE i.eleve.id IN :eleveIds ORDER BY i.annee.id DESC")
    List<Object[]> findClasseByEleveIds(@Param("eleveIds") List<Long> eleveIds);

    /**
     * Récupère l'élève de plusieurs inscriptions.
     *
     * @param inscriptionIds Les identifiants des inscriptions.
     * @return Une liste de paires (identifiant d'inscription, élève).
     */
    @Query("SELECT i.id, i.eleve FROM Inscription i WHERE i.id IN :inscriptionIds")
    List<Object[]> findEleveByInscriptionIds(@Param("inscriptionIds") List<Long> inscriptionIds);

    /**
     * Vérifie si une classe compte au moins un élève inscrit.
     *
     * @param classeId L'identifiant de la classe.
     * @return true si au moins un élève est inscrit dans cette classe.
     */
    boolean existsByClasseId(Long classeId);

    /**
     * Récupère les élèves d'une classe, triés par nom puis prénom (jointure sur l'inscription).
     *
     * @param classeId L'identifiant de la classe.
     * @return La liste des élèves de la classe.
     */
    @Query("SELECT i.eleve FROM Inscription i WHERE i.classe.id = :classeId ORDER BY i.eleve.nom, i.eleve.prenom")
    List<Eleve> findElevesByClasseId(@Param("classeId") Long classeId);

    /**
     * Récupère les inscriptions d'une classe.
     *
     * @param classeId L'identifiant de la classe.
     * @return La liste des inscriptions de la classe, triées par nom puis prénom.
     */
    @Query("SELECT i FROM Inscription i WHERE i.classe.id = :classeId ORDER BY i.eleve.nom, i.eleve.prenom")
    List<Inscription> findByClasseId(@Param("classeId") Long classeId);
}
