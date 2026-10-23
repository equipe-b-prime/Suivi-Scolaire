package com.suiviScolaire.suiviScolaire.service;

import org.springframework.stereotype.Service;

import com.suiviScolaire.suiviScolaire.entity.Enseignement;
import com.suiviScolaire.suiviScolaire.repository.EnseignementRepository;
import com.suiviScolaire.suiviScolaire.dto.EnseignementResponseDto;

/**
 * Service métier gérant les enseignements.
 * Fournit la conversion entité/DTO des enseignements (matière, professeur).
 */
@Service
public class EnseignementService {

    private final EnseignementRepository enseignementRepository;
    private final MatiereService matiereService;
    private final ProfesseurService professeurService;

    /**
     * Constructeur injectant le repository des enseignements et les services des matières et des professeurs.
     *
     * @param enseignementRepository le repository des enseignements
     * @param matiereService le service des matières (conversion)
     * @param professeurService le service des professeurs (conversion)
     */
    public EnseignementService(EnseignementRepository enseignementRepository, MatiereService matiereService, ProfesseurService professeurService) {
        this.enseignementRepository = enseignementRepository;
        this.matiereService = matiereService;
        this.professeurService = professeurService;
    }

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
