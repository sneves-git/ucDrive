package com.ucdrive.client.commands;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.ucdrive.refactorLater.User;

public class ChangeClientDirectory {

    public ChangeClientDirectory() {
    }

    public void changeClientDirectory(User user, DataInputStream in, DataOutputStream out, BufferedReader reader)
            throws IOException {

        System.out.print(user.getClientPath() + "> ");
        String command = reader.readLine();

        // Moves the directory back one directory
        if (command.equals("cd ..") || command.equals("..")) {
            String newPath = "";

            // Split path by "/"
            List<String> list = new ArrayList<String>(Arrays.asList(user.getClientPath().split("/")));

            // Removes Last Folder in directory
            list.remove(list.size() - 1);

            // Joins path to form new clients directory
            int i = 0;
            if (list.size() > 0) {
                for (i = 0; i < list.size() - 1; ++i) {
                    newPath += list.get(i) + "/";
                }
                newPath += list.get(i);
                user.setClientPath(newPath);
            }

            // Write to server
            out.writeUTF("Success!");
            return;
        }

        // Moves the directory forward one directory to the folder "nameOfFolder"
        List<String> list = new ArrayList<String>(Arrays.asList(command.split(" ")));

        // If input is incorrect ex:"cd folder (something)" -> error
        if (list.size() > 2) {
            out.writeUTF("Wrong input!");
            return;
        }
        if (list.size() == 2) {
            if (list.get(0).equals("cd")) {
                list.remove(0);
            } else {
                out.writeUTF("Wrong input!");
                return;
            }
        }

        ArrayList<String> clientFolders = user.getUserFoldersInClient();
        for (String folder : clientFolders) {
            if (folder.equals(list.get(0))) {
                out.writeUTF("Success!");
                return;
            }
        }

        out.writeUTF("The folder doesn't exist!");
        return;
    }

}
