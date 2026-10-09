package com.suiviScolaire.suiviScolaire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.suiviScolaire.suiviScolaire.entity.Coefficient;
import com.suiviScolaire.suiviScolaire.repository.CoefficientRepository;
import com.suiviScolaire.suiviScolaire.dto.CoefficientResponseDto;

/**
 * Service métier gérant les coefficients.
 * Fournit la conversion entité/DTO des coefficients (avec leur niveau).
 */
@Service
@RequiredArgsConstructor
public class CoefficientService {

    private final CoefficientRepository coefficientRepository;
    private final NiveauService niveauService;

    /**
     * Convertit une entité coefficient en objet de réponse, avec son niveau.
     *
     * @param coefficient L'entité coefficient à convertir.
     * @return CoefficientResponseDto La réponse du coefficient.
     */
    public CoefficientResponseDto toDto(Coefficient coefficient) {
        return CoefficientResponseDto.builder()
                .id(coefficient.getId())
                .valeur(coefficient.getValeur())
                .niveau(niveauService.toDto(coefficient.getNiveau()))
                .build();
    }
}
