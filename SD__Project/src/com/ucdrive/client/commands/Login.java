package com.ucdrive.client.commands;

import com.ucdrive.refactorLater.User;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Login {
    private String username, password;

    public Login() {
        username = null;
        password = null;
    }

    public void login_(DataInputStream in, DataOutputStream out, BufferedReader reader) throws IOException {
        System.out.println("------------ Login -------------");
        String data = "Username does not exist!";
        do {
            System.out.print("Username: ");

            // Reads username from std.in
            username = reader.readLine();

            // Sends username to server
            out.writeUTF(username);

            // Server sends ACK saying if user exists or not
            data = in.readUTF();

        } while (data.equals("Username does not exist!"));

        data = "Invalid password!";
        do {
            System.out.print("Password: ");
            // Reads password from std.in
            password = reader.readLine();

            // Sends password to server
            out.writeUTF(password);

            // Server sends ACK saying if password exists or not
            data = in.readUTF();

        } while (data.equals("Invalid password!"));
        System.out.println();

        // VER ISTO QUE FALTA!
        // -------------------
        // clientsApplication(user, users);
    }
}
