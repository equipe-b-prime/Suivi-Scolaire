package com.suiviScolaire.suiviScolaire.repository;

import com.suiviScolaire.suiviScolaire.entity.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository JPA de l'entité Classe.
 * Fournit les opérations CRUD ainsi que les requêtes de jointure sur les inscriptions.
 */
@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    /**
     * Compte les élèves inscrits dans une classe (via la table des inscriptions).
     *
     * @param classeId L'identifiant de la classe.
     * @return Le nombre d'élèves inscrits.
     */
    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.classe.id = :classeId AND i.dateFin IS NULL")
    long countElevesByClasse(@Param("classeId") Long classeId);

    /**
     * Compte les élèves inscrits dans plusieurs classes.
     *
     * @param classeIds Les identifiants des classes.
     * @return Une liste de paires (identifiant de classe, nombre d'élèves).
     */
    @Query("SELECT i.classe.id, COUNT(i) FROM Inscription i WHERE i.classe.id IN :classeIds AND i.dateFin IS NULL GROUP BY i.classe.id")
    List<Object[]> countElevesByClasseIds(@Param("classeIds") List<Long> classeIds);

    /**
     * Recherche les classes portant un nom exact.
     *
     * @param nom Le nom recherché.
     * @return La liste des classes homonymes.
     */
    @Query("SELECT e FROM Classe e WHERE e.nom = :nom")
    List<Classe> findByNom(@Param("nom") String nom);

    /**
     * Vérifie si une classe de même nom existe déjà dans le même niveau.
     *
     * @param nom Le nom de la classe.
     * @param niveau Le niveau de la classe.
     * @return true si une classe identique existe déjà.
     */
    boolean existsByNomAndNiveau(String nom, String niveau);

    /**
     * Vérifie si une autre classe (identifiant différent) porte le même nom dans le même niveau.
     *
     * @param nom Le nom de la classe.
     * @param niveau Le niveau de la classe.
     * @param id L'identifiant de la classe à exclure (celle en cours de modification).
     * @return true si une autre classe identique existe déjà.
     */
    boolean existsByNomAndNiveauAndIdNot(String nom, String niveau, Long id);
}
