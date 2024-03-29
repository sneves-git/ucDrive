package com.ucdrive.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import com.ucdrive.utils.User;
import com.ucdrive.utils.Users;
import com.ucdrive.server.commands.Login;

public class FirstMenu {
    public User user;

    public FirstMenu() {
        this.user = null;
    }

    public void firstMenu(Users users, DataInputStream in, DataOutputStream out, String server, int myPort, int filePort, String host) {
        int choice = -1, flag = 0;
        try {
            do {
                // Receives signal that user wants to log in
                String data = in.readUTF();
                switch (data) {
                    case "login":
                        Login login = new Login();
                        this.user = login.login_(users, in, out);

                        if (flag == 0) {
                            if (user.getLastChoice() == 0) {
                                out.writeUTF("Menu!");
                            } else {
                                out.writeUTF(Integer.toString(user.getLastChoice()));
                            }
                            flag = 1;
                        }

                        AuthenticatedMenu authMenu = new AuthenticatedMenu();
                        choice = authMenu.authenticatedMenu(this.user, users, in, out, server, myPort, filePort, host);

                        break;
                    default:
                        break;
                }

            } while (choice != 13);
        } catch (EOFException e) {
        } catch (IOException e) {
        }
    }
}