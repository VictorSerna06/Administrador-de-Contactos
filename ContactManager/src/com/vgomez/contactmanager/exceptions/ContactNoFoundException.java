package com.vgomez.contactmanager.exceptions;

public class ContactNoFoundException extends RuntimeException{

    public ContactNoFoundException(String mensaje){
        super(mensaje);
    }
}
