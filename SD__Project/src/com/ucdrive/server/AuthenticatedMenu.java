package com.ucdrive.server;

import java.util.Scanner;

public class AuthenticatedMenu {
    private Scanner sc;

    public AuthenticatedMenu() {
        sc = new Scanner(System.in);
    }

    /*
     * private void clientsApplication(User user, Users users) {
     * int choice;
     * // choice = authenticatedMenu();
     * while ((choice = authenticatedMenu()) != 8) {
     * switch (choice) {
     * case 1:
     * changePassword(user);
     * break;
     * case 2:
     * changeClientsDirectory();
     * break;
     * case 3:
     * changeServersDirectory(user, users);
     * break;
     * case 4:
     * listClientFiles();
     * break;
     * case 5:
     * listServerFiles(user);
     * break;
     * case 6:
     * uploadAFile();
     * break;
     * case 7:
     * downloadAFile();
     * break;
     * }
     * }
     * }
     */
    private int authenticatedMenu() {
        System.out.print("Choose one of the following options:\n"
                + "\t1 - Change password\n"
                + "\t2 - Change client's directory\n"
                + "\t3 - Change server's directory\n"
                + "\t4 - List client files\n"
                + "\t5 - List server files\n"
                + "\t6 - Upload a file\n"
                + "\t7 - Download a file\n"
                + "\t8 - Exit\n"
                + "Choice: ");

        // Checks if user's input is valid
        int choice = 0;
        try {
            do {
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    if (choice > 0 && choice < 9) {
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
                        + "\t1 - Change password\n"
                        + "\t2 - Change client's directory\n"
                        + "\t3 - Change server's directory\n"
                        + "\t4 - List cleint files\n"
                        + "\t5 - List server files\n"
                        + "\t6 - Upload a file\n"
                        + "\t7 - Download a file\n"
                        + "\t8 - Exit\n"
                        + "Choice: ");
            } while (choice < 1 || choice > 8);
        } catch (Exception e) {
            System.out.println("Login.java in Menu() - An error occurred with scanner.");
            e.printStackTrace();
        }
        System.out.println();

        return choice;
    }
}
