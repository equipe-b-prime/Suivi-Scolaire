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
@JsonPropertyOrder({"status", "nombre", "totalElements", "totalPages", "page", "size", "hasError", "data"})
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
     * Numéro de la page courante (0-indexé).
     */
    private int page;

    /**
     * Taille de la page actuelle.
     */
    private int size;

    /**
     * Constructeur pour une liste de résultats (pagination par défaut : page 0, taille = données).
     *
     * @param data la liste des données retournées
     */
    public Response(List<T> data) {
        this.status = new Status(800, "Opération effectuée avec succès");
        this.hasError = false;
        this.nombre = (long) data.size();
        this.data = data;
        this.page = 0;
        this.size = data.size() > 0 ? data.size() : 30;
        this.totalElements = (long) data.size();
        this.totalPages = 1L;
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
        this.page = 0;
        this.size = 1;
        this.totalElements = 1L;
        this.totalPages = 1L;
    }

    /**
     * Constructeur pour une réponse de pagination avec métadonnées complètes.
     *
     * @param content  la liste des données retournées
     * @param totalElements nombre total d'éléments dans la base de données
     * @param totalPages nombre total de pages
     * @param page numéro de la page courante (0-indexé)
     * @param size taille de la page courante
     */
    public Response(List<T> content, long totalElements, long totalPages, int page, int size) {
        this.status = new Status(800, "Opération effectuée avec succès");
        this.hasError = false;
        this.nombre = totalElements;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.data = content;
        this.page = page;
        this.size = size;
    }
}