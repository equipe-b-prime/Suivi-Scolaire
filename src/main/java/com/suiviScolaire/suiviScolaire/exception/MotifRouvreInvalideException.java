package com.suiviScolaire.suiviScolaire.exception;

/**
 * Exception levée lorsqu'un motif de réouverture est requis ou trop court.
 */
public class MotifRouvreInvalideException extends RuntimeException {

    public MotifRouvreInvalideException(String message) {
        super(message);
    }
}