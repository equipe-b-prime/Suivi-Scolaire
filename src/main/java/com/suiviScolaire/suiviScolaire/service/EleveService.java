package com.suiviScolaire.suiviScolaire.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import com.suiviScolaire.suiviScolaire.entity.Annee;
import com.suiviScolaire.suiviScolaire.repository.AnneeRepository;
import com.suiviScolaire.suiviScolaire.entity.Classe;
import com.suiviScolaire.suiviScolaire.repository.ClasseRepository;
import com.suiviScolaire.suiviScolaire.entity.Eleve;
import com.suiviScolaire.suiviScolaire.repository.EleveRepository;
import com.suiviScolaire.suiviScolaire.dto.ClasseElevesResponseDto;
import com.suiviScolaire.suiviScolaire.dto.EleveRequestDto;
import com.suiviScolaire.suiviScolaire.dto.EleveResponseDto;
import com.suiviScolaire.suiviScolaire.dto.RequestDto;
import com.suiviScolaire.suiviScolaire.exception.NotFoundException;
import com.suiviScolaire.suiviScolaire.exception.RessourceUtiliseeException;
import com.suiviScolaire.suiviScolaire.dto.ResponseDto;
import com.suiviScolaire.suiviScolaire.entity.Inscription;
import com.suiviScolaire.suiviScolaire.repository.InscriptionRepository;
import com.suiviScolaire.suiviScolaire.repository.NoteRepository;

/**
 * Service métier gérant les élèves.
 * Fournit la création, la modification, la suppression et les conversions entité/DTO.
 * Le rattachement à une classe passe par la table "inscription"
 * (sans relation directe entre Eleve et Classe).
 */
@Service
public class EleveService extends AbstractCrudService {

    private final EleveRepository eleveRepository;
    private final InscriptionRepository inscriptionRepository;
    private final ClasseRepository classeRepository;
    private final AnneeRepository anneeRepository;
    private final NoteRepository noteRepository;
    private final ClasseService classeService;

