package com.ucdrive.client;

import java.io.*;

public class AuthenticatedMenu {
    public AuthenticatedMenu() {
    }

    public void authenticatedMenu(DataInputStream in, DataOutputStream out, BufferedReader reader) {
        // Checks if user's input is valid
        int choice = -1;
        try {
            do {
                System.out.println(in.readUTF());

                out.writeUTF(reader.readLine());

                /*
                 * if (sc.hasNextInt()) {
                 * choice = sc.nextInt();
                 * if (choice > 0 && choice < 9) {
                 * if (sc.hasNextLine()) {
                 * sc.nextLine();
                 * }
                 * continue;
                 * }
                 * } else {
                 * if (sc.hasNextLine()) {
                 * sc.nextLine();
                 * }
                 * }
                 */
            } while (choice < 1 || choice > 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}
