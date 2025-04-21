package com.vgomez.contactmanager.validator;

public class Validator {

    // Constantes que contienen los caracteres a validar en cada campo
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,7}$";
    private static final String PHONE_REGEX = "^\\d{10}$";

    // Valida que el correo electrónico tenga el formato correcto
    public static boolean isValidEmail(String email){
        if(email == null) return false;
        return email.matches(EMAIL_REGEX);
    }

    // Valida que número de teléfono tenga 10 dígitos numéricos
    public static boolean isValidPhone(String phone){
        if(phone == null) return false;
        return phone.matches(PHONE_REGEX);
    }

    // Valida que el nombre no esté en blanco
    public static boolean isValidName(String name){
        return name != null && !name.trim().isEmpty();
    }
}
