package com.vgomez.contactmanager;

import com.vgomez.contactmanager.exceptions.ContactNoFoundException;
import com.vgomez.contactmanager.exceptions.DeleteFailedContact;
import com.vgomez.contactmanager.exceptions.EmptyException;
import com.vgomez.contactmanager.exceptions.NoContactException;
import com.vgomez.contactmanager.validator.Validator;

import java.util.Scanner;

public class Main {

    // Instancia única de Scanner para la entrada de datos por consola
    private static final Scanner S = new Scanner(System.in);

    // Mensaje de bienvenida que se muestra al iniciar el programa
    private static final String BIENVENIDA = """
            ******************* ¡Bienvenido! *******************
            ContactManager te ayuda a administrar tus contactos
            """;

    // Menú principal con las opciones disponibles para el usuario
    private static final String MENU = """
            Elige una opción:
                            
            1.- Mostrar todos los contactos
            2.- Guardar contacto
            3.- Buscar contacto
            4.- Editar contacto
            5.- Eliminar contacto
            6.- Salir
            """;

    private static final String CHARACTER = "**********************************************************";

    public static void main(String[] args) {

        // Instancia ContactService
        ContactService contactService = new ContactService();

        System.out.println(BIENVENIDA);

        // Inicia el programa
        mostrarMenu(contactService);
    }

