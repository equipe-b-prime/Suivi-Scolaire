package com.suiviScolaire.suiviScolaire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.suiviScolaire.suiviScolaire.entity.Classe;
import com.suiviScolaire.suiviScolaire.repository.ClasseRepository;
import com.suiviScolaire.suiviScolaire.dto.ClasseRequestDto;
import com.suiviScolaire.suiviScolaire.dto.ClasseResponseDto;

/**
 * Service métier gérant les classes.
 * Fournit les opérations CRUD ainsi que les conversions entité/DTO
 * et le calcul de l'effectif réel (requêtes batch, sans JOIN).
 */
@Service
@RequiredArgsConstructor
public class ClasseService {

    private final ClasseRepository classeRepository;

    /**
     * Convertit une entité classe en objet de réponse (sans effectif réel).
     *
     * @param classe L'entité classe à convertir.
     * @return ClasseResponseDto Les détails de la classe convertie.
     */
    public ClasseResponseDto toDto(Classe classe) {
        return ClasseResponseDto.builder()
                .id(classe.getId())
                .nom(classe.getNom())
                .niveau(classe.getNiveau())
                .effectifMax(classe.getEffectifMax())
                .build();
    }

    /**
     * Convertit les données d'une requête en entité classe.
     *
     * @param classeRequest Les données de la requête pour créer la classe.
     * @return Classe L'entité classe créée.
     */
    public Classe toEntity(ClasseRequestDto classeRequest) {
        return Classe.builder()
                .nom(classeRequest.getNom())
                .niveau(classeRequest.getNiveau())
                .effectifMax(classeRequest.getEffectifMax())
                .build();
    }

    /**
     * Crée une nouvelle classe.
     *
     * @param classeRequest les informations de la classe à créer
     * @return la classe créée
     */
    public ClasseResponseDto createClasse(ClasseRequestDto classeRequest) {
        return toDto(classeRepository.save(toEntity(classeRequest)));
    }

    /**
     * Récupère une classe à partir de son identifiant, avec son effectif réel.
     *
     * @param id L'identifiant de la classe à récupérer.
     * @return ClasseResponseDto Les détails de la classe récupérée.
     */
    public ClasseResponseDto getClasseById(Long id) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classe non trouvée"));
        long effectifReel = classeRepository.countElevesByClasse(id);
        return toDtoWithEffectif(classe, effectifReel);
    }

    /**
     * Récupère la liste de toutes les classes, avec leur effectif réel (batch).
     *
     * @return List<ClasseResponseDto> La liste des classes récupérées.
     */
    public List<ClasseResponseDto> getAllClasses() {
        List<Classe> classes = classeRepository.findAll();
        List<Long> ids = classes.stream().map(Classe::getId).toList();
        Map<Long, Long> effectifs = new HashMap<>();
        if (!ids.isEmpty()) {
            for (Object[] row : classeRepository.countElevesByClasseIds(ids)) {
                effectifs.put((Long) row[0], (Long) row[1]);
            }
        }
        return classes.stream()
                .map(classe -> toDtoWithEffectif(classe, effectifs.getOrDefault(classe.getId(), 0L)))
                .toList();
    }

    /**
     * Modifie une classe existante.
     *
     * @param id L'identifiant de la classe à modifier.
     * @param classeRequest Les données de la requête pour modifier la classe.
     * @return ClasseResponseDto Les détails de la classe modifiée.
     */
    public ClasseResponseDto updateClasseById(Long id, ClasseRequestDto classeRequest) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classe non trouvée"));
        classe.setNom(classeRequest.getNom());
        classe.setNiveau(classeRequest.getNiveau());
        classe.setEffectifMax(classeRequest.getEffectifMax());
        return toDto(classeRepository.save(classe));
    }

    /**
     * Supprime une classe à partir de son identifiant.
     *
     * @param id L'identifiant de la classe à supprimer.
     * @return ClasseResponseDto Les détails de la classe supprimée.
     */
    public ClasseResponseDto deleteClasseById(Long id) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classe non trouvée"));
        classeRepository.deleteById(id);
        return toDto(classe);
    }

    /**
     * Construit la réponse d'une classe en incluant son effectif réel.
     *
     * @param classe L'entité classe.
     * @param effectifReel L'effectif réel calculé.
     * @return ClasseResponseDto La réponse de la classe.
     */
    private ClasseResponseDto toDtoWithEffectif(Classe classe, long effectifReel) {
        return ClasseResponseDto.builder()
                .id(classe.getId())
                .nom(classe.getNom())
                .niveau(classe.getNiveau())
                .effectifMax(classe.getEffectifMax())
                .effectifReel(effectifReel)
                .build();
    }
}