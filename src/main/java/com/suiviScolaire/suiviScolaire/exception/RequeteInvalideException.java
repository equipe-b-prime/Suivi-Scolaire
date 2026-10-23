package com.suiviScolaire.suiviScolaire.exception;

/**
 * Exception levée lorsqu'un paramètre de requête est absent ou invalide
 * (trimestre hors bornes, nom de recherche vide, tri non supporté...).
 */
public class RequeteInvalideException extends RuntimeException {

    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message décrivant le paramètre invalide
     */
    public RequeteInvalideException(String message) {
        super(message);
    }
}
