package com.example.progettopsw.support.exceptions;

public class ElementoNonTrovatoException extends RuntimeException {
    public ElementoNonTrovatoException() {
        super("Elemento non trovato!");
    }
}
