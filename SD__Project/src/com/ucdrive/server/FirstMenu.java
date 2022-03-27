package com.ucdrive.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import com.ucdrive.refactorLater.User;
import com.ucdrive.refactorLater.Users;
import com.ucdrive.server.commands.Login;


public class FirstMenu{
    public User user;

    public FirstMenu(){
        this.user = null;
    }

    public void firstMenu(Users users, DataInputStream in, DataOutputStream out){
        try {
            while (true) {
                // Receives signal that user wants to log in
                String data = in.readUTF();
                switch (data) {
                    case "login":
                        Login login = new Login();
                        this.user = login.login_(users, in, out);

                        AuthenticatedMenu authMenu = new AuthenticatedMenu();
                        authMenu.authenticatedMenu(this.user, users, in, out);

                        break;
                    default:
                        break;
                }


            }
        } catch (EOFException e) {
            System.out.println("EOF:" + e);
        } catch (IOException e) {
            System.out.println("IO:" + e);
        }
    }
}