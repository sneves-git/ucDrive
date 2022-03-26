package com.ucdrive.client.commands;

import com.ucdrive.refactorLater.User;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

import static com.ucdrive.refactorLater.Users.*;
import static com.ucdrive.refactorLater.Users.getUsers;
import static com.ucdrive.configs.UsersConfigsFile.readUsersInfo;

public class Login {
    private static Scanner sc = new Scanner(System.in);
    private static String username = null, password = null;


    public static User login_(DataInputStream in, DataOutputStream out, BufferedReader reader) throws IOException {
        System.out.println("------------ Login -------------");
        String data = "false", text = null;
        do{
            System.out.print("Username: ");
            if (sc.hasNextLine()) {
                // Reads username from std.in
                text = reader.readLine();

                // Sends username to server
                out.writeUTF(text);

                // Server sends ACK saying if user exists or not
                data = in.readUTF();
            }
        }while(data.equals("false"));

        do {
            System.out.print("Password: ");
            if (sc.hasNextLine()) {
                // Reads password from std.in
                text = reader.readLine();

                // Sends password to server
                out.writeUTF(sc.nextLine());

                // Server sends ACK saying if password exists or not
                data = in.readUTF();
            }
        } while (data.equals("false"));
        System.out.println();


        User user = null;
        for (User u : getUsers()) {
            if (u.getUsername().equals(username)) {
                user = u;
            }
        }

        return user;

        // VER ISTO QUE FALTA!
        // -------------------
        //clientsApplication(user);
    }
}
