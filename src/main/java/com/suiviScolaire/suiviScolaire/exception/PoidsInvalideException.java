package com.suiviScolaire.suiviScolaire.exception;

/**
 * Exception levée lorsqu'un poids de type d'évaluation est invalide.
 */
public class PoidsInvalideException extends RuntimeException {

    public PoidsInvalideException(String message) {
        super(message);
    }
}