package com.vgomez.contactmanager;

import com.vgomez.contactmanager.exceptions.ContactNoFoundException;
import com.vgomez.contactmanager.exceptions.DeleteFailedContact;
import com.vgomez.contactmanager.exceptions.EmptyException;
import com.vgomez.contactmanager.exceptions.NoContactException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContactService {

    // Atributo
    private List<Contact> contactList;

    // Constructor
    public ContactService() {
        contactList = new ArrayList<>();
    }

    // Agrega un contacto nuevo a la lista
    public void addContact(Contact contact) {
        contactList.add(contact);
    }

    // Retorna todos los contactos almacenados
    public List<Contact> getAllContacts() {

        if (!contactList.isEmpty()) {

            contactList.sort(ContactService::order);
        } else {
            throw new ContactNoFoundException("La lista de contactos está vacía.");
        }

        return contactList;
    }

    // Compara cada uno de los contactos para ordenarlos alfabéticamente
    public static int order(Contact c1, Contact c2) {
        return c1.getName().compareTo(c2.getName());
    }

    // Buscar contacto por nombre
    public Contact searchByName(String name) {

        if (!name.isEmpty()) {
            for (Contact contact : contactList) {
                if (contact.getName().equalsIgnoreCase(name)) {
                    return contact;
                }
            }
        } else if (name == null) {
            throw new EmptyException("El nombre de usuario no puede estar vacío");
        } else {
            throw new NoContactException("No se encontró en la lista el contacto: " + name);
        }
        return null;
    }

    // Eliminar contacto por nombre
    public boolean removeByName(String name) {
        if (!name.isEmpty()) {
            Iterator<Contact> iterator = contactList.iterator();
            while (iterator.hasNext()) {
                Contact contact = iterator.next();
                if (contact.getName().equalsIgnoreCase(name)) {
                    iterator.remove();
                    return true;
                }
            }
        } else {
            throw new DeleteFailedContact("No es posible realizar la operación ya que no se encontró el contacto con el nombre: " + name);
        }
        return false;
    }

    // Editar un campo específico de un contacto existente
    public boolean edit(String name, String field, String updateData) {

        if (name == null || name.isEmpty()) return false;

        Contact contact = searchByName(name);
        if (contact == null) return false;

        switch (field) {
            case "name":
                contact.setName(updateData);
                return true;
            case "email":
                contact.setEmail(updateData);
                return true;
            case "phone":
                contact.setPhone(updateData);
                return true;
            default:
                throw new IllegalArgumentException("Campo no valido: " + field);
        }
    }
}
