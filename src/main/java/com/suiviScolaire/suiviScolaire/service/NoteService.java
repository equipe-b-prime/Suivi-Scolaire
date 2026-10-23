package com.suiviScolaire.suiviScolaire.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.suiviScolaire.suiviScolaire.entity.Enseignement;
import com.suiviScolaire.suiviScolaire.repository.EnseignementRepository;
import com.suiviScolaire.suiviScolaire.entity.Inscription;
import com.suiviScolaire.suiviScolaire.repository.InscriptionRepository;
import com.suiviScolaire.suiviScolaire.entity.Classe;
import com.suiviScolaire.suiviScolaire.entity.Eleve;
import com.suiviScolaire.suiviScolaire.entity.Matiere;
import com.suiviScolaire.suiviScolaire.entity.Note;
import com.suiviScolaire.suiviScolaire.repository.NoteRepository;
import com.suiviScolaire.suiviScolaire.repository.EleveRepository;
import com.suiviScolaire.suiviScolaire.repository.ClasseRepository;
import com.suiviScolaire.suiviScolaire.repository.MatiereRepository;
import com.suiviScolaire.suiviScolaire.dto.ClasseNotesResponseDto;
import com.suiviScolaire.suiviScolaire.dto.EleveResponseDto;
import com.suiviScolaire.suiviScolaire.dto.NotesParEleveResponseDto;
import com.suiviScolaire.suiviScolaire.dto.NotesParMatiereResponseDto;
import com.suiviScolaire.suiviScolaire.dto.NoteRequestDto;
import com.suiviScolaire.suiviScolaire.dto.NoteResponseDto;
import com.suiviScolaire.suiviScolaire.dto.RequestDto;
import com.suiviScolaire.suiviScolaire.exception.NotFoundException;
import com.suiviScolaire.suiviScolaire.exception.RequeteInvalideException;
import com.suiviScolaire.suiviScolaire.dto.ResponseDto;
import com.suiviScolaire.suiviScolaire.entity.Periode;
import com.suiviScolaire.suiviScolaire.repository.PeriodeRepository;
import com.suiviScolaire.suiviScolaire.entity.TypeEvaluation;
import com.suiviScolaire.suiviScolaire.repository.TypeEvaluationRepository;

/**
 * Service métier gérant les notes.
 * Fournit les opérations CRUD ainsi que les conversions entité/DTO.
 * L'élève et la matière sont remontés via les relations existantes
 * (note → inscription → élève et note → enseignement → matière).
 */
@Service
public class NoteService extends AbstractCrudService {

    /**
     * Propriétés de tri acceptées sur la liste des notes.
     */
    private static final List<String> PROPRIETES_TRI = List.of("id", "valeur", "dateSaisie");

    private final NoteRepository noteRepository;
    private final EleveRepository eleveRepository;
    private final ClasseRepository classeRepository;
    private final MatiereRepository matiereRepository;
    private final InscriptionRepository inscriptionRepository;
    private final EnseignementRepository enseignementRepository;
    private final PeriodeRepository periodeRepository;
    private final TypeEvaluationRepository typeEvaluationRepository;
    private final InscriptionService inscriptionService;
    private final EnseignementService enseignementService;
    private final PeriodeService periodeService;
    private final TypeEvaluationService typeEvaluationService;
    private final EleveService eleveService;
    private final MatiereService matiereService;

