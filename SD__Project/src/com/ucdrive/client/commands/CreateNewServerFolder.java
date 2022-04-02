package com.ucdrive.client.commands;

import com.ucdrive.utils.ConsoleColors;

import java.io.*;

public class CreateNewServerFolder {
    public CreateNewServerFolder(){}

    public void createNewServerFolder(DataInputStream in, DataOutputStream out, BufferedReader reader) throws IOException {
        System.out.println("\n--------- Create new server Folder --------");

        System.out.print(in.readUTF());
        out.writeUTF(reader.readLine());

        String ack = in.readUTF();
        if(ack.equals("Failed!")){
            System.out.println(ConsoleColors.RED + "Failed to create new folder, it already exists!" + ConsoleColors.RESET);
        }else if(ack.equals("Success!")){
            System.out.println(ConsoleColors.GREEN + "Created folder successfully!" + ConsoleColors.RESET);
        }else if(ack.equals("Wrong input!")){
            System.out.println(ConsoleColors.RED + "Wrong input! Please use: mkdir <folderName> OR <folderName>!" + ConsoleColors.RESET);
        }
        System.out.println("-------------------------------------------\n");

    }
}
