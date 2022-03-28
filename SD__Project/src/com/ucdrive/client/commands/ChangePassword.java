package com.ucdrive.client.commands;

import java.io.*;

public class ChangePassword {
    private String password;

    public ChangePassword() {
        password = null;
    }

    public void changePassword(DataInputStream in, DataOutputStream out, BufferedReader reader) throws IOException {
        System.out.println("------------ Change password -------------");
        String data = "Invalid password!";
        do {
            System.out.print("Current password: ");

            // Reads password from std.in
            password = reader.readLine();

            // Sends username to server
            out.writeUTF(password);

            // Server sends ACK saying if password exists or not
            data = in.readUTF();
        } while (data.equals("Invalid password!"));

        System.out.print("New password: ");

        // Reads password from std.in
        password = reader.readLine();

        // Sends password to server
        out.writeUTF(password);

        // Receives ACK
        System.out.println(in.readUTF());
    }
}