    /**
     * Constructeur injectant les repositories et le service des classes.
     *
     * @param eleveRepository le repository des élèves
     * @param inscriptionRepository le repository des inscriptions
     * @param classeRepository le repository des classes
     * @param anneeRepository le repository des années scolaires
     * @param noteRepository le repository des notes
     * @param classeService le service des classes (conversion)
     */
    public EleveService(EleveRepository eleveRepository,
                        InscriptionRepository inscriptionRepository,
                        ClasseRepository classeRepository,
                        AnneeRepository anneeRepository,
                        NoteRepository noteRepository,
                        ClasseService classeService) {
        super(eleveRepository);
        this.eleveRepository = eleveRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.classeRepository = classeRepository;
        this.anneeRepository = anneeRepository;
        this.noteRepository = noteRepository;
        this.classeService = classeService;
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
     * Convertit une entité élève en objet de réponse, avec sa classe.
     *
     * @param entity L'entité élève à convertir.
     * @return ResponseDto Les détails de l'élève converti.
     */
    @Override
    public ResponseDto toDto(Object entity) {
        return toDto((Eleve) entity);
    }

    /**
     * Convertit une entité élève en objet de réponse, avec sa classe.
     *
     * @param eleve L'entité élève à convertir.
     * @return EleveResponseDto Les détails de l'élève converti.
     */
    public EleveResponseDto toDto(Eleve eleve) {
        Classe classe = eleveRepository.findClassesByEleve(eleve.getId())
                .stream().findFirst().orElse(null);
        return EleveResponseDto.builder()
                .id(eleve.getId())
                .nom(eleve.getNom())
                .prenom(eleve.getPrenom())
                .dateNaissance(eleve.getDateNaissance())
                .classe(classe == null ? null : classeService.toDto(classe))
                .build();
    }

    /**
     * Récupère une page d'élèves triés par nom puis par prénom.
     *
     * @param page l'index de la page souhaitée
     * @param taille le nombre d'éléments par page
     * @return une page d'objets {@link ResponseDto}
     */
    @Override
    public Page<ResponseDto> getAll(int page, int taille) {
        Pageable pageable = PageRequest.of(pageAutorisee(page), tailleAutorisee(taille),
                Sort.by("nom", "prenom"));
        return eleveRepository.findAll(pageable).map(this::toDto);
    }

    /**
     * Inscrit un nouvel élève et le rattache à une classe.
     *
     * @param requestDto les informations de l'élève à créer
     * @return l'élève créé
     * @throws NotFoundException si la classe n'existe pas
     * @throws IllegalArgumentException si la classe est déjà pleine
     */
    @Override
    public ResponseDto create(RequestDto requestDto) {
        EleveRequestDto eleveRequest = (EleveRequestDto) requestDto;
        Classe classe = classeRepository.findById(eleveRequest.getClasseId())
                .orElseThrow(() -> new NotFoundException("Classe non trouvée"));
        checkClasseDisponible(classe);
        Eleve eleve = eleveRepository.save(toEntity(eleveRequest));
        Annee annee = resolveAnnee(eleveRequest.getAnneeId());
        inscriptionRepository.save(Inscription.builder()
                .eleve(eleve)
                .classe(classe)
                .annee(annee)
                .build());
        return toDto(eleve);
    }

    /**
     * Modifie un élève existant. La date de naissance reste figée ; la classe peut changer
     * si la classe de destination n'est pas pleine.
     *
     * @param id L'identifiant de l'élève à modifier.
     * @param requestDto Les données de la requête pour modifier l'élève.
     * @return ResponseDto Les détails de l'élève modifié.
     * @throws NotFoundException si l'élève ou la classe n'existe pas
     * @throws IllegalArgumentException si la classe de destination est pleine
     */
    @Override
    public ResponseDto update(Long id, RequestDto requestDto) {
        EleveRequestDto eleveRequest = (EleveRequestDto) requestDto;
        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
        eleve.setNom(eleveRequest.getNom());
        eleve.setPrenom(eleveRequest.getPrenom());
        eleveRepository.save(eleve);
        if (eleveRequest.getClasseId() != null) {
            changeClasse(eleve, eleveRequest.getClasseId(), eleveRequest.getAnneeId());
        }
        return toDto(eleve);
    }

    /**
     * Supprime un élève, sauf s'il possède des notes.
     *
     * @param id L'identifiant de l'élève à supprimer.
     * @return ResponseDto Les détails de l'élève supprimé.
     * @throws NotFoundException si l'élève n'existe pas
     * @throws RessourceUtiliseeException si l'élève possède des notes
     */
    @Override
    public ResponseDto delete(Long id) {
        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
        if (noteRepository.countNotesByEleve(id) > 0) {
            throw new RessourceUtiliseeException("Eleve", id, "Note");
        }
        ResponseDto response = toDto(eleve);
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
     * @throws NotFoundException si la classe n'existe pas
     * @throws IllegalArgumentException si la classe de destination est pleine
     */
    private void changeClasse(Eleve eleve, Long classeId, Long anneeId) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new NotFoundException("Classe non trouvée"));
        checkClasseDisponible(classe);
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
     * Vérifie qu'une classe accepte encore un élève.
     *
     * @param classe La classe à contrôler.
     * @throws IllegalArgumentException si la classe a atteint son effectif maximum
     */
    private void checkClasseDisponible(Classe classe) {
        long effectifReel = classeRepository.countElevesByClasse(classe.getId());
        if (effectifReel >= classe.getEffectifMax()) {
            throw new IllegalArgumentException("La classe '" + classe.getNom()
                    + "' est pleine (" + effectifReel + "/" + classe.getEffectifMax() + ").");
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
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
        }
        return anneeRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    /**
     * Récupère l'effectif d'une classe : la classe, ses effectifs et ses élèves triés par nom puis prénom.
     *
     * @param classeId L'identifiant de la classe.
     * @return La réponse contenant l'effectif de la classe.
     * @throws NotFoundException si la classe n'existe pas
     */
    public ClasseElevesResponseDto getElevesByClasse(Long classeId) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new NotFoundException("Classe non trouvée"));
        List<Eleve> eleves = inscriptionRepository.findElevesByClasseId(classeId);
        return ClasseElevesResponseDto.builder()
                .classeId(classe.getId())
                .nom(classe.getNom())
                .niveau(classe.getNiveau())
                .effectifMax(classe.getEffectifMax())
                .effectifReel(classeRepository.countElevesByClasse(classeId))
                .eleves(eleves.stream().map(this::toDto).toList())
                .build();
    }

    /**
     * Recherche des élèves dont le nom contient le fragment fourni (insensible à la casse).
     *
     * @param nom Le fragment de nom recherché.
     * @return La liste des élèves correspondants, avec leur classe.
     */
    public List<ResponseDto> searchByNom(String nom) {
        return eleveRepository.findByNomContainingIgnoreCase(nom).stream()
                .<ResponseDto>map(this::toDto)
                .toList();
    }

    /**
     * Recherche un eleve par son nom exact (premier résultat en cas d'homonymie).
     *
     * @param nom Le nom du eleve recherché.
     * @return ResponseDto Les détails du eleve trouvé.
     * @throws NotFoundException si aucun eleve ne porte ce nom
     */
    public ResponseDto getByNom(String nom) {
        Eleve eleve = eleveRepository.findByNom(nom).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
        return toDto(eleve);
    }
}
