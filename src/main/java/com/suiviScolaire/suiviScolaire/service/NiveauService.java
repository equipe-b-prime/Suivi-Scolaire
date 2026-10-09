package com.suiviScolaire.suiviScolaire.service;

import org.springframework.stereotype.Service;

import com.suiviScolaire.suiviScolaire.entity.Niveau;
import com.suiviScolaire.suiviScolaire.dto.NiveauResponseDto;

/**
 * Service métier gérant les niveaux.
 * Fournit la conversion entité/DTO des niveaux.
 */
@Service
public class NiveauService {

    /**
     * Convertit une entité niveau en objet de réponse.
     *
     * @param niveau L'entité niveau à convertir.
     * @return NiveauResponseDto La réponse du niveau.
     */
    public NiveauResponseDto toDto(Niveau niveau) {
        return NiveauResponseDto.builder()
                .id(niveau.getId())
                .nom(niveau.getNom())
                .build();
    }
}
