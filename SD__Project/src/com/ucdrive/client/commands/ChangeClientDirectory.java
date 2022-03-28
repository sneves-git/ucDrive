package com.ucdrive.client.commands;

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
            } else {
                System.out.println("Stop going back! There is no going back anymore...");
                newPath = ClientPath;
            }
            out.writeUTF("Success!");
            out.writeUTF(newPath);
            // Write to server
            System.out.println("Success!");
            return;
        }

        // Moves the directory forward one directory to the folder "nameOfFolder"
        List<String> aux_list = new ArrayList<String>(Arrays.asList(command.split(" ")));

        // If input is incorrect ex:"cd folder (something)" -> error
        if (aux_list.size() > 2) {
            out.writeUTF("Wrong input!");
            System.out.println("Wrong input!");

            return;
        } else if (aux_list.size() == 2) {
            if (aux_list.get(0).equals("cd")) {
                aux_list.remove(0);
            } else {
                out.writeUTF("Wrong input!");
                System.out.println("Wrong input!");
                return;
            }
        }

        ArrayList<String> clientFolders = getUserFoldersInClient(ClientPath);
        for (String folder : clientFolders) {
            if (folder.equals(aux_list.get(0))) {
                newPath = ClientPath + "\\"+folder ;

                out.writeUTF("Success!");
                out.writeUTF(newPath);

                System.out.println("Success!");
                return;
            }
        }

        out.writeUTF("The folder doesn't exist!");
        System.out.println("The folder doesn't exist!");
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