    /**
     * Constructeur injectant les repositories et les services de conversion.
     *
     * @param noteRepository le repository des notes
     * @param eleveRepository le repository des élèves
     * @param classeRepository le repository des classes
     * @param matiereRepository le repository des matières
     * @param inscriptionRepository le repository des inscriptions
     * @param enseignementRepository le repository des enseignements
     * @param periodeRepository le repository des périodes
     * @param typeEvaluationRepository le repository des types d'évaluation
     * @param inscriptionService le service des inscriptions (conversion)
     * @param enseignementService le service des enseignements (conversion)
     * @param periodeService le service des périodes (conversion)
     * @param typeEvaluationService le service des types d'évaluation (conversion)
     * @param eleveService le service des élèves (conversion)
     * @param matiereService le service des matières (conversion)
     */
    public NoteService(NoteRepository noteRepository,
                       EleveRepository eleveRepository,
                       ClasseRepository classeRepository,
                       MatiereRepository matiereRepository,
                       InscriptionRepository inscriptionRepository,
                       EnseignementRepository enseignementRepository,
                       PeriodeRepository periodeRepository,
                       TypeEvaluationRepository typeEvaluationRepository,
                       InscriptionService inscriptionService,
                       EnseignementService enseignementService,
                       PeriodeService periodeService,
                       TypeEvaluationService typeEvaluationService,
                       EleveService eleveService,
                       MatiereService matiereService) {
        super(noteRepository);
        this.noteRepository = noteRepository;
        this.eleveRepository = eleveRepository;
        this.classeRepository = classeRepository;
        this.matiereRepository = matiereRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.enseignementRepository = enseignementRepository;
        this.periodeRepository = periodeRepository;
        this.typeEvaluationRepository = typeEvaluationRepository;
        this.inscriptionService = inscriptionService;
        this.enseignementService = enseignementService;
        this.periodeService = periodeService;
        this.typeEvaluationService = typeEvaluationService;
        this.eleveService = eleveService;
        this.matiereService = matiereService;
    }

    /**
     * Convertit les données d'une requête en entité note.
     *
     * @param noteRequest Les données de la requête pour créer la note.
     * @param inscription L'inscription de l'élève associée.
     * @param enseignement L'enseignement (professeur/matière) associé.
     * @param periode La période (trimestre) associée.
     * @param typeEvaluation Le type d'évaluation associé.
     * @return Note L'entité note créée.
     */
    public Note toEntity(NoteRequestDto noteRequest, Inscription inscription,
                         Enseignement enseignement, Periode periode,
                         TypeEvaluation typeEvaluation) {
        return Note.builder()
                .valeur(noteRequest.getValeur())
                .inscription(inscription)
                .enseignement(enseignement)
                .periode(periode)
                .typeEvaluation(typeEvaluation)
                .build();
    }

    /**
     * Convertit une entité note en objet de réponse, avec son élève et sa matière.
     *
     * @param entity L'entité note à convertir.
     * @return ResponseDto Les détails de la note convertie.
     */
    @Override
    public ResponseDto toDto(Object entity) {
        return toDto((Note) entity);
    }

    /**
     * Convertit une entité note en objet de réponse, avec son élève et sa matière.
     *
     * @param note L'entité note à convertir.
     * @return NoteResponseDto Les détails de la note convertie.
     */
    public NoteResponseDto toDto(Note note) {
        return NoteResponseDto.builder()
                .id(note.getId())
                .valeur(note.getValeur())
                .dateSaisie(note.getDateSaisie())
                .inscription(inscriptionService.toDto(note.getInscription()))
                .enseignement(enseignementService.toDto(note.getEnseignement()))
                .periode(periodeService.toDto(note.getPeriode()))
                .typeEvaluation(typeEvaluationService.toDto(note.getTypeEvaluation()))
                .eleve(eleveService.toDto(note.getInscription().getEleve()))
                .matiere(matiereService.toDto(note.getEnseignement().getMatiere()))
                .build();
    }

    /**
     * Récupère une page de notes triées par matière (via l'enseignement).
     *
     * @param page l'index de la page souhaitée
     * @param taille le nombre d'éléments par page
     * @return une page d'objets {@link ResponseDto}
     */
    @Override
    public Page<ResponseDto> getAll(int page, int taille) {
        Pageable pageable = PageRequest.of(pageAutorisee(page), tailleAutorisee(taille),
                Sort.by("enseignement.matiere.nom"));
        return noteRepository.findAll(pageable).map(this::toDto);
    }

