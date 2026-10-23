package com.suiviScolaire.suiviScolaire.repository;

import com.suiviScolaire.suiviScolaire.entity.Matiere;
import com.suiviScolaire.suiviScolaire.entity.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository JPA de l'entité Professeur.
 * Fournit les opérations CRUD ainsi que les requêtes de jointure sur les enseignements.
 */
@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {

    /**
     * Récupère les matières enseignées par un professeur.
     *
     * @param professeurId L'identifiant du professeur.
     * @return La liste des matières enseignées.
     */
    @Query("SELECT e.matiere FROM Enseignement e WHERE e.professeur.id = :professeurId")
    List<Matiere> findMatieresByProfesseur(@Param("professeurId") Long professeurId);

    /**
     * Recherche les professeurs portant un nom exact.
     *
     * @param nom Le nom recherché.
     * @return La liste des professeurs homonymes.
     */
    @Query("SELECT e FROM Professeur e WHERE e.nom = :nom")
    List<Professeur> findByNom(@Param("nom") String nom);

    /**
     * Vérifie si un professeur porte déjà cet email.
     *
     * @param email L'email recherché.
     * @return true si un professeur porte déjà cet email.
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un autre professeur (identifiant différent) porte cet email.
     *
     * @param email L'email recherché.
     * @param id L'identifiant du professeur à exclure (celui en cours de modification).
     * @return true si un autre professeur porte déjà cet email.
     */
    boolean existsByEmailAndIdNot(String email, Long id);
}
