package com.suiviScolaire.suiviScolaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.suiviScolaire.suiviScolaire.entity.Periode;

/**
 * Repository JPA pour l'accès aux données des périodes.
 * Fournit les opérations CRUD standard sur la table "periode".
 */
@Repository
public interface PeriodeRepository extends JpaRepository<Periode, Long> {
}