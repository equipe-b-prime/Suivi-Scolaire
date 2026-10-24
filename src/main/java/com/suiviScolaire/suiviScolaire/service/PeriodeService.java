package com.suiviScolaire.suiviScolaire.service;

import org.springframework.stereotype.Service;

import com.suiviScolaire.suiviScolaire.entity.Periode;
import com.suiviScolaire.suiviScolaire.repository.PeriodeRepository;
import com.suiviScolaire.suiviScolaire.dto.PeriodeResponseDto;

/**
 * Service métier gérant les périodes.
 * Fournit la conversion entité/DTO des périodes.
 */
@Service
public class PeriodeService {

    private final PeriodeRepository periodeRepository;

    /**
     * Constructeur injectant le repository des périodes.
     *
     * @param periodeRepository le repository des périodes
     */
    public PeriodeService(PeriodeRepository periodeRepository) {
        this.periodeRepository = periodeRepository;
    }

    /**
     * Convertit une entité période en objet de réponse.
     *
     * @param periode L'entité période à convertir.
     * @return PeriodeResponseDto La réponse de la période.
     */
    public PeriodeResponseDto toDto(Periode periode) {
        return PeriodeResponseDto.builder()
                .id(periode.getId())
                .nom(periode.getNom())
                .build();
    }
}