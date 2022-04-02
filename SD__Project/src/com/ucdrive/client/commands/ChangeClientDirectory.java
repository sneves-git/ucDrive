package com.ucdrive.client.commands;

import com.ucdrive.utils.ConsoleColors;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class ChangeClientDirectory {

    public ChangeClientDirectory() {
    }

    public void changeClientDirectory(DataInputStream in, DataOutputStream out, BufferedReader reader)
            throws IOException {
        System.out.println("\n------------ Change client's directory -------------");

        String ClientPath = in.readUTF();

        System.out.print(ClientPath + "> ");
        String command = reader.readLine();

        String newPath = "";
        // Moves the directory back one directory
        if (command.equals("cd ..") || command.equals("..")) {

            // Split path by "/"
            String Path_ = ClientPath.replaceAll(Pattern.quote("\\"), "/");
            List<String> list = new ArrayList<String>(Arrays.asList(Path_.split("/")));

            // Removes Last Folder in directory
            list.remove(list.size() - 1);

            // Joins path to form new clients directory
            int i = 0;
            if (list.size() > 0) {
                for (i = 0; i < list.size() - 1; ++i) {
                    newPath += list.get(i) + "\\";
                }
                newPath += list.get(i);
                System.out.println(ConsoleColors.GREEN + "Success went back a directory!" + ConsoleColors.RESET + "\n------------------------------------------------\n");

            } else {
                System.out.println(ConsoleColors.RED + "Could not go back a directory! There is no going back anymore..." + ConsoleColors.RESET + "\n------------------------------------------------\n");
                newPath = ClientPath;
            }
            // Write to server
            out.writeUTF("Success!");
            out.writeUTF(newPath);
            return;
        }

        // Moves the directory forward one directory to the folder "nameOfFolder"
        List<String> aux_list = new ArrayList<String>(Arrays.asList(command.split(" ")));

        // If input is incorrect ex:"cd folder (something)" -> error
        if (aux_list.size() > 2) {
            out.writeUTF("Wrong input!");
            System.out.println(ConsoleColors.RED + "Wrong input! Please use: cd <folderName> OR <folderName>!" + ConsoleColors.RESET);
            System.out.println("------------------------------------------------\n");
            return;
        } else if (aux_list.size() == 2) {
            if (aux_list.get(0).equals("cd")) {
                aux_list.remove(0);
            } else {
                out.writeUTF("Wrong input!");
                System.out.println(ConsoleColors.RED + "Wrong input! Please use: cd <folderName> OR <folderName>!" + ConsoleColors.RESET);
                System.out.println("------------------------------------------------\n");
                return;
            }
        }

        ArrayList<String> clientFolders = getUserFoldersInClient(ClientPath);
        for (String folder : clientFolders) {
            if (folder.equals(aux_list.get(0))) {
                newPath = ClientPath + "\\"+folder ;

                out.writeUTF("Success!");
                out.writeUTF(newPath);

                System.out.println(ConsoleColors.GREEN + "Changed directory to " + aux_list + "!" + ConsoleColors.RESET);
                System.out.println("------------------------------------------------\n");

                return;
            }
        }

        out.writeUTF("The folder doesn't exist!");
        System.out.println(ConsoleColors.RED + "Directory " + aux_list + " does not exist!" + ConsoleColors.RESET);
        System.out.println("------------------------------------------------\n");

        return;
    }

    public ArrayList<String> getUserFoldersInClient(String clientPath) {
        ArrayList<String> folders = new ArrayList<String>();

        File dir = new File(clientPath);
        File[] files = dir.listFiles();

        if (files != null && files.length > 0) {
            for (File file : files) {
                // Check if the file is a directory
                if (file.isDirectory()) {
                    folders.add(file.getName());
                }
            }
        }
        return folders;
    }

}
