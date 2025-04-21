package com.vgomez.contactmanager.exceptions;

public class NoContactException extends RuntimeException{

    public NoContactException(String mensaje){
        super(mensaje);
    }
}
