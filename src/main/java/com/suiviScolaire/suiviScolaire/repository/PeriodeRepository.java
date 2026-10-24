package com.suiviScolaire.suiviScolaire.repository;

import com.suiviScolaire.suiviScolaire.entity.Periode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    /**
     * Trouve une période par son numéro (trimestre 1, 2 ou 3).
     *
     * @param numero Le numéro du trimestre.
     * @return Optional contenant la période trouvée.
     */
    Optional<Periode> findByNumero(Integer numero);

    /**
     * Trouve la période la plus récente (par numéro décroissant).
     *
     * @return Optional contenant la période la plus récente.
     */
    Optional<Periode> findTopByOrderByNumeroDesc();
}
