package com.suiviScolaire.suiviScolaire.exception;

/**
 * Exception levée lorsqu'on tente de modifier le poids alors qu'un trimestre est clôturé.
 */
public class TrimestreClotureException extends RuntimeException {

    public TrimestreClotureException(String message) {
        super(message);
    }
}