package com.suiviScolaire.suiviScolaire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.suiviScolaire.suiviScolaire.entity.Eleve;
import com.suiviScolaire.suiviScolaire.entity.Enseignement;
import com.suiviScolaire.suiviScolaire.repository.EnseignementRepository;
import com.suiviScolaire.suiviScolaire.entity.Inscription;
import com.suiviScolaire.suiviScolaire.repository.InscriptionRepository;
import com.suiviScolaire.suiviScolaire.entity.Matiere;
import com.suiviScolaire.suiviScolaire.entity.Note;
import com.suiviScolaire.suiviScolaire.repository.NoteRepository;
import com.suiviScolaire.suiviScolaire.dto.NoteRequestDto;
import com.suiviScolaire.suiviScolaire.dto.NoteResponseDto;
import com.suiviScolaire.suiviScolaire.entity.Periode;
import com.suiviScolaire.suiviScolaire.repository.PeriodeRepository;
import com.suiviScolaire.suiviScolaire.entity.TypeEvaluation;
import com.suiviScolaire.suiviScolaire.repository.TypeEvaluationRepository;

/**
 * Service métier gérant les notes.
 * Fournit les opérations CRUD, les conversions entité/DTO
 * et la récupération de l'élève et de la matière (requêtes batch, sans JOIN).
 */
@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
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
     * Convertit une entité note en objet de réponse (sans élève ni matière).
     *
     * @param note L'entité note à convertir.
     * @return NoteResponseDto Les détails de la note convertie.
     */
    public NoteResponseDto toDto(Note note) {
        return NoteResponseDto.builder()
                .id(note.getId())
                .valeur(note.getValeur())
                .inscription(inscriptionService.toDto(note.getInscription()))
                .enseignement(enseignementService.toDto(note.getEnseignement()))
                .periode(periodeService.toDto(note.getPeriode()))
                .typeEvaluation(typeEvaluationService.toDto(note.getTypeEvaluation()))
                .build();
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
     * Crée une nouvelle note.
     *
     * @param noteRequest les informations de la note à créer
     * @return la note créée
     */
    public NoteResponseDto createNote(NoteRequestDto noteRequest) {
        Inscription inscription = inscriptionRepository.findById(noteRequest.getInscriptionId())
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée"));
        Enseignement enseignement = enseignementRepository.findById(noteRequest.getEnseignementId())
                .orElseThrow(() -> new RuntimeException("Enseignement non trouvé"));
        Periode periode = periodeRepository.findById(noteRequest.getPeriodeId())
                .orElseThrow(() -> new RuntimeException("Période non trouvée"));
        TypeEvaluation typeEvaluation = typeEvaluationRepository.findById(noteRequest.getTypeEvaluationId())
                .orElseThrow(() -> new RuntimeException("Type d'évaluation non trouvé"));
        return toDto(noteRepository.save(toEntity(noteRequest, inscription, enseignement, periode, typeEvaluation)));
    }

    /**
     * Récupère une note à partir de son identifiant, avec l'élève et la matière.
     *
     * @param id L'identifiant de la note à récupérer.
     * @return NoteResponseDto Les détails de la note récupérée.
     */
    public NoteResponseDto getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note non trouvée"));
        return toDtoWithDetails(note,
                note.getInscription().getEleve(),
                note.getEnseignement().getMatiere());
    }

    /**
     * Récupère la liste de toutes les notes, avec l'élève et la matière (batch).
     *
     * @return List<NoteResponseDto> La liste des notes récupérées.
     */
    public List<NoteResponseDto> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        List<Long> inscriptionIds = notes.stream().map(note -> note.getInscription().getId()).distinct().toList();
        List<Long> enseignementIds = notes.stream().map(note -> note.getEnseignement().getId()).distinct().toList();
        Map<Long, Eleve> eleves = new HashMap<>();
        Map<Long, Matiere> matieres = new HashMap<>();
        if (!inscriptionIds.isEmpty()) {
            for (Object[] row : inscriptionRepository.findEleveByInscriptionIds(inscriptionIds)) {
                eleves.put((Long) row[0], (Eleve) row[1]);
            }
        }
        if (!enseignementIds.isEmpty()) {
            for (Object[] row : enseignementRepository.findMatiereByEnseignementIds(enseignementIds)) {
                matieres.put((Long) row[0], (Matiere) row[1]);
            }
        }
        return notes.stream()
                .map(note -> toDtoWithDetails(note,
                        eleves.get(note.getInscription().getId()),
                        matieres.get(note.getEnseignement().getId())))
                .toList();
    }

    /**
     * Modifie une note existante.
     *
     * @param id L'identifiant de la note à modifier.
     * @param noteRequest Les données de la requête pour modifier la note.
     * @return NoteResponseDto Les détails de la note modifiée.
     */
    public NoteResponseDto updateNoteById(Long id, NoteRequestDto noteRequest) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note non trouvée"));
        Inscription inscription = inscriptionRepository.findById(noteRequest.getInscriptionId())
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée"));
        Enseignement enseignement = enseignementRepository.findById(noteRequest.getEnseignementId())
                .orElseThrow(() -> new RuntimeException("Enseignement non trouvé"));
        Periode periode = periodeRepository.findById(noteRequest.getPeriodeId())
                .orElseThrow(() -> new RuntimeException("Période non trouvée"));
        TypeEvaluation typeEvaluation = typeEvaluationRepository.findById(noteRequest.getTypeEvaluationId())
                .orElseThrow(() -> new RuntimeException("Type d'évaluation non trouvé"));
        note.setValeur(noteRequest.getValeur());
        note.setInscription(inscription);
        note.setEnseignement(enseignement);
        note.setPeriode(periode);
        note.setTypeEvaluation(typeEvaluation);
        return toDto(noteRepository.save(note));
    }

    /**
     * Supprime une note à partir de son identifiant.
     *
     * @param id L'identifiant de la note à supprimer.
     * @return NoteResponseDto Les détails de la note supprimée.
     */
    public NoteResponseDto deleteNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note non trouvée"));
        noteRepository.deleteById(id);
        return toDto(note);
    }

    /**
     * Construit la réponse d'une note en incluant l'élève et la matière.
     *
     * @param note L'entité note.
     * @param eleve L'élève associé (peut être null).
     * @param matiere La matière associée (peut être null).
     * @return NoteResponseDto La réponse de la note.
     */
    private NoteResponseDto toDtoWithDetails(Note note, Eleve eleve, Matiere matiere) {
        NoteResponseDto response = toDto(note);
        response.setEleve(eleve == null ? null : eleveService.toDto(eleve));
        response.setMatiere(matiere == null ? null : matiereService.toDto(matiere));
        return response;
    }
}
