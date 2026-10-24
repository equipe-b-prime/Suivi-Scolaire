package com.suiviScolaire.suiviScolaire.repository;

import com.suiviScolaire.suiviScolaire.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository JPA de l'entité Note.
 * Fournit les opérations CRUD ainsi que les requêtes de jointure sur l'élève et la matière.
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    /**
     * Compte les notes d'un élève (via son inscription).
     *
     * @param eleveId L'identifiant de l'élève.
     * @return Le nombre de notes de l'élève.
     */
    @Query("SELECT COUNT(n) FROM Note n WHERE n.inscription.eleve.id = :eleveId")
    long countNotesByEleve(@Param("eleveId") Long eleveId);

    /**
     * Compte les notes d'une matière (via l'enseignement).
     *
     * @param matiereId L'identifiant de la matière.
     * @return Le nombre de notes de la matière.
     */
    @Query("SELECT COUNT(n) FROM Note n WHERE n.enseignement.matiere.id = :matiereId")
    long countNotesByMatiere(@Param("matiereId") Long matiereId);

    /**
     * Récupère les notes d'un élève, triées par matière puis par identifiant.
     *
     * @param eleveId L'identifiant de l'élève.
     * @return La liste des notes de l'élève.
     */
    @Query("SELECT n FROM Note n WHERE n.inscription.eleve.id = :eleveId ORDER BY n.enseignement.matiere.nom, n.id")
    List<Note> findNotesByEleve(@Param("eleveId") Long eleveId);

    /**
     * Récupère les notes d'un élève pour un trimestre donné.
     *
     * @param eleveId L'identifiant de l'élève.
     * @param trimestre Le numéro du trimestre (1, 2 ou 3).
     * @return La liste des notes de l'élève pour ce trimestre.
     */
    @Query("SELECT n FROM Note n WHERE n.inscription.eleve.id = :eleveId AND n.periode.numero = :trimestre ORDER BY n.enseignement.matiere.nom, n.id")
    List<Note> findNotesByEleveAndTrimestre(@Param("eleveId") Long eleveId, @Param("trimestre") Integer trimestre);

    /**
     * Vérifie si une matière est enseignée dans une classe, c'est-à-dire s'il existe
     * au moins une note de cette matière pour un élève de la classe (jointure
     * note -> inscription -> classe et note -> enseignement -> matière).
     *
     * @param classeId L'identifiant de la classe.
     * @param matiereId L'identifiant de la matière.
     * @return true si la matière est enseignée dans cette classe.
     */
    @Query("SELECT COUNT(n) FROM Note n WHERE n.inscription.classe.id = :classeId AND n.enseignement.matiere.id = :matiereId")
    long countNotesByClasseAndMatiere(@Param("classeId") Long classeId, @Param("matiereId") Long matiereId);

    /**
     * Récupère les notes d'une classe dans une matière pour un trimestre donné,
     * triées par nom puis prénom de l'élève.
     *
     * @param classeId L'identifiant de la classe.
     * @param matiereId L'identifiant de la matière.
     * @param trimestre Le numéro du trimestre (1, 2 ou 3).
     * @return La liste des notes correspondantes.
     */
    @Query("SELECT n FROM Note n WHERE n.inscription.classe.id = :classeId AND n.enseignement.matiere.id = :matiereId AND n.periode.numero = :trimestre ORDER BY n.inscription.eleve.nom, n.inscription.eleve.prenom, n.id")
    List<Note> findNotesByClasseAndMatiereAndTrimestre(@Param("classeId") Long classeId,
                                                       @Param("matiereId") Long matiereId,
                                                       @Param("trimestre") Integer trimestre);

    /**
     * Compte le nombre de notes d'une classe dans une matière pour un trimestre donné.
     *
     * @param classeId L'identifiant de la classe.
     * @param matiereId L'identifiant de la matière.
     * @param trimestre Le numéro du trimestre (1, 2 ou 3).
     * @return Le nombre de notes.
     */
    @Query("SELECT COUNT(n) FROM Note n WHERE n.inscription.classe.id = :classeId AND n.enseignement.matiere.id = :matiereId AND n.periode.numero = :trimestre")
    long countNotesByClasseAndMatiereAndTrimestre(@Param("classeId") Long classeId,
                                                   @Param("matiereId") Long matiereId,
                                                   @Param("trimestre") Integer trimestre);
}
