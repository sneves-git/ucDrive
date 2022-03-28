package com.ucdrive.server.commands;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DeleteServerFolderOrFile {
    public DeleteServerFolderOrFile(){}

    public void deleteServerFolder(String serverPath, DataInputStream in, DataOutputStream out, String server) throws IOException  {
        System.out.println("--------- Delete server Folder/File --------");
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString() + "/src/com/ucdrive/server/"+ server + "/" + serverPath;

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
            if (aux_list.get(0).equals("del")) {
                aux_list.remove(0);
            } else {
                System.out.println("Wrong input!");
                return;
            }
        }

        File f = new File(path + "/" + aux_list.get(0));
        if (f.exists()) {
            f.delete();
            System.out.println("Success!");
        } else {
            System.out.println("The folder/file doesn't exist or you are in the wrong directory.");
        }
    }
}
