package com.suiviScolaire.suiviScolaire.controller;

import com.suiviScolaire.suiviScolaire.entity.Classe;
import com.suiviScolaire.suiviScolaire.entity.Matiere;
import com.suiviScolaire.suiviScolaire.entity.Periode;
import com.suiviScolaire.suiviScolaire.exception.MotifRouvreInvalideException;
import com.suiviScolaire.suiviScolaire.exception.NotFoundException;
import com.suiviScolaire.suiviScolaire.repository.ClasseRepository;
import com.suiviScolaire.suiviScolaire.repository.MatiereRepository;
import com.suiviScolaire.suiviScolaire.repository.NoteRepository;
import com.suiviScolaire.suiviScolaire.repository.PeriodeRepository;
import com.suiviScolaire.suiviScolaire.service.NoteService;
import com.suiviScolaire.suiviScolaire.utils.response.Response;
import com.suiviScolaire.suiviScolaire.utils.response.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/trimestres")
@Tag(name = "Trimestres / Périodes", description = "Endpoints pour la gestion, la clôture et la réouverture des trimestres scolaires")
public class PeriodeController {

    private final PeriodeRepository periodeRepository;
    private final NoteService noteService;
    private final ClasseRepository classeRepository;
    private final MatiereRepository matiereRepository;
    private final NoteRepository noteRepository;

    public PeriodeController(PeriodeRepository periodeRepository,
                             NoteService noteService,
                             ClasseRepository classeRepository,
                             MatiereRepository matiereRepository,
                             NoteRepository noteRepository) {
        this.periodeRepository = periodeRepository;
        this.noteService = noteService;
        this.classeRepository = classeRepository;
        this.matiereRepository = matiereRepository;
        this.noteRepository = noteRepository;
    }

    @Operation(summary = "Rouvrir un trimestre clôturé", description = "Rouvre le dernier trimestre clôturé sous réserve de fournir un motif explicite d'au moins 20 caractères.")
    @PostMapping("/{numero}/reouverture")
    public ResponseEntity<Response<String>> reouvertureTrimestre(
            @Parameter(description = "Numéro du trimestre à rouvrir (1, 2 ou 3)", example = "1", required = true) @PathVariable Integer numero,
            @Parameter(description = "Motif justifiant la réouverture (minimum 20 caractères)", example = "Saisie tardive des notes d'évaluation de rattrapage en mathématiques", required = true) @RequestParam String motif) {
        if (motif == null || motif.trim().length() < 20) {
            throw new MotifRouvreInvalideException("Le motif de réouverture doit faire au moins 20 caractères.");
        }

        Periode periode = periodeRepository.findByNumero(numero)
                .orElseThrow(() -> new NotFoundException("Trimestre " + numero + " non trouvé"));

        if (!periode.isCloture()) {
            Response<String> reponse = new Response<>("Le trimestre " + numero + " n'est pas clôturé.");
            reponse.setHasError(true);
            reponse.setStatus(new Status(HttpStatus.CONFLICT.value(), "Trimestre non clôturé"));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(reponse);
        }

        boolean dernierCloture = true;
        for (int i = numero + 1; i <= 3; i++) {
            Periode trimestreSup = periodeRepository.findByNumero(i).orElse(null);
            if (trimestreSup != null && trimestreSup.isCloture()) {
                dernierCloture = false;
                break;
            }
        }

        if (!dernierCloture) {
            Response<String> reponse = new Response<>("Impossible de rouvrir le trimestre " + numero + " : un trimestre supérieur est clôturé.");
            reponse.setHasError(true);
            reponse.setStatus(new Status(HttpStatus.CONFLICT.value(), "Pas le dernier trimestre clôturé"));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(reponse);
        }

        periode.setCloture(false);
        periode.setMotifRouvre(motif);
        periode.setDateRouvre(LocalDate.now());
        periodeRepository.save(periode);

        Response<String> reponse = new Response<>("Trimestre " + numero + " réouvert avec succès. Motif: " + motif);
        reponse.setHasError(false);
        reponse.setNombre(1L);
        reponse.setStatus(new Status(HttpStatus.OK.value(), "Trimestre rouvert"));
        return ResponseEntity.ok(reponse);
    }

    @Operation(summary = "Clôturer un trimestre", description = "Clôture un trimestre après vérification que toutes les matières de toutes les classes possèdent au moins une note enregistrée.")
    @PostMapping("/{numero}/cloture")
    public ResponseEntity<Response<String>> clotureTrimestre(
            @Parameter(description = "Numéro du trimestre à clôturer (1, 2 ou 3)", example = "1", required = true) @PathVariable Integer numero) {
        Periode periode = periodeRepository.findByNumero(numero)
                .orElseThrow(() -> new NotFoundException("Trimestre " + numero + " non trouvé"));

        if (periode.isCloture()) {
            Response<String> reponse = new Response<>("Le trimestre " + numero + " est déjà clôturé.");
            reponse.setHasError(true);
            reponse.setStatus(new Status(HttpStatus.CONFLICT.value(), "Trimestre déjà clôturé"));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(reponse);
        }

        List<String> manques = new ArrayList<>();
        List<Classe> classes = classeRepository.findAll();
        List<Matiere> matieres = matiereRepository.findAll();

        for (Classe classe : classes) {
            for (Matiere matiere : matieres) {
                long count = noteRepository.countNotesByClasseAndMatiereAndTrimestre(classe.getId(), matiere.getId(), numero);
                if (count == 0) {
                    manques.add("Classe " + classe.getNom() + " - Matière " + matiere.getNom());
                }
            }
        }

        if (!manques.isEmpty()) {
            Response<String> reponse = new Response<>("Impossible de clôturer le trimestre " + numero + " : notes manquantes.");
            reponse.setHasError(true);
            reponse.setNombre((long) manques.size());
            reponse.setData(manques);
            reponse.setStatus(new Status(HttpStatus.CONFLICT.value(), "Clôture refusée"));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(reponse);
        }

        periode.setCloture(true);
        periodeRepository.save(periode);

        Response<String> reponse = new Response<>("Trimestre " + numero + " clôturé avec succès.");
        reponse.setHasError(false);
        reponse.setNombre(1L);
        reponse.setStatus(new Status(HttpStatus.OK.value(), "Trimestre clôturé"));
        return ResponseEntity.ok(reponse);
    }
}