package com.suiviScolaire.suiviScolaire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.suiviScolaire.suiviScolaire.repository.EnseignementRepository;
import com.suiviScolaire.suiviScolaire.entity.Matiere;
import com.suiviScolaire.suiviScolaire.dto.MatiereResponseDto;
import com.suiviScolaire.suiviScolaire.entity.Professeur;
import com.suiviScolaire.suiviScolaire.repository.ProfesseurRepository;
import com.suiviScolaire.suiviScolaire.dto.ProfesseurRequestDto;
import com.suiviScolaire.suiviScolaire.dto.ProfesseurResponseDto;

/**
 * Service métier gérant les professeurs.
 * Fournit les opérations CRUD ainsi que les conversions entité/DTO
 * et la récupération des matières enseignées (requêtes batch, sans JOIN).
 */
@Service
@RequiredArgsConstructor
public class ProfesseurService {

    private final ProfesseurRepository professeurRepository;
    private final EnseignementRepository enseignementRepository;
    private final MatiereService matiereService;

    /**
     * Convertit une entité professeur en objet de réponse (sans matières).
     *
     * @param professeur L'entité professeur à convertir.
     * @return ProfesseurResponseDto Les détails du professeur converti.
     */
    public ProfesseurResponseDto toDto(Professeur professeur) {
        return ProfesseurResponseDto.builder()
                .id(professeur.getId())
                .nom(professeur.getNom())
                .prenom(professeur.getPrenom())
                .email(professeur.getEmail())
                .build();
    }

    /**
     * Convertit les données d'une requête en entité professeur.
     *
     * @param professeurRequest Les données de la requête pour créer le professeur.
     * @return Professeur L'entité professeur créée.
     */
    public Professeur toEntity(ProfesseurRequestDto professeurRequest) {
        return Professeur.builder()
                .nom(professeurRequest.getNom())
                .prenom(professeurRequest.getPrenom())
                .email(professeurRequest.getEmail())
                .build();
    }

    /**
     * Crée un nouveau professeur.
     *
     * @param professeurRequest les informations du professeur à créer
     * @return le professeur créé
     */
    public ProfesseurResponseDto createProfesseur(ProfesseurRequestDto professeurRequest) {
        return toDto(professeurRepository.save(toEntity(professeurRequest)));
    }

    /**
     * Récupère un professeur à partir de son identifiant, avec ses matières.
     *
     * @param id L'identifiant du professeur à récupérer.
     * @return ProfesseurResponseDto Les détails du professeur récupéré.
     */
    public ProfesseurResponseDto getProfesseurById(Long id) {
        Professeur professeur = professeurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professeur non trouvé"));
        List<MatiereResponseDto> matieres = professeurRepository.findMatieresByProfesseur(id).stream()
                .map(matiereService::toDto)
                .toList();
        return toDtoWithMatieres(professeur, matieres);
    }

    /**
     * Récupère la liste de tous les professeurs, avec leurs matières (batch).
     *
     * @return List<ProfesseurResponseDto> La liste des professeurs récupérés.
     */
    public List<ProfesseurResponseDto> getAllProfesseurs() {
        List<Professeur> professeurs = professeurRepository.findAll();
        List<Long> ids = professeurs.stream().map(Professeur::getId).toList();
        Map<Long, List<MatiereResponseDto>> matieres = new HashMap<>();
        if (!ids.isEmpty()) {
            for (Object[] row : enseignementRepository.findMatiereByProfesseurIds(ids)) {
                Long professeurId = (Long) row[0];
                matieres.computeIfAbsent(professeurId, k -> new ArrayList<>())
                        .add(matiereService.toDto((Matiere) row[1]));
            }
        }
        return professeurs.stream()
                .map(professeur -> toDtoWithMatieres(professeur, matieres.get(professeur.getId())))
                .toList();
    }

    /**
     * Modifie un professeur existant.
     *
     * @param id L'identifiant du professeur à modifier.
     * @param professeurRequest Les données de la requête pour modifier le professeur.
     * @return ProfesseurResponseDto Les détails du professeur modifié.
     */
    public ProfesseurResponseDto updateProfesseurById(Long id, ProfesseurRequestDto professeurRequest) {
        Professeur professeur = professeurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professeur non trouvé"));
        professeur.setNom(professeurRequest.getNom());
        professeur.setPrenom(professeurRequest.getPrenom());
        professeur.setEmail(professeurRequest.getEmail());
        return toDto(professeurRepository.save(professeur));
    }

    /**
     * Supprime un professeur à partir de son identifiant.
     *
     * @param id L'identifiant du professeur à supprimer.
     * @return ProfesseurResponseDto Les détails du professeur supprimé.
     */
    public ProfesseurResponseDto deleteProfesseurById(Long id) {
        Professeur professeur = professeurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professeur non trouvé"));
        professeurRepository.deleteById(id);
        return toDto(professeur);
    }

    /**
     * Construit la réponse d'un professeur en incluant ses matières.
     *
     * @param professeur L'entité professeur.
     * @param matieres Les matières (peut être null).
     * @return ProfesseurResponseDto La réponse du professeur.
     */
    private ProfesseurResponseDto toDtoWithMatieres(Professeur professeur, List<MatiereResponseDto> matieres) {
        return ProfesseurResponseDto.builder()
                .id(professeur.getId())
                .nom(professeur.getNom())
                .prenom(professeur.getPrenom())
                .email(professeur.getEmail())
                .matieres(matieres)
                .build();
    }
}