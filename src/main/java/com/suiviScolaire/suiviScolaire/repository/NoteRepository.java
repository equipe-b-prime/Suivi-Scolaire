package com.suiviScolaire.suiviScolaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.suiviScolaire.suiviScolaire.entity.Note;

/**
 * Repository JPA pour l'accès aux données des notes.
 * Fournit les opérations CRUD standard sur la table "note".
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
}