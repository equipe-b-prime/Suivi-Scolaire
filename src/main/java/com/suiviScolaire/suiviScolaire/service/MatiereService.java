package com.suiviScolaire.suiviScolaire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.suiviScolaire.suiviScolaire.entity.Coefficient;
import com.suiviScolaire.suiviScolaire.repository.CoefficientRepository;
import com.suiviScolaire.suiviScolaire.dto.CoefficientResponseDto;
import com.suiviScolaire.suiviScolaire.entity.Matiere;
import com.suiviScolaire.suiviScolaire.repository.MatiereRepository;
import com.suiviScolaire.suiviScolaire.dto.MatiereRequestDto;
import com.suiviScolaire.suiviScolaire.dto.MatiereResponseDto;

/**
 * Service métier gérant les matières.
 * Fournit les opérations CRUD ainsi que les conversions entité/DTO
 * et la récupération des coefficients (requêtes batch, sans JOIN).
 */
@Service
@RequiredArgsConstructor
public class MatiereService {

    private final MatiereRepository matiereRepository;
    private final CoefficientRepository coefficientRepository;
    private final CoefficientService coefficientService;

    /**
     * Convertit une entité matière en objet de réponse (sans coefficients).
     *
     * @param matiere L'entité matière à convertir.
     * @return MatiereResponseDto Les détails de la matière convertie.
     */
    public MatiereResponseDto toDto(Matiere matiere) {
        return MatiereResponseDto.builder()
                .id(matiere.getId())
                .nom(matiere.getNom())
                .build();
    }

    /**
     * Convertit les données d'une requête en entité matière.
     *
     * @param matiereRequest Les données de la requête pour créer la matière.
     * @return Matiere L'entité matière créée.
     */
    public Matiere toEntity(MatiereRequestDto matiereRequest) {
        return Matiere.builder()
                .nom(matiereRequest.getNom())
                .build();
    }

    /**
     * Crée une nouvelle matière.
     *
     * @param matiereRequest les informations de la matière à créer
     * @return la matière créée
     */
    public MatiereResponseDto createMatiere(MatiereRequestDto matiereRequest) {
        return toDto(matiereRepository.save(toEntity(matiereRequest)));
    }

    /**
     * Récupère une matière à partir de son identifiant, avec ses coefficients.
     *
     * @param id L'identifiant de la matière à récupérer.
     * @return MatiereResponseDto Les détails de la matière récupérée.
     */
    public MatiereResponseDto getMatiereById(Long id) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
        List<CoefficientResponseDto> coefficients = matiereRepository.findCoefficientsByMatiere(id).stream()
                .map(coefficientService::toDto)
                .toList();
        return toDtoWithCoefficients(matiere, coefficients);
    }

    /**
     * Récupère la liste de toutes les matières, avec leurs coefficients (batch).
     *
     * @return List<MatiereResponseDto> La liste des matières récupérées.
     */
    public List<MatiereResponseDto> getAllMatieres() {
        List<Matiere> matieres = matiereRepository.findAll();
        List<Long> ids = matieres.stream().map(Matiere::getId).toList();
        Map<Long, List<CoefficientResponseDto>> coefficients = new HashMap<>();
        if (!ids.isEmpty()) {
            for (Object[] row : coefficientRepository.findCoefficientsByMatiereIds(ids)) {
                Long matiereId = (Long) row[0];
                coefficients.computeIfAbsent(matiereId, k -> new ArrayList<>())
                        .add(coefficientService.toDto((Coefficient) row[1]));
            }
        }
        return matieres.stream()
                .map(matiere -> toDtoWithCoefficients(matiere, coefficients.get(matiere.getId())))
                .toList();
    }

    /**
     * Modifie une matière existante.
     *
     * @param id L'identifiant de la matière à modifier.
     * @param matiereRequest Les données de la requête pour modifier la matière.
     * @return MatiereResponseDto Les détails de la matière modifiée.
     */
    public MatiereResponseDto updateMatiereById(Long id, MatiereRequestDto matiereRequest) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
        matiere.setNom(matiereRequest.getNom());
        return toDto(matiereRepository.save(matiere));
    }

    /**
     * Supprime une matière à partir de son identifiant.
     *
     * @param id L'identifiant de la matière à supprimer.
     * @return MatiereResponseDto Les détails de la matière supprimée.
     */
    public MatiereResponseDto deleteMatiereById(Long id) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
        matiereRepository.deleteById(id);
        return toDto(matiere);
    }

    /**
     * Construit la réponse d'une matière en incluant ses coefficients.
     *
     * @param matiere L'entité matière.
     * @param coefficients Les coefficients (peut être null).
     * @return MatiereResponseDto La réponse de la matière.
     */
    private MatiereResponseDto toDtoWithCoefficients(Matiere matiere, List<CoefficientResponseDto> coefficients) {
        return MatiereResponseDto.builder()
                .id(matiere.getId())
                .nom(matiere.getNom())
                .coefficients(coefficients)
                .build();
    }
}