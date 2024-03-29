package com.ucdrive.client.commands;

import com.ucdrive.utils.ConsoleColors;

import java.io.*;

public class ChangePassword {
    private String password;

    public ChangePassword() {
        password = null;
    }

    public void changePassword(DataInputStream in, DataOutputStream out, BufferedReader reader) throws IOException {
        System.out.println("\n------------ Change password -------------");
        String data = "Invalid password!";
        do {
            System.out.print("Current password: ");

            // Reads password from std.in
            password = reader.readLine();

            // Sends username to server
            out.writeUTF(password);

            // Server sends ACK saying if password exists or not
            data = in.readUTF();

            if(data.equals("Invalid password!")){
                System.out.println(ConsoleColors.RED + "Wrong password!" + ConsoleColors.RESET);
            }
        } while (data.equals("Invalid password!"));

        System.out.print("New password: ");

        // Reads password from std.in
        password = reader.readLine();

        // Sends password to server
        out.writeUTF(password);

        // Receives ACK
        System.out.println(ConsoleColors.GREEN + in.readUTF() + ConsoleColors.RESET);
        System.out.println("-----------------------------------------\n");

    }
}
