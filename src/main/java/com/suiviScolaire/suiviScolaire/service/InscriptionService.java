package com.suiviScolaire.suiviScolaire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.suiviScolaire.suiviScolaire.entity.Inscription;
import com.suiviScolaire.suiviScolaire.repository.InscriptionRepository;
import com.suiviScolaire.suiviScolaire.dto.InscriptionResponseDto;

/**
 * Service métier gérant les inscriptions.
 * Fournit la conversion entité/DTO des inscriptions (année, élève, classe).
 */
@Service
@RequiredArgsConstructor
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final AnneeService anneeService;
    private final EleveService eleveService;
    private final ClasseService classeService;

    /**
     * Convertit une entité inscription en objet de réponse.
     *
     * @param inscription L'entité inscription à convertir.
     * @return InscriptionResponseDto La réponse de l'inscription.
     */
    public InscriptionResponseDto toDto(Inscription inscription) {
        return InscriptionResponseDto.builder()
                .id(inscription.getId())
                .annee(anneeService.toDto(inscription.getAnnee()))
                .eleve(eleveService.toDto(inscription.getEleve()))
                .classe(classeService.toDto(inscription.getClasse()))
                .build();
    }
}
