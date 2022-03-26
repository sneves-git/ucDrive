package com.ucdrive.client;

import java.io.*;

public class AuthenticatedMenu {
    public AuthenticatedMenu() {
    }

    public boolean authenticatedMenu(DataInputStream in, DataOutputStream out, BufferedReader reader) {
        // Checks if user's input is valid
        try {
            do {
                System.out.println(in.readUTF());
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
                        + "\t1  - Change password\n"
                        + "\t2  - Change client's directory\n"
                        + "\t3  - Change server's directory\n"
                        + "\t4  - Configure IP and port of servers\n"
                        + "\t5  - Create new folder\n"
                        + "\t6  - Delete folder\n"
                        + "\t7  - Download a file\n"
                        + "\t8  - List client files\n"
                        + "\t9  - List server files\n"
                        + "\t10 - Upload a file\n"
                        + "\t11 - Exit\n"
                        + "Choice: ");
            } while (choice < 1 || choice > 8);
        } catch (Exception e) {
            System.out.println("Login.java in Menu() - An error occurred with scanner.");
            e.printStackTrace();
        }
        System.out.println();
    }
}
