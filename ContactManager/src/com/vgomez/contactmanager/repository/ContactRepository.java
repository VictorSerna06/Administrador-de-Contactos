package com.vgomez.contactmanager.repository;

import com.vgomez.contactmanager.model.Contact;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactRepository {

    // Atributos
    private static final String FILE_PATH = "contacts.dat";

    // Método para cargar contactos desde el archivo
    public List<Contact> loadContacts(){

        File file = new File(FILE_PATH);

        if(!file.exists()){
            return new ArrayList<>();
        }

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
                return (List<Contact>) ois.readObject();

        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Método para guardar contactos en el archivo
    public void saveContacts(List<Contact> contacts){

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(contacts);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
