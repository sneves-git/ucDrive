package com.ucdrive.server.commands;

import com.ucdrive.server.copyPartOfFileStorage.PrimaryUpdateFolderOrFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CreateNewServerFolder {
    public CreateNewServerFolder() {
    }

    public void createNewServerFolder(String serverPath, DataInputStream in, DataOutputStream out, String server, int myPort, int filePort, String host) throws IOException, InterruptedException {
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString() + "/src/com/ucdrive/server/" + server +"/"+ serverPath;

        // Write to client current path and wait for folder to create
        out.writeUTF(serverPath + ">");

        // Read client's input
        String command = in.readUTF();

        List<String> aux_list = new ArrayList<>(Arrays.asList(command.split(" ")));
        // If input is incorrect ex:"mkdir folder (something)" -> error
        if (aux_list.size() > 2) {
            out.writeUTF("Wrong input!");
            return;
        } else if (aux_list.size() == 2) {
            if (aux_list.get(0).equals("mkdir")) {
                aux_list.remove(0);
            } else {
                out.writeUTF("Wrong input!");
                return;
            }
        }

        File f = new File(path + "/" + aux_list.get(0));
        if(f.mkdir()){
            out.writeUTF("Success!");
            Thread pufof = new PrimaryUpdateFolderOrFile(myPort, filePort, host, server, path + "/" + aux_list.get(0),
                    "CreateNewFolder");
            pufof.join();
        }else{
            out.writeUTF("Failed!");
        }

    }
}
