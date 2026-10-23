package com.suiviScolaire.suiviScolaire.utils.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Enveloppe standard des réponses HTTP de l'application.
 *
 * @param <T> le type des données retournées
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "nombre", "totalElements", "totalPages", "hasError", "data"})
public class Response<T> {
    private Status status;
    private boolean hasError;
    private List<T> data;
    private Long nombre;

    /**
     * Nombre total d'éléments disponibles (uniquement renseigné pour les réponses paginées).
     */
    private Long totalElements;

    /**
     * Nombre total de pages disponibles (uniquement renseigné pour les réponses paginées).
     */
    private Long totalPages;

    /**
     * Constructeur pour une liste de résultats.
     *
     * @param data la liste des données retournées
     */
    public Response(List<T> data) {
        this.status = new Status(800, "Opération effectuée avec succès");
        this.hasError = false;
        this.nombre = (long) data.size();
        this.data = data;
    }

    /**
     * Constructeur pour un résultat unique.
     *
     * @param data l'unique donnée retournée
     */
    public Response(T data) {
        this.data = List.of(data);
        this.status = new Status(800, "Opération effectuée avec succès");
        this.hasError = false;
        this.nombre = 1L;
    }
}
