package com.vgomez.contactmanager.exceptions;

public class EmptyException extends RuntimeException{

    public EmptyException(String mensaje){
        super(mensaje);
    }
}
