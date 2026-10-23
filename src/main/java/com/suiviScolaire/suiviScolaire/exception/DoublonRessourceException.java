package com.suiviScolaire.suiviScolaire.exception;

/**
 * Exception levée lorsqu'une ressource enfreint une contrainte d'unicité métier
 * (par exemple deux classes de même nom dans un même niveau, ou un email déjà utilisé).
 */
public class DoublonRessourceException extends RuntimeException {

    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message décrivant le doublon détecté
     */
    public DoublonRessourceException(String message) {
        super(message);
    }
}
