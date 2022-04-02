package com.ucdrive.client;

import com.ucdrive.utils.ConsoleColors;

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
            while (true) {
                try {
                    choice = Integer.parseInt(firstReader.readLine());

                    if (choice > 0 && choice < 4) {
                        break;
                    } else {
                        System.out.print(ConsoleColors.RED + "Invalid option.\n\n" + ConsoleColors.RESET
                                + "Choose one of the following options:\n"
                                + "\t1 - Configure IP and port of servers\n"
                                + "\t2 - Login\n"
                                + "\t3 - Exit\n"
                                + "Choice: ");
                    }
                } catch (NumberFormatException e) {
                    System.out.print(ConsoleColors.RED + "Invalid option.\n\n" + ConsoleColors.RESET
                            + "Choose one of the following options:\n"
                            + "\t1 - Configure IP and port of servers\n"
                            + "\t2 - Login\n"
                            + "\t3 - Exit\n"
                            + "Choice: ");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return choice;
    }

    @Override
    public String toString() {
        return "Menu";
    }
}
