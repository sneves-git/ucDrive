package com.ucdrive.server.commands;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CreateNewServerFolder {
    public CreateNewServerFolder() {
    }

    public void createNewServerFolder(String serverPath, DataInputStream in, DataOutputStream out) throws IOException {
        System.out.println("--------- Create new server Folder --------");
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString() + "/src/com/ucdrive/server/" + serverPath;

        // Write to client current path and wait for folder to create
        out.writeUTF(serverPath + ">");

        // Read client's input
        String command = in.readUTF();

        List<String> aux_list = new ArrayList<>(Arrays.asList(command.split(" ")));
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

        File f = new File(path + "/" + aux_list.get(0));
        if (f.mkdir()) {
            System.out.println("Success!");
        } else {
            System.out.println("Failed!");
        }
    }
}
