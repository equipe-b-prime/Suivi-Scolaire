package com.suiviScolaire.suiviScolaire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.suiviScolaire.suiviScolaire.entity.Annee;
import com.suiviScolaire.suiviScolaire.repository.AnneeRepository;
import com.suiviScolaire.suiviScolaire.dto.AnneeResponseDto;

/**
 * Service métier gérant les années scolaires.
 * Fournit la conversion entité/DTO des années.
 */
@Service
@RequiredArgsConstructor
public class AnneeService {

    private final AnneeRepository anneeRepository;

    /**
     * Convertit une entité année en objet de réponse.
     *
     * @param annee L'entité année à convertir.
     * @return AnneeResponseDto La réponse de l'année.
     */
    public AnneeResponseDto toDto(Annee annee) {
        return AnneeResponseDto.builder()
                .id(annee.getId())
                .libelle(annee.getLibelle())
                .build();
    }
}