    /**
     * Crée une nouvelle note.
     *
     * @param requestDto les informations de la note à créer
     * @return la note créée
     */
    @Override
    public ResponseDto create(RequestDto requestDto) {
        NoteRequestDto noteRequest = (NoteRequestDto) requestDto;
        Inscription inscription = inscriptionRepository.findById(noteRequest.getInscriptionId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
        Enseignement enseignement = enseignementRepository.findById(noteRequest.getEnseignementId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
        Periode periode = periodeRepository.findById(noteRequest.getPeriodeId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
        TypeEvaluation typeEvaluation = typeEvaluationRepository.findById(noteRequest.getTypeEvaluationId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
        checkTrimestreOuvert(periode);
        return toDto(noteRepository.save(toEntity(noteRequest, inscription, enseignement, periode, typeEvaluation)));
    }

    /**
     * Supprime une note, tant que le trimestre n'est pas clôturé.
     *
     * @param id L'identifiant de la note à supprimer.
     * @return ResponseDto Les détails de la note supprimée.
     * @throws NotFoundException si la note n'existe pas
     * @throws IllegalArgumentException si le trimestre est clôturé
     */
    @Override
    public ResponseDto delete(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
        checkTrimestreOuvert(note.getPeriode());
        ResponseDto response = toDto(note);
        noteRepository.deleteById(id);
        return response;
    }

    /**
     * Modifie une note existante.
     *
     * @param id L'identifiant de la note à modifier.
     * @param requestDto Les données de la requête pour modifier la note.
     * @return ResponseDto Les détails de la note modifiée.
     */
    @Override
    public ResponseDto update(Long id, RequestDto requestDto) {
        NoteRequestDto noteRequest = (NoteRequestDto) requestDto;
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
        Periode periode = periodeRepository.findById(noteRequest.getPeriodeId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
        TypeEvaluation typeEvaluation = typeEvaluationRepository.findById(noteRequest.getTypeEvaluationId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
        checkTrimestreOuvert(periode);
        note.setValeur(noteRequest.getValeur());
        note.setPeriode(periode);
        note.setTypeEvaluation(typeEvaluation);
        return toDto(noteRepository.save(note));
    }

    /**
     * Vérifie qu'un trimestre n'est pas clôturé.
     *
     * @param periode La période (trimestre) concernée.
     * @throws IllegalArgumentException si le trimestre est clôturé
     */
    private void checkTrimestreOuvert(Periode periode) {
        if (periode.isCloture()) {
            throw new IllegalArgumentException("Le trimestre '" + periode.getNom()
                    + "' est clôturé : la note n'est plus modifiable.");
        }
    }

    /**
     * Récupère les notes d'un élève, regroupées par matière.
     * Sans trimestre, toutes les notes de l'élève sont renvoyées.
     *
     * @param eleveId L'identifiant de l'élève.
     * @param trimestre Le numéro du trimestre (optionnel).
     * @return Les notes regroupées par matière.
     * @throws NotFoundException si l'élève n'existe pas
     */
    public List<NotesParMatiereResponseDto> getNotesByEleve(Long eleveId, Integer trimestre) {
        if (!eleveRepository.existsById(eleveId)) {
            throw new NotFoundException("Elève non trouvé");
        }
        List<Note> notes = trimestre == null
                ? noteRepository.findNotesByEleve(eleveId)
                : noteRepository.findNotesByEleveAndTrimestre(eleveId, trimestre);
        return groupParMatiere(notes);
    }

    /**
     * Récupère les notes d'une classe dans une matière pour un trimestre donné,
     * regroupées par élève, avec la moyenne de la classe.
     *
     * @param classeId L'identifiant de la classe.
     * @param matiereId L'identifiant de la matière.
     * @param trimestre Le numéro du trimestre.
     * @return Les résultats de la classe dans cette matière.
     * @throws NotFoundException si la classe ou la matière n'existe pas
     * @throws RequeteInvalideException si la matière n'est pas enseignée dans cette classe
     */
    public ClasseNotesResponseDto getNotesByClasse(Long classeId, Long matiereId, Integer trimestre) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new NotFoundException("Classe non trouvée"));
        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new NotFoundException("Matière non trouvée"));
        if (noteRepository.countNotesByClasseAndMatiere(classeId, matiereId) == 0) {
            throw new RequeteInvalideException("La matière '" + matiere.getNom()
                    + "' n'est pas enseignée dans la classe '" + classe.getNom() + "'.");
        }
        List<Note> notes = noteRepository.findNotesByClasseAndMatiereAndTrimestre(classeId, matiereId, trimestre);
        List<NotesParEleveResponseDto> parEleve = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        int nombre = 0;
        for (Note note : notes) {
            Eleve eleve = note.getInscription().getEleve();
            total = total.add(note.getValeur());
            nombre++;
            if (parEleve.isEmpty() || !parEleve.get(parEleve.size() - 1).getEleve().getId().equals(eleve.getId())) {
                parEleve.add(NotesParEleveResponseDto.builder()
                        .eleve(eleveService.toDto(eleve))
                        .notes(new ArrayList<>())
                        .build());
            }
            parEleve.get(parEleve.size() - 1).getNotes().add(toDto(note));
        }
        BigDecimal moyenne = nombre == 0 ? BigDecimal.ZERO
                : total.divide(BigDecimal.valueOf(nombre), 2, RoundingMode.HALF_UP);
        return ClasseNotesResponseDto.builder()
                .classeId(classe.getId())
                .classeNom(classe.getNom())
                .matiere(matiereService.toDto(matiere))
                .trimestre(trimestre)
                .moyenneClasse(moyenne)
                .eleves(parEleve)
                .build();
    }

    /**
     * Récupère une page de notes avec le nombre total d'éléments et de pages.
     * La taille de page est ramenée à 100 au maximum.
     *
     * @param page l'index de la page souhaitée
     * @param size le nombre d'éléments par page (30 par défaut, 100 au maximum)
     * @param sort le tri appliqué, au format "propriété" ou "propriété,direction"
     * @return la page demandée
     * @throws IllegalArgumentException si la propriété de tri n'est pas supportée
     */
    public Page<ResponseDto> getAllPage(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(pageAutorisee(page), taillePage(size), parseSort(sort));
        return noteRepository.findAll(pageable).map(this::toDto);
    }

    /**
     * Regroupe une liste de notes par matière (les notes étant déjà triées par matière).
     *
     * @param notes Les notes à regrouper.
     * @return La liste des notes groupées par matière.
     */
    private List<NotesParMatiereResponseDto> groupParMatiere(List<Note> notes) {
        List<NotesParMatiereResponseDto> groupes = new ArrayList<>();
        for (Note note : notes) {
            Long matiereId = note.getEnseignement().getMatiere().getId();
            if (groupes.isEmpty() || !groupes.get(groupes.size() - 1).getMatiere().getId().equals(matiereId)) {
                groupes.add(NotesParMatiereResponseDto.builder()
                        .matiere(matiereService.toDto(note.getEnseignement().getMatiere()))
                        .notes(new ArrayList<>())
                        .build());
            }
            groupes.get(groupes.size() - 1).getNotes().add(toDto(note));
        }
        return groupes;
    }

    /**
     * Borne la taille de page demandée : 30 éléments par défaut, 100 au maximum.
     *
     * @param size la taille de page transmise par le client
     * @return la taille de page autorisée
     */
    private int taillePage(int size) {
        if (size < 1) {
            return 30;
        }
        return Math.min(size, CrudService.TAILLE_MAX);
    }

    /**
     * Traduit le paramètre de tri ("valeur,desc") en objet {@link Sort}.
     *
     * @param sort le tri demandé
     * @return le tri correspondant
     * @throws IllegalArgumentException si la propriété de tri n'est pas supportée
     */
    private Sort parseSort(String sort) {
        String[] parties = sort.split(",");
        String propriete = parties[0].trim();
        Sort.Direction direction = parties.length > 1
                ? Sort.Direction.fromString(parties[1].trim())
                : Sort.Direction.ASC;
        if (!PROPRIETES_TRI.contains(propriete)) {
            throw new RequeteInvalideException("Le tri '" + propriete + "' n'est pas supporté.");
        }
        return Sort.by(direction, propriete);
    }
}