    /*
    Muestra el menú principal de opciones y gestiona la interacción con el usuario
    Utiliza un bucle para recibir entradas hasta que el usuario decida salir
    */
    private static void mostrarMenu(ContactService contactService){

        System.out.println(MENU);
        try {
            while (true) {
                String optionMenu = S.nextLine();
                switch (optionMenu) {
                    case "1":
                        System.out.println();
                        listContacts(contactService);
                        break;
                    case "2":
                        contactValidate(contactService);
                        break;
                    case "3":
                        System.out.println();
                        searchContact(contactService);
                        break;
                    case "4":
                        System.out.println();
                        updateContact(contactService);
                        break;
                    case "5":
                        System.out.println();
                        deleteContact(contactService);
                        break;
                    case "6":
                    case "salir":
                        System.out.println("Programa finalizado.");
                        System.exit(0);
                }
                System.out.println();
                System.out.println(MENU);
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

    }

    /*
    Muestra todos los usuarios almacenados en consola
    Si no hay contactos o ocurre un error, se muestra un mensaje apropiado
    */
    private static void listContacts(ContactService contactService) {
        try {
            if (!contactService.getAllContacts().isEmpty()) {
                System.out.println(CHARACTER);
                System.out.println("Lista de contactos:\n");
                contactService.getAllContacts().forEach(System.out::println);
                System.out.println(CHARACTER);
            }
        } catch (ContactNoFoundException e) {
            System.out.println(CHARACTER);
            System.out.println(e.getMessage());
            System.out.println(CHARACTER);
        }
    }

    //Solicita y valida los datos del usuario antes de crear un nuevo contacto
    public static void contactValidate(ContactService contactService) {

        while (true) {
            System.out.println();
            System.out.println("Ingresa el nombre del contacto:");
            String name = S.nextLine();
            System.out.println("Ingresa el email con el formato correcto:");
            String email = S.nextLine().trim();
            System.out.println("Ingresa el número de teléfono a 10 dígitos:");
            String number = S.nextLine().trim();

            if (name.isEmpty() || email.isEmpty() || number.isEmpty()) {
                System.out.println(CHARACTER);
                System.out.println("Ningún campo debe estar vacío, intentalo de nuevo.");
                System.out.println(CHARACTER);
                continue;
            } else if (!Validator.isValidEmail(email)) {
                System.out.println(CHARACTER);
                System.out.println("El email no es valido, intentalo de nuevo.");
                System.out.println(CHARACTER);
                continue;
            } else if (!Validator.isValidPhone(number)) {
                System.out.println(CHARACTER);
                System.out.println("El número de teléfono no es valido, intentalo de nuevo.");
                System.out.println(CHARACTER);
                continue;
            } else if (!Validator.isValidName(name)) {
                System.out.println(CHARACTER);
                System.out.println("El nombre no es valido, intentalo de nuevo.");
                System.out.println(CHARACTER);
                continue;
            } else {
                try {
                    Contact contact = new Contact(name, email, number);
                    contactService.addContact(contact);
                    System.out.println();
                    System.out.println(CHARACTER);
                    System.out.println("Contacto guardado.");
                    System.out.println(CHARACTER);
                } catch (NoContactException e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
        }
    }

    /*
    Permite al usuario buscar un contacto por nombre
    Muestra los detalles si se encuentra o un mensaje si no existe
    */
    public static void searchContact(ContactService contactService) {

        while (true) {
            System.out.println("Ingresa el nombre del contacto que deseas buscar:");
            String name = S.nextLine();
            if (name.isEmpty()) {
                System.out.println(CHARACTER);
                System.out.println("El campo nombre no puede estar vacío, intentalo de nuevo.");
                System.out.println(CHARACTER);
                System.out.println();
                continue;
            }
            Contact found = contactService.searchByName(name);
            if (found == null) {
                System.out.println();
                System.out.println(CHARACTER);
                System.out.println("El contacto '" + name + "' no fue encontrado, intentalo de nuevo");
                System.out.println(CHARACTER);
                System.out.println();
                continue;
            } else {
                try {
                    System.out.println();
                    System.out.println(CHARACTER);
                    System.out.println("Contacto encontrado:");
                    System.out.println();
                    System.out.println(found);
                    System.out.println(CHARACTER);
                    break;
                } catch (NoContactException e) {
                    System.out.println(CHARACTER);
                    System.out.println(e.getMessage());
                    System.out.println(CHARACTER);
                }

            }
        }
    }

    /*
    Permite modificar un campo especifico de un contacto existente
    Solicita nombre del contacto, campo a modificar y el nuevo valor
    */
    public static void updateContact(ContactService contactService) {
        System.out.println("Ingresa el nombre del contacto que deseas actualizar:");
        String name = S.nextLine();
        if (name.isEmpty()) {
            System.out.println(CHARACTER);
            System.out.println("El campo nombre no puede estar vacío");
            System.out.println(CHARACTER);
        } else {
            while (true) {
                System.out.println("Ingresa el campo que deseas editar (name | email | phone):");
                String field = S.nextLine();
                if (field.equalsIgnoreCase("name") || field.equalsIgnoreCase("email") || field.equalsIgnoreCase("phone")) {
                    System.out.println("Ingresa el nuevo dato:");
                    String updateData = S.nextLine();
                    if (updateData.isEmpty()) {
                        System.out.println(CHARACTER);
                        System.out.println("Ningún campo puede estar vacío.");
                        System.out.println(CHARACTER);
                        continue;
                    }
                    try {
                        boolean isuptade = contactService.edit(name, field, updateData);
                        if (isuptade) {
                            System.out.println();
                            System.out.println(CHARACTER);
                            System.out.println("Contacto actualizado.");
                            System.out.println(CHARACTER);
                            break;
                        } else {
                            System.out.println();
                            System.out.println(CHARACTER);
                            System.out.println("Contacto no encontrado.");
                            System.out.println(CHARACTER);
                            break;
                        }
                    } catch (NoContactException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                } else {
                    System.out.println();
                    System.out.println(CHARACTER);
                    System.out.println("Campo invalido, vuelve a intentarlo.");
                    System.out.println(CHARACTER);
                    System.out.println();
                }
            }
        }
    }

    /*
    Elimina un contacto del sistema de acuerdo al nombre proporcionado por el usuario
    Maneja errores si el nombre está vacío o si no se puede eliminar el contacto
    */
    public static void deleteContact(ContactService contactService) {
        System.out.println("Ingresa el nombre del contacto que deseas eliminar:");
        String name = S.nextLine();
        try {
            boolean isRemove = contactService.removeByName(name);
            if (isRemove) {
                System.out.println();
                System.out.println(CHARACTER);
                System.out.println("Contacto eliminado.");
                System.out.println(CHARACTER);
            } else {
                System.out.println();
                System.out.println(CHARACTER);
                System.out.println("Contacto '" + name + "' no encontrado.");
                System.out.println(CHARACTER);
            }
        } catch (EmptyException e) {
            System.out.println(CHARACTER);
            System.out.println("El nombre de usuario no puede estar vacío.");
            System.out.println(CHARACTER);
        } catch (DeleteFailedContact e) {
            System.out.println(CHARACTER);
            System.out.println("El campo nombre esta vacío, por lo que el contacto\n no fue encontrado y no fue posible eliminar.");
            System.out.println(CHARACTER);
        }
    }
}
