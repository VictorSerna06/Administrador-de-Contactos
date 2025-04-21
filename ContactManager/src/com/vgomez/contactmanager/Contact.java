package com.vgomez.contactmanager;

public class Contact {

    // Atributos
    private String name;
    private String email;
    private String phone;

    // Constructor que inicializa un contacto con los datos proporcionados
    public Contact(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Devuelve una representación en texto del contacto
    @Override
    public String toString() {
        return String.format("Nombre: %s \nEmail: %s \nTeléfono: %s\n", name, email, phone);
    }
}
