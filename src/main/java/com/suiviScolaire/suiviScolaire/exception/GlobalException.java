package com.suiviScolaire.suiviScolaire.exception;

import com.suiviScolaire.suiviScolaire.utils.response.Response;
import com.suiviScolaire.suiviScolaire.utils.response.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestionnaire global des exceptions de l'application.
 * Centralise le format des réponses d'erreur (validation, ressource introuvable, conflit métier).
 */
@RestControllerAdvice
public class GlobalException {

    /**
     * Gère les erreurs de validation des requêtes (@Valid).
     *
     * @param ex L'exception de validation.
     * @return ResponseEntity<Response<Map<String,String>>> La liste des erreurs par champ.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<Map<String, String>> listeDerreurs = new ArrayList<>();
        for (FieldError error : ex.getFieldErrors()) {
            Map<String, String> detailErreur = new HashMap<>();
            detailErreur.put("champ", error.getField());
            detailErreur.put("message", error.getDefaultMessage());
            listeDerreurs.add(detailErreur);
        }
        Response<Map<String, String>> response = new Response<>();
        response.setData(listeDerreurs);
        response.setStatus(new Status(HttpStatus.BAD_REQUEST.value(), "Erreur de validation"));
        response.setHasError(true);
        response.setNombre((long) listeDerreurs.size());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les erreurs de ressource introuvable.
     *
     * @param ex L'exception de ressource introuvable.
     * @return ResponseEntity<Response<String>> Le message d'erreur encapsulé.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response<String>> notFoundException(NotFoundException ex) {
        Response<String> reponse = new Response<>(ex.getMessage());
        reponse.setNombre(1L);
        reponse.setHasError(true);
        reponse.setStatus(new Status(HttpStatus.NOT_FOUND.value(), "l'operation a echouée"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(reponse);
    }

    /**
     * Gère les paramètres de requête dont le type ne correspond pas à celui attendu.
     *
     * @param ex L'exception signalant une incompatibilité de type sur un paramètre.
     * @return ResponseEntity<Response<List<String>>> Le message d'erreur encapsulé.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Response<List<String>>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String typeAttendu = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "inconnu";
        switch (typeAttendu) {
            case "int", "Long", "Integer" -> typeAttendu = "entier";
            case "boolean", "Boolean" -> typeAttendu = "booléen";
            case "String" -> typeAttendu = "chaîne de caractères";
            default -> typeAttendu = "inconnu";
        }
        Response<List<String>> response = new Response<>(Collections.emptyList());
        response.setData(null);
        response.setStatus(new Status(HttpStatus.BAD_REQUEST.value(),
                "Le parametre '" + ex.getName() + "' doit etre de type " + typeAttendu));
        response.setHasError(true);
        response.setNombre(0L);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les paramètres de requête absents ou invalides.
     *
     * @param ex L'exception signalant un paramètre invalide.
     * @return ResponseEntity<Response<String>> Le message d'erreur encapsulé.
     */
    @ExceptionHandler(RequeteInvalideException.class)
    public ResponseEntity<Response<String>> handleRequeteInvalide(RequeteInvalideException ex) {
        Response<String> reponse = new Response<>(ex.getMessage());
        reponse.setNombre(1L);
        reponse.setHasError(true);
        reponse.setStatus(new Status(HttpStatus.BAD_REQUEST.value(), "Requête invalide"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reponse);
    }

    /**
     * Gère les violations de contrainte d'unicité métier.
     *
     * @param ex L'exception signalant un doublon de ressource.
     * @return ResponseEntity<Response<String>> Le message d'erreur encapsulé.
     */
    @ExceptionHandler(DoublonRessourceException.class)
    public ResponseEntity<Response<String>> handleDoublonRessource(DoublonRessourceException ex) {
        Response<String> reponse = new Response<>(ex.getMessage());
        reponse.setNombre(1L);
        reponse.setHasError(true);
        reponse.setStatus(new Status(HttpStatus.CONFLICT.value(), "Doublon de ressource"));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(reponse);
    }

    /**
     * Gère les suppressions refusées car la ressource est encore référencée.
     *
     * @param ex L'exception signalant une ressource encore utilisée.
     * @return ResponseEntity<Response<String>> Le message d'erreur encapsulé.
     */
    @ExceptionHandler(RessourceUtiliseeException.class)
    public ResponseEntity<Response<String>> handleRessourceUtilisee(RessourceUtiliseeException ex) {
        Response<String> reponse = new Response<>(ex.getMessage());
        reponse.setNombre(1L);
        reponse.setHasError(true);
        reponse.setStatus(new Status(HttpStatus.CONFLICT.value(), "Ressource utilisée"));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(reponse);
    }

    /**
     * Gère les règles métier violées (arguments invalides).
     *
     * @param ex L'exception signalant une règle métier non respectée.
     * @return ResponseEntity<Response<String>> Le message d'erreur encapsulé.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<String>> handleIllegalArgument(IllegalArgumentException ex) {
        Response<String> reponse = new Response<>(ex.getMessage());
        reponse.setNombre(1L);
        reponse.setHasError(true);
        reponse.setStatus(new Status(HttpStatus.CONFLICT.value(), "Règle métier non respectée"));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(reponse);
    }
}
