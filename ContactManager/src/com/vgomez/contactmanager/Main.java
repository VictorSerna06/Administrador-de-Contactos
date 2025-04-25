package com.vgomez.contactmanager;

import com.vgomez.contactmanager.exceptions.ContactNoFoundException;
import com.vgomez.contactmanager.exceptions.DeleteFailedContact;
import com.vgomez.contactmanager.exceptions.EmptyException;
import com.vgomez.contactmanager.exceptions.NoContactException;
import com.vgomez.contactmanager.model.Contact;
import com.vgomez.contactmanager.service.ContactService;
import com.vgomez.contactmanager.validator.Validator;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
            6.- Filtrar contactos por letra inicial
            7.- Salir
            """;

    private static final String CHARACTER = "***************************************************************";

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
    private static void mostrarMenu(ContactService contactService) {

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
                        System.out.println();
                        filterByInitial(contactService);
                        break;
                    case "7":
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
    Muestra todos los usuarios almacenados en consola, también muestra cuantos usuarios se encontrarón
    Si no hay contactos o ocurre un error, se muestra un mensaje apropiado
    */
    private static void listContacts(ContactService contactService) {
        try {
            List<Contact> contacts = contactService.getAllContacts();
            if (!contacts.isEmpty()) {
                printSeparator();
                System.out.println("Lista de contactos:\t\t\t" + "Contactos encontrados: " + contacts.size() + "\n");
                contactService.getAllContacts().forEach(System.out::println);
                printSeparator();
            }
        } catch (ContactNoFoundException e) {
            printSeparator();
            System.out.println(e.getMessage());
            printSeparator();
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
                printSeparator();
                System.out.println("Ningún campo debe estar vacío, intentalo de nuevo.");
                printSeparator();
                continue;
            } else if (!Validator.isValidEmail(email)) {
                printSeparator();
                System.out.println("El email no es valido, intentalo de nuevo.");
                printSeparator();
                continue;
            } else if (!Validator.isValidPhone(number)) {
                printSeparator();
                System.out.println("El número de teléfono no es valido, intentalo de nuevo.");
                printSeparator();
                continue;
            } else if (!Validator.isValidName(name)) {
                printSeparator();
                System.out.println("El nombre no es valido, intentalo de nuevo.");
                printSeparator();
                continue;
            } else {
                try {
                    Contact contact = new Contact(name, email, number);
                    contactService.addContact(contact);
                    System.out.println();
                    printSeparator();
                    System.out.println("Contacto guardado.");
                    printSeparator();
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
                printSeparator();
                System.out.println("El campo nombre no puede estar vacío, intentalo de nuevo.");
                printSeparator();
                System.out.println();
                continue;
            }
            Contact found = contactService.searchByName(name);
            if (found == null) {
                System.out.println();
                printSeparator();
                System.out.println("El contacto '" + name + "' no fue encontrado, intentalo de nuevo");
                printSeparator();
                System.out.println();
                continue;
            } else {
                try {
                    System.out.println();
                    printSeparator();
                    System.out.println("Contacto encontrado:");
                    System.out.println();
                    System.out.println(found);
                    printSeparator();
                    break;
                } catch (NoContactException e) {
                    printSeparator();
                    System.out.println(e.getMessage());
                    printSeparator();
                }

            }
        }
    }

    /*
    Permite modificar un campo específico de un contacto existente
    Solicita nombre del contacto, campo a modificar y el nuevo valor
    */
    public static void updateContact(ContactService contactService) {
        System.out.println("Ingresa el nombre del contacto que deseas actualizar:");
        String name = S.nextLine();
        if (name.isEmpty()) {
            printSeparator();
            System.out.println("El campo nombre no puede estar vacío");
            printSeparator();
        } else {
            while (true) {
                System.out.println("Ingresa el campo que deseas editar (name | email | phone):");
                String field = S.nextLine();
                if (field.equalsIgnoreCase("name") || field.equalsIgnoreCase("email") || field.equalsIgnoreCase("phone")) {
                    System.out.println("Ingresa el nuevo dato:");
                    String updateData = S.nextLine();
                    if (updateData.isEmpty()) {
                        printSeparator();
                        System.out.println("Ningún campo puede estar vacío.");
                        printSeparator();
                        continue;
                    }
                    try {
                        boolean isuptade = contactService.edit(name, field, updateData);
                        if (isuptade) {
                            System.out.println();
                            printSeparator();
                            System.out.println("Contacto actualizado.");
                            printSeparator();
                            break;
                        } else {
                            System.out.println();
                            printSeparator();
                            System.out.println("Contacto no encontrado.");
                            printSeparator();
                            break;
                        }
                    } catch (NoContactException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                } else {
                    System.out.println();
                    printSeparator();
                    System.out.println("Campo invalido, vuelve a intentarlo.");
                    printSeparator();
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
                printSeparator();
                System.out.println("Contacto eliminado.");
                printSeparator();
            } else {
                System.out.println();
                printSeparator();
                System.out.println("Contacto '" + name + "' no encontrado.");
                printSeparator();
            }
        } catch (EmptyException e) {
            printSeparator();
            System.out.println("El nombre de usuario no puede estar vacío.");
            printSeparator();
        } catch (DeleteFailedContact e) {
            printSeparator();
            System.out.println("El campo nombre esta vacío, por lo que el contacto\n no fue encontrado y no fue posible eliminar.");
            printSeparator();
        }
    }

    /*
    Muestra los contactos de acuerdo a la letra ingresada
    Si se ingresa más de una letra o si no se encuentra algún contacto con esa letra se
    muestra un menaje personalizado
    */
    public static void filterByInitial(ContactService contactService) {
        System.out.println("Ingresa la letra inicial por la cual deseas filtrar:");
        String initial = S.nextLine();

        if (initial.length() != 1 || !Character.isLetter(initial.charAt(0))) {
            printSeparator();
            System.out.println("Debes ingresar solo una letra.");
            printSeparator();
            return;
        }

        char firstLetter = Character.toLowerCase(initial.charAt(0));

        List<Contact> matches = contactService.getAllContacts().stream().filter(
                        c -> Character.toLowerCase(c.getName().charAt(0)) == firstLetter).
                collect(Collectors.toList());

        if (matches.isEmpty()) {
            System.out.println();
            printSeparator();
            System.out.println("No se encontraron contactos que empiecen con la letra: " + initial);
            printSeparator();
        } else {
            System.out.println();
            printSeparator();
            System.out.println("Contactos que empiezan con '" + initial.toUpperCase() + "'\t\t" + matches.size() + " contactos encontrados.\n");
            matches.forEach(System.out::println);
            printSeparator();
        }
    }

    // Imprime una serie de caracteres para mejorar la salida en consola
    public static void printSeparator() {
        System.out.println(CHARACTER);
    }
}
