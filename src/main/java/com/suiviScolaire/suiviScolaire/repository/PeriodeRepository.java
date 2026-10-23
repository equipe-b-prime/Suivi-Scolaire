package com.suiviScolaire.suiviScolaire.repository;

import com.suiviScolaire.suiviScolaire.entity.Periode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA de l'entité Periode.
 */
@Repository
public interface PeriodeRepository extends JpaRepository<Periode, Long> {

    /**
     * Vérifie si au moins une période (trimestre) est clôturée.
     *
     * @return true si un trimestre est clôturé.
     */
    boolean existsByClotureTrue();
}
