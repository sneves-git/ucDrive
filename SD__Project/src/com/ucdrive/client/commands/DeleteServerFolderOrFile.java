package com.ucdrive.client.commands;

import com.ucdrive.utils.ConsoleColors;

import java.io.*;

public class DeleteServerFolderOrFile {

    public DeleteServerFolderOrFile(){}

    public void deleteServerFolder(DataInputStream in, DataOutputStream out, BufferedReader reader) throws IOException{
        System.out.println("\n--------- Delete server Folder/File --------");

        ListServerFiles obj15 = new ListServerFiles();
        obj15.listServerFiles(in, false);
        System.out.println();

        System.out.print(in.readUTF());
        out.writeUTF(reader.readLine());

        String ack = in.readUTF();
        if(ack.equals("The folder/file doesn't exist or you are in the wrong directory.")){
            System.out.println(ConsoleColors.RED + "The folder/file does not exist or you are in the wrong directory!" + ConsoleColors.RESET);
        }else if(ack.equals("Wrong input!")){
            System.out.println(ConsoleColors.RED + "Wrong input! Please use: del <folderName> OR del <fileName>!" + ConsoleColors.RESET);
        }else if(ack.equals("Success!")){
            System.out.println(ConsoleColors.GREEN + "Folder/File was deleted successfully!" + ConsoleColors.RESET);
        }

        System.out.println("--------------------------------------------\n");

    }
}
