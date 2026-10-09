package com.suiviScolaire.suiviScolaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.suiviScolaire.suiviScolaire.entity.Classe;
import com.suiviScolaire.suiviScolaire.entity.Inscription;

/**
 * Repository JPA pour l'accès aux données des classes.
 * Fournit les opérations CRUD standard sur la table "classe"
 * ainsi que des requêtes personnalisées (navigation + WHERE, sans JOIN).
 */
@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    /**
     * Compte le nombre d'élèves inscrits dans une classe (effectif réel).
     *
     * @param classeId L'identifiant de la classe.
     * @return long Le nombre d'élèves inscrits.
     */
    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.classe.id = :classeId")
    long countElevesByClasse(@Param("classeId") Long classeId);

    /**
     * Compte les élèves par classe pour une liste d'identifiants (batch).
     *
     * @param classeIds Les identifiants des classes.
     * @return List<Object[]> Chaque entrée : [classeId, effectif].
     */
    @Query("SELECT i.classe.id, COUNT(i) FROM Inscription i WHERE i.classe.id IN :classeIds GROUP BY i.classe.id")
    List<Object[]> countElevesByClasseIds(@Param("classeIds") List<Long> classeIds);
}