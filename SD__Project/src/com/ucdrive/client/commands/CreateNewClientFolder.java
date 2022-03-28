package com.ucdrive.client.commands;

import java.io.*;
import java.util.*;

public class CreateNewClientFolder {
    public CreateNewClientFolder() {
    }

    public void createNewClientFolder(DataInputStream in, DataOutputStream out, BufferedReader reader)
            throws IOException {
        System.out.println("--------- Create new client Folder --------");
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
                System.out.println("Wrong input!");
                return;
            }
        }

        File f = new File(ClientPath + "/" + aux_list.get(0));
        if (f.mkdir()) {
            System.out.println("Success!");
        } else {
            System.out.println("Failed!");
        }

    }
}
