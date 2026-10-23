package com.suiviScolaire.suiviScolaire.service;

import org.springframework.stereotype.Service;

import com.suiviScolaire.suiviScolaire.entity.Inscription;
import com.suiviScolaire.suiviScolaire.repository.InscriptionRepository;
import com.suiviScolaire.suiviScolaire.dto.InscriptionResponseDto;

/**
 * Service métier gérant les inscriptions.
 * Fournit la conversion entité/DTO des inscriptions (année, élève, classe).
 */
@Service
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final AnneeService anneeService;
    private final EleveService eleveService;
    private final ClasseService classeService;

    /**
     * Constructeur injectant le repository des inscriptions et les services des années scolaires, des élèves et des classes.
     *
     * @param inscriptionRepository le repository des inscriptions
     * @param anneeService le service des années scolaires (conversion)
     * @param eleveService le service des élèves (conversion)
     * @param classeService le service des classes (conversion)
     */
    public InscriptionService(InscriptionRepository inscriptionRepository, AnneeService anneeService, EleveService eleveService, ClasseService classeService) {
        this.inscriptionRepository = inscriptionRepository;
        this.anneeService = anneeService;
        this.eleveService = eleveService;
        this.classeService = classeService;
    }

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
