package com.suiviScolaire.suiviScolaire.service;

import com.suiviScolaire.suiviScolaire.dto.MoyenneGeneraleResponseDto;
import com.suiviScolaire.suiviScolaire.entity.Classe;
import com.suiviScolaire.suiviScolaire.entity.Eleve;
import com.suiviScolaire.suiviScolaire.entity.Periode;
import com.suiviScolaire.suiviScolaire.repository.ClasseRepository;
import com.suiviScolaire.suiviScolaire.repository.EleveRepository;
import com.suiviScolaire.suiviScolaire.repository.InscriptionRepository;
import com.suiviScolaire.suiviScolaire.repository.PeriodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service de détection des élèves en difficulté.
 * Exécuté chaque semaine pour signaler les élèves dont la moyenne chute.
 */
@Service
public class SignalementService {

    private static final Logger log = LoggerFactory.getLogger(SignalementService.class);

    private final EleveRepository eleveRepository;
    private final PeriodeRepository periodeRepository;
    private final ClasseRepository classeRepository;
    private final InscriptionRepository inscriptionRepository;
    private final NoteService noteService;

    public SignalementService(EleveRepository eleveRepository,
                              PeriodeRepository periodeRepository,
                              ClasseRepository classeRepository,
                              InscriptionRepository inscriptionRepository,
                              NoteService noteService) {
        this.eleveRepository = eleveRepository;
        this.periodeRepository = periodeRepository;
        this.classeRepository = classeRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.noteService = noteService;
    }

    /**
     * Tâche planifiée exécutée chaque semaine (le lundi à 8h00).
     * Signale les élèves en difficulté : moyenne générale < 8 ou chute > 4 points depuis le trimestre précédent.
     * Produit une alerte par classe avec la moyenne et l'écart constaté.
     * Un élève déjà signalé ce trimestre ne l'est pas deux fois.
     */
    @Scheduled(cron = "0 0 8 * * MON")
    public void detecterElevesDifficulte() {
        Periode trimestreEnCours = periodeRepository.findTopByOrderByNumeroDesc()
                .orElse(null);

        if (trimestreEnCours == null) {
            return;
        }

        Integer numeroTrimestre = trimestreEnCours.getNumero();
        List<Classe> classes = classeRepository.findAll();

        for (Classe classe : classes) {
            List<Eleve> elevesClasse = inscriptionRepository.findElevesByClasseId(classe.getId());
            for (Eleve eleve : elevesClasse) {
                if (Boolean.TRUE.equals(eleve.getEstSigle())
                        && eleve.getTrimestreSigle() != null
                        && eleve.getTrimestreSigle().equals(numeroTrimestre)) {
                    continue;
                }

                MoyenneGeneraleResponseDto moyCouranteDto = noteService.getMoyenneGenerale(eleve.getId(), numeroTrimestre);
                BigDecimal moyCourante = moyCouranteDto.getMoyenneGenerale();
                BigDecimal ecart = null;

                if (moyCourante != null && numeroTrimestre > 1) {
                    MoyenneGeneraleResponseDto moyPrecDto = noteService.getMoyenneGenerale(eleve.getId(), numeroTrimestre - 1);
                    if (moyPrecDto.getMoyenneGenerale() != null) {
                        ecart = moyPrecDto.getMoyenneGenerale().subtract(moyCourante);
                    }
                }

                boolean signaler = false;
                if (moyCourante != null && moyCourante.compareTo(BigDecimal.valueOf(8)) < 0) {
                    signaler = true;
                }
                if (ecart != null && ecart.compareTo(BigDecimal.valueOf(4)) > 0) {
                    signaler = true;
                }

                if (signaler) {
                    eleve.setMoyenneTrimestreCourant(moyCourante);
                    eleve.setEcartTrimestrePrec(ecart);
                    eleve.setEstSigle(true);
                    eleve.setTrimestreSigle(numeroTrimestre);
                    eleveRepository.save(eleve);

                    log.warn("[ALERTE CLASSE {}] Élève {} {} en difficulté au trimestre {} : moyenne = {}, écart = {}",
                            classe.getNom(), eleve.getNom(), eleve.getPrenom(), numeroTrimestre, moyCourante, ecart);
                }
            }
        }
    }
}