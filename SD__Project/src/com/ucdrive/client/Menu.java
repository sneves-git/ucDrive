package com.ucdrive.client;

import java.util.*;

// All menus from client, authentication menu, logged in menu, ...
public class Menu {
    Scanner sc;

    public Menu() {
        sc = new Scanner(System.in);
    }

    public int clientMenu() {
        System.out.print("Choose one of the following options:\n"
                + "\t1 - Configure IP and port of server\n"
                + "\t2 - Login\n"
                + "\t3 - Exit\n"
                + "Choice: ");

        // Checks if user's input is valid
        int choice = 0;
        try {
            do {
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    if (choice > 0 && choice < 4) {
                        if (sc.hasNextLine()) {
                            sc.nextLine();
                        }
                        continue;
                    }
                } else {
                    if (sc.hasNextLine()) {
                        sc.nextLine();
                    }
                }

                System.out.print("\nInvalid option.\n\n"
                        + "Choose one of the following options:\n"
                        + "\t1 - Configure IP and port of server\n"
                        + "\t2 - Login\n"
                        + "\t3 - Exit\n"
                        + "Choice: ");
            } while (choice < 1 || choice > 3);
        } catch (Exception e) {
            System.out.println("Login.java in Menu() - An error occurred with scanner.");
            e.printStackTrace();
        }
        return choice;
    }

    @Override
    public String toString() {
        return "Menu";
    }
}

