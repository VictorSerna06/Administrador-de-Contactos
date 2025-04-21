package com.vgomez.contactmanager.exceptions;

public class DeleteFailedContact extends RuntimeException{

    public DeleteFailedContact(String mensaje){
        super(mensaje);
    }
}
