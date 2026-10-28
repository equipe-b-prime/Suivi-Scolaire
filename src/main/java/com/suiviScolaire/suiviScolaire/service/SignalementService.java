package com.suiviScolaire.suiviScolaire.service;

import com.suiviScolaire.suiviScolaire.entity.Eleve;
import com.suiviScolaire.suiviScolaire.entity.Periode;
import com.suiviScolaire.suiviScolaire.repository.EleveRepository;
import com.suiviScolaire.suiviScolaire.repository.PeriodeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service de détection des élèves en difficulté.
 * Exécuté chaque semaine pour signaler les élèves dont la moyenne chute.
 */
@Service
public class SignalementService {

    private final EleveRepository eleveRepository;
    private final PeriodeRepository periodeRepository;

    public SignalementService(EleveRepository eleveRepository,
                              PeriodeRepository periodeRepository) {
        this.eleveRepository = eleveRepository;
        this.periodeRepository = periodeRepository;
    }

    /**
     * Tâche planifiée exécutée chaque semaine (le lundi à 8h00).
     * Signale les élèves en difficulté : moyenne < 8 ou écart > 4 points.
     * Un élève déjà signalé ce trimestre ne l'est pas deux fois.
     */
    @Scheduled(cron = "0 0 8 * * MON")
    public void detecterElevesDifficulte() {
        // Récupérer le trimestre en cours (le plus récent clôturé ou en cours)
        Periode trimestreEnCours = periodeRepository.findTopByOrderByNumeroDesc()
                .orElse(null);

        if (trimestreEnCours == null) {
            return;
        }

        Integer numeroTrimestre = trimestreEnCours.getNumero();
        List<Eleve> eleves = eleveRepository.findAll();

        for (Eleve eleve : eleves) {
            // Vérifier si l'élève a déjà été signalé ce trimestre
            if (Boolean.TRUE.equals(eleve.getEstSigle()) && eleve.getTrimestreSigle() != null && eleve.getTrimestreSigle() == numeroTrimestre) {
                continue; // Déjà signalé ce trimestre, on saute
            }

            // Règles de signalement :
            // - Moyenne générale du trimestre en cours < 8
            // - Ou l'écart a chuté de plus de 4 points depuis le trimestre précédent
            boolean signaler = false;
            String raison = "";

            if (eleve.getMoyenneTrimestreCourant() != null
                    && eleve.getMoyenneTrimestreCourant().compareTo(BigDecimal.valueOf(8)) < 0) {
                signaler = true;
                raison = "Moyenne générale du trimestre " + numeroTrimestre + " inférieure à 8 (" + eleve.getMoyenneTrimestreCourant() + ")";
            }

            if (eleve.getEcartTrimestrePrec() != null
                    && eleve.getEcartTrimestrePrec().compareTo(BigDecimal.valueOf(4)) > 0) {
                signaler = true;
                raison = raison + " (écart trimestre précédent de " + eleve.getEcartTrimestrePrec() + " points > 4)";
            }

            if (signaler) {
                // Signaler l'élève
                eleve.setEstSigle(true);
                eleve.setTrimestreSigle(numeroTrimestre);
                eleveRepository.save(eleve);

                // Ici, on pourrait produire une alerte (log, email, etc.)
                // Pour l'instant, on laisse un log implicite via le save
            }
        }
    }
}