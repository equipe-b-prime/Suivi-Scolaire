package com.suiviScolaire.suiviScolaire.service;

import org.springframework.stereotype.Service;

import com.suiviScolaire.suiviScolaire.entity.Coefficient;
import com.suiviScolaire.suiviScolaire.repository.CoefficientRepository;
import com.suiviScolaire.suiviScolaire.dto.CoefficientResponseDto;

/**
 * Service métier gérant les coefficients.
 * Fournit la conversion entité/DTO des coefficients (avec leur niveau).
 */
@Service
public class CoefficientService {

    private final CoefficientRepository coefficientRepository;
    private final NiveauService niveauService;

    /**
     * Constructeur injectant le repository des coefficients et le service des niveaux.
     *
     * @param coefficientRepository le repository des coefficients
     * @param niveauService le service des niveaux (conversion)
     */
    public CoefficientService(CoefficientRepository coefficientRepository, NiveauService niveauService) {
        this.coefficientRepository = coefficientRepository;
        this.niveauService = niveauService;
    }

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
