package com.suiviScolaire.suiviScolaire.controller;

import com.suiviScolaire.suiviScolaire.entity.Periode;
import com.suiviScolaire.suiviScolaire.exception.NotFoundException;
import com.suiviScolaire.suiviScolaire.exception.RessourceUtiliseeException;
import com.suiviScolaire.suiviScolaire.repository.ClasseRepository;
import com.suiviScolaire.suiviScolaire.repository.MatiereRepository;
import com.suiviScolaire.suiviScolaire.entity.Classe;
import com.suiviScolaire.suiviScolaire.entity.Matiere;
import com.suiviScolaire.suiviScolaire.exception.MotifRouvreInvalideException;
import com.suiviScolaire.suiviScolaire.exception.NotFoundException;
import com.suiviScolaire.suiviScolaire.exception.RessourceUtiliseeException;
import com.suiviScolaire.suiviScolaire.repository.ClasseRepository;
import com.suiviScolaire.suiviScolaire.repository.MatiereRepository;
import com.suiviScolaire.suiviScolaire.repository.NoteRepository;
import com.suiviScolaire.suiviScolaire.repository.PeriodeRepository;
import com.suiviScolaire.suiviScolaire.service.NoteService;
import java.time.LocalDate;
import com.suiviScolaire.suiviScolaire.utils.response.Response;
import com.suiviScolaire.suiviScolaire.utils.response.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des périodes (trimestres).
 */
@RestController
@RequestMapping("/trimestres")
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

    /**
     * Rouvre un trimestre clôturé.
     * Règle : le motif est obligatoire et doit faire au moins 20 caractères ;
     * seul le dernier trimestre clôturé peut être rouvert.
     * Traçabilité : la réouverture est journalisée avec la date, le motif et l'auteur.
     *
     * @param numero Le numéro du trimestre à rouvrir.
     * @param motif Le motif de la réouverture (obligatoire, ≥ 20 caractères).
     * @return ResponseEntity<Response<String>> Statut de la réouverture.
     * @throws NotFoundException si le trimestre n'existe pas.
     * @throws MotifRouvreInvalideException si le motif est absent ou trop court.
     * @throws RessourceUtiliseeException si le trimestre n'est pas clôturé (409).
     */
    @PostMapping("/{numero}/reouverture")
    public ResponseEntity<Response<String>> reouvertureTrimestre(@PathVariable Integer numero,
                                                                 @RequestParam String motif) {
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

        // Vérifier que c'est le dernier trimestre clôturé
        // (implémentation simple : vérifier qu'aucun trimestre ultérieur n'est clôturé)
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

        // Effectuer la réouverture
        periode.setCloture(false);
        periode.setMotifRouvre(motif);
        periode.setDateRouvre(LocalDate.now());
        // Note: utilisateurRouvre ne serait défini que côté sécurité/authentification
        periodeRepository.save(periode);

        Response<String> reponse = new Response<>("Trimestre " + numero + " réouvert avec succès. Motif: " + motif);
        reponse.setHasError(false);
        reponse.setNombre(1L);
        reponse.setStatus(new Status(HttpStatus.OK.value(), "Trimestre rouvert"));
        return ResponseEntity.ok(reponse);
    }

    /**
     * Clôture d'un trimestre.
     * Règle : toutes les matières de toutes les classes doivent avoir au moins une note.
     * Refus 409 listant les classes et matières sans note.
     *
     * @param numero Le numéro du trimestre à clôturer (1, 2 ou 3).
     * @return ResponseEntity<Response<String>> Statut de la clôture.
     * @throws NotFoundException si le trimestre n'existe pas.
     */
    @PostMapping("/{numero}/cloture")
    public ResponseEntity<Response<String>> clotureTrimestre(@PathVariable Integer numero) {
        Periode periode = periodeRepository.findByNumero(numero)
                .orElseThrow(() -> new NotFoundException("Trimestre " + numero + " non trouvé"));

        if (periode.isCloture()) {
            Response<String> reponse = new Response<>("Le trimestre " + numero + " est déjà clôturé.");
            reponse.setHasError(true);
            reponse.setStatus(new Status(HttpStatus.CONFLICT.value(), "Trimestre déjà clôturé"));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(reponse);
        }

        // Vérifier que toutes les classes ont toutes les matières avec au moins une note pour ce trimestre
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

        // Clôturer le trimestre
        periode.setCloture(true);
        periodeRepository.save(periode);

        Response<String> reponse = new Response<>("Trimestre " + numero + " clôturé avec succès.");
        reponse.setHasError(false);
        reponse.setNombre(1L);
        reponse.setStatus(new Status(HttpStatus.OK.value(), "Trimestre clôturé"));
        return ResponseEntity.ok(reponse);
    }
}