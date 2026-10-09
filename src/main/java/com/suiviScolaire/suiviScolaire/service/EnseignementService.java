package com.suiviScolaire.suiviScolaire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.suiviScolaire.suiviScolaire.entity.Enseignement;
import com.suiviScolaire.suiviScolaire.repository.EnseignementRepository;
import com.suiviScolaire.suiviScolaire.dto.EnseignementResponseDto;

/**
 * Service métier gérant les enseignements.
 * Fournit la conversion entité/DTO des enseignements (matière, professeur).
 */
@Service
@RequiredArgsConstructor
public class EnseignementService {

    private final EnseignementRepository enseignementRepository;
    private final MatiereService matiereService;
    private final ProfesseurService professeurService;

    /**
     * Convertit une entité enseignement en objet de réponse.
     *
     * @param enseignement L'entité enseignement à convertir.
     * @return EnseignementResponseDto La réponse de l'enseignement.
     */
    public EnseignementResponseDto toDto(Enseignement enseignement) {
        return EnseignementResponseDto.builder()
                .id(enseignement.getId())
                .matiere(matiereService.toDto(enseignement.getMatiere()))
                .professeur(professeurService.toDto(enseignement.getProfesseur()))
                .build();
    }
}
