package com.suiviScolaire.suiviScolaire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.suiviScolaire.suiviScolaire.entity.Annee;
import com.suiviScolaire.suiviScolaire.repository.AnneeRepository;
import com.suiviScolaire.suiviScolaire.entity.Classe;
import com.suiviScolaire.suiviScolaire.repository.ClasseRepository;
import com.suiviScolaire.suiviScolaire.entity.Eleve;
import com.suiviScolaire.suiviScolaire.repository.EleveRepository;
import com.suiviScolaire.suiviScolaire.dto.EleveRequestDto;
import com.suiviScolaire.suiviScolaire.dto.EleveResponseDto;
import com.suiviScolaire.suiviScolaire.entity.Inscription;
import com.suiviScolaire.suiviScolaire.repository.InscriptionRepository;

/**
 * Service métier gérant les élèves.
 * Fournit les opérations CRUD, les conversions entité/DTO
 * et la gestion du rattachement à une classe via la table "inscription"
 * (sans relation directe entre Eleve et Classe).
 */
@Service
@RequiredArgsConstructor
public class EleveService {

    private final EleveRepository eleveRepository;
    private final InscriptionRepository inscriptionRepository;
    private final ClasseRepository classeRepository;
    private final AnneeRepository anneeRepository;
    private final ClasseService classeService;

    /**
     * Convertit une entité élève en objet de réponse (sans la classe).
     *
     * @param eleve L'entité élève à convertir.
     * @return EleveResponseDto Les détails de l'élève converti.
     */
    public EleveResponseDto toDto(Eleve eleve) {
        return EleveResponseDto.builder()
                .id(eleve.getId())
                .nom(eleve.getNom())
                .prenom(eleve.getPrenom())
                .dateNaissance(eleve.getDateNaissance())
                .build();
    }

    /**
     * Convertit les données d'une requête en entité élève.
     *
     * @param eleveRequest Les données de la requête pour créer l'élève.
     * @return Eleve L'entité élève créée.
     */
    public Eleve toEntity(EleveRequestDto eleveRequest) {
        return Eleve.builder()
                .nom(eleveRequest.getNom())
                .prenom(eleveRequest.getPrenom())
                .dateNaissance(eleveRequest.getDateNaissance())
                .build();
    }

    /**
     * Inscrit un nouvel élève et le rattache à une classe (si fournie).
     *
     * @param eleveRequest les informations de l'élève à créer
     * @return l'élève créé
     */
    public EleveResponseDto createEleve(EleveRequestDto eleveRequest) {
        Eleve eleve = eleveRepository.save(toEntity(eleveRequest));
        if (eleveRequest.getClasseId() != null) {
            Classe classe = classeRepository.findById(eleveRequest.getClasseId())
                    .orElseThrow(() -> new RuntimeException("Classe non trouvée"));
            Annee annee = resolveAnnee(eleveRequest.getAnneeId());
            inscriptionRepository.save(Inscription.builder()
                    .eleve(eleve)
                    .classe(classe)
                    .annee(annee)
                    .build());
        }
        return toDto(eleve);
    }

    /**
     * Récupère un élève à partir de son identifiant, avec sa classe.
     *
     * @param id L'identifiant de l'élève à récupérer.
     * @return EleveResponseDto Les détails de l'élève récupéré.
     */
    public EleveResponseDto getEleveById(Long id) {
        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé"));
        Classe classe = eleveRepository.findClassesByEleve(id).stream().findFirst().orElse(null);
        return toDtoWithClasse(eleve, classe);
    }

    /**
     * Récupère la liste de tous les élèves, avec leur classe (batch).
     *
     * @return List<EleveResponseDto> La liste des élèves récupérés.
     */
    public List<EleveResponseDto> getAllEleves() {
        List<Eleve> eleves = eleveRepository.findAll();
        List<Long> ids = eleves.stream().map(Eleve::getId).toList();
        Map<Long, Classe> classes = new HashMap<>();
        if (!ids.isEmpty()) {
            for (Object[] row : inscriptionRepository.findClasseByEleveIds(ids)) {
                classes.putIfAbsent((Long) row[0], (Classe) row[1]);
            }
        }
        return eleves.stream()
                .map(eleve -> toDtoWithClasse(eleve, classes.get(eleve.getId())))
                .toList();
    }

    /**
     * Modifie un élève existant et, si une classe est fournie, change sa classe.
     *
     * @param id L'identifiant de l'élève à modifier.
     * @param eleveRequest Les données de la requête pour modifier l'élève.
     * @return EleveResponseDto Les détails de l'élève modifié.
     */
    public EleveResponseDto updateEleveById(Long id, EleveRequestDto eleveRequest) {
        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé"));
        eleve.setNom(eleveRequest.getNom());
        eleve.setPrenom(eleveRequest.getPrenom());
        eleve.setDateNaissance(eleveRequest.getDateNaissance());
        eleveRepository.save(eleve);
        if (eleveRequest.getClasseId() != null) {
            changeClasse(eleve, eleveRequest.getClasseId(), eleveRequest.getAnneeId());
        }
        return getEleveById(id);
    }

    /**
     * Supprime un élève ainsi que ses inscriptions (évite la violation de clé étrangère).
     *
     * @param id L'identifiant de l'élève à supprimer.
     * @return EleveResponseDto Les détails de l'élève supprimé.
     */
    public EleveResponseDto deleteEleveById(Long id) {
        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé"));
        EleveResponseDto response = toDto(eleve);
        inscriptionRepository.deleteAll(inscriptionRepository.findByEleveId(id));
        eleveRepository.deleteById(id);
        return response;
    }

    /**
     * Change la classe d'un élève (met à jour son inscription, ou en crée une).
     *
     * @param eleve L'élève concerné.
     * @param classeId L'identifiant de la nouvelle classe.
     * @param anneeId L'identifiant de l'année (optionnel).
     */
    private void changeClasse(Eleve eleve, Long classeId, Long anneeId) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new RuntimeException("Classe non trouvée"));
        List<Inscription> inscriptions = inscriptionRepository.findByEleveId(eleve.getId());
        if (inscriptions.isEmpty()) {
            Annee annee = resolveAnnee(anneeId);
            inscriptionRepository.save(Inscription.builder()
                    .eleve(eleve)
                    .classe(classe)
                    .annee(annee)
                    .build());
        } else {
            Inscription inscription = inscriptions.get(0);
            inscription.setClasse(classe);
            inscriptionRepository.save(inscription);
        }
    }

    /**
     * Résout l'année scolaire à utiliser : celle fournie, sinon la plus récente.
     *
     * @param anneeId L'identifiant de l'année (peut être null).
     * @return Annee L'année résolue.
     */
    private Annee resolveAnnee(Long anneeId) {
        if (anneeId != null) {
            return anneeRepository.findById(anneeId)
                    .orElseThrow(() -> new RuntimeException("Année non trouvée"));
        }
        return anneeRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("Aucune année scolaire disponible"));
    }

    /**
     * Construit la réponse d'un élève en incluant sa classe.
     *
     * @param eleve L'entité élève.
     * @param classe La classe de l'élève (peut être null).
     * @return EleveResponseDto La réponse de l'élève.
     */
    private EleveResponseDto toDtoWithClasse(Eleve eleve, Classe classe) {
        return EleveResponseDto.builder()
                .id(eleve.getId())
                .nom(eleve.getNom())
                .prenom(eleve.getPrenom())
                .dateNaissance(eleve.getDateNaissance())
                .classe(classe == null ? null : classeService.toDto(classe))
                .build();
    }
}