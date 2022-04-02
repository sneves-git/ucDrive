package com.ucdrive.client.commands;

import com.ucdrive.utils.ConsoleColors;

import java.io.*;
import java.util.*;


public class DeleteClientFolderOrFile {
    public DeleteClientFolderOrFile() {
    }

    public void deleteClientFolder(DataInputStream in, DataOutputStream out, BufferedReader reader)
            throws IOException {
        System.out.println("\n--------- Delete client Folder/File --------");
        ListClientFiles obj14 = new ListClientFiles();
        obj14.listClientFiles(in, out, false);
        System.out.println();

        String ClientPath = in.readUTF();
        System.out.print(ClientPath + ">");
        String command = reader.readLine();
        List<String> aux_list = new ArrayList<String>(Arrays.asList(command.split(" ")));
        // If input is incorrect ex:"mkdir folder (something)" -> error
        if (aux_list.size() > 2) {
            System.out.println(ConsoleColors.RED + "Wrong input! Please use: del <folderName> OR del <fileName>!" + ConsoleColors.RESET);
            return;
        } else if (aux_list.size() == 2) {
            if (aux_list.get(0).equals("del")) {
                aux_list.remove(0);
            } else {
                System.out.println(ConsoleColors.RED + "Wrong input! Please use: del <folderName> OR del <fileName>!" + ConsoleColors.RESET);
                return;
            }
        }

        File f = new File(ClientPath + "/" + aux_list.get(0));
        if (f.exists()) {
            f.delete();
            System.out.println(ConsoleColors.GREEN + "Folder " + aux_list.get(0) + " was deleted successfully!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "The folder/file " + aux_list.get(0) + " does not exist or you are in the wrong directory!" + ConsoleColors.RESET);
        }
        System.out.println("-----------------------------------------\n");

    }
}
