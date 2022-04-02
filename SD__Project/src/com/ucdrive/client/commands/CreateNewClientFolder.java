package com.ucdrive.client.commands;

import com.ucdrive.utils.ConsoleColors;

import java.io.*;
import java.util.*;

public class CreateNewClientFolder {
    public CreateNewClientFolder() {
    }

    public void createNewClientFolder(DataInputStream in, DataOutputStream out, BufferedReader reader)
            throws IOException {
        System.out.println("\n--------- Create new client Folder --------");
        String ClientPath = in.readUTF();
        System.out.print(ClientPath + ">");
        String command = reader.readLine();
        List<String> aux_list = new ArrayList<String>(Arrays.asList(command.split(" ")));
        // If input is incorrect ex:"mkdir folder (something)" -> error
        if (aux_list.size() > 2) {
          
            System.out.println("Wrong input!");

            return;
        } else if (aux_list.size() == 2) {
            if (aux_list.get(0).equals("mkdir")) {
                aux_list.remove(0);
            } else {
                System.out.println(ConsoleColors.RED + "Wrong input! Please use: mkdir <folderName> OR <folderName>!" + ConsoleColors.RESET);
                return;
            }
        }

        File f = new File(ClientPath + "/" + aux_list.get(0));
        if (f.mkdir()) {
            System.out.println(ConsoleColors.GREEN + "Created folder" + aux_list.get(0) + " successfully!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Failed to create folder" + aux_list.get(0) + "!" + ConsoleColors.RESET);
        }
        System.out.println("\n-------------------------------------------");

    }
}
