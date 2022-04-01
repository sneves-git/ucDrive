package com.ucdrive.client;

import java.io.*;
import java.util.*;

// All menus from client, authentication menu, logged in menu, ...
public class FirstMenu {
    private BufferedReader firstReader;

    public FirstMenu(BufferedReader reader) {
        this.firstReader = reader;
    }

    public int chooseOption() {
        System.out.print("Choose one of the following options:\n"
                + "\t1 - Configure IP and port of servers\n"
                + "\t2 - Login\n"
                + "\t3 - Exit\n"
                + "Choice: ");

        // Checks if user's input is valid
        int choice = 0;
        try {

            // sc = new Scanner(System.in);
            while (true) {
                try {
                    choice = Integer.parseInt(firstReader.readLine());

                    if (choice > 0 && choice < 4) {
                        break;
                    } else {
                        System.out.print("\nInvalid option.\n\n"
                                + "Choose one of the following options:\n"
                                + "\t1 - Configure IP and port of servers\n"
                                + "\t2 - Login\n"
                                + "\t3 - Exit\n"
                                + "Choice: ");
                    }
                } catch (NumberFormatException e) {
                    System.out.print("\nInvalid option.\n\n"
                            + "Choose one of the following options:\n"
                            + "\t1 - Configure IP and port of servers\n"
                            + "\t2 - Login\n"
                            + "\t3 - Exit\n"
                            + "Choice: ");
                }
            }

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
