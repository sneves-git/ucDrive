package com.ucdrive.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

import com.ucdrive.server.commands.ChangePassword;
import com.ucdrive.refactorLater.User;
import com.ucdrive.refactorLater.Users;

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
    public int authenticatedMenu(User user, Users users, DataInputStream in, DataOutputStream out) throws IOException {
        int choice = 0;

        while (choice != 11) {
            switch (choice) {
                case -1:
                    out.writeUTF("\nInvalid option.\n\n"
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
                    try {
                        choice = Integer.parseInt(in.readUTF());
                    } catch (NumberFormatException e) {
                        choice = -1;
                    }
                case 0:
                    out.writeUTF("Choose one of the following options:\n"
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

                    try {
                        choice = Integer.parseInt(in.readUTF());
                    } catch (NumberFormatException e) {
                        choice = -1;
                    }

                case 1:
                    ChangePassword obj = new ChangePassword();
                    obj.changePassword(user, users, in, out);
                    choice = 0;
                    break;
                case 2:
                    // changeClientsDirectory();
                    choice = 0;
                    break;
                case 3:
                    // changeServersDirectory(user, users);
                    choice = 0;
                    break;
                case 4:
                    // configureIpAndPort();
                    choice = 0;
                    break;
                case 5:
                    // createNewFolder();
                    choice = 0;
                    break;
                case 6:
                    // deleteFolder();
                    choice = 0;
                    break;
                case 7:
                    // downloadAFile();
                    choice = 0;
                    break;
                case 8:
                    // listClientFiles();
                    choice = 0;
                    break;
                case 9:
                    // listServerFiles(user);
                    choice = 0;
                    break;
                case 10:
                    // uploadAFile();
                    choice = 0;
                    break;
                case 11:
                    // exit();
                    break;
            }
        }
    }
}
