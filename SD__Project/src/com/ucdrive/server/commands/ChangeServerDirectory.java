package com.ucdrive.server.commands;

import com.ucdrive.configs.UsersConfigsFile;
import com.ucdrive.refactorLater.User;
import com.ucdrive.refactorLater.Users;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ChangeServerDirectory {
    public ChangeServerDirectory() {
    }

    public void changeServerDirectory(User user, Users users, DataInputStream in, DataOutputStream out, String server)
            throws IOException {
        out.writeUTF(user.getLastSessionServer() + "> ");
        String command = in.readUTF();
        String newPath = "";
        String fileName = "src/com/ucdrive/configs/users.txt";
        UsersConfigsFile conf = new UsersConfigsFile();

        // Moves the directory back one directory
        if (command.equals("cd ..") || command.equals("..")) {

            // Split path by "/"
            List<String> list = new ArrayList<String>(Arrays.asList(user.getLastSessionServer().split("/")));

            // Removes Last Folder in directory
            list.remove(list.size() - 1);

            // Joins path to form new clients directory
            int i = 0;
            if (list.size() > 1) {
                for (i = 0; i < list.size() - 1; ++i) {
                    newPath += list.get(i) + "/";
                }
                newPath += list.get(i);
            } else {
                newPath = user.getLastSessionServer();
            }

            // Write to server
            out.writeUTF("Success!");
            for (User u : users.getUsers()) {
                if (u.getUsername().equals(user.getUsername())) {
                    u.setLastSessionServer(newPath);
                }
            }

            conf.updateUsersFile(users);
            return;
        }

        // Moves the directory forward one directory to the folder "nameOfFolder"
        List<String> aux_list = new ArrayList<String>(Arrays.asList(command.split(" ")));

        // If input is incorrect ex:"cd folder (something)" -> error
        if (aux_list.size() > 2) {
            out.writeUTF("Wrong input!");
            return;
        }
        if (aux_list.size() == 2) {
            if (aux_list.get(0).equals("cd")) {
                aux_list.remove(0);
            } else {
                out.writeUTF("Wrong input!");
                return;
            }
        }
        ArrayList<String> clientFolders = getUserFoldersInClient(user.getLastSessionServer(), user.getUsername(),
                server);
        for (String folder : clientFolders) {
            if (folder.equals(aux_list.get(0))) {
                newPath = user.getLastSessionServer() + "/" + folder;
                for (User u : users.getUsers()) {
                    if (u.getUsername().equals(user.getUsername())) {
                        u.setLastSessionServer(newPath);
                    }
                }
                conf.updateUsersFile(users);

                out.writeUTF("Success!");

                System.out.println("Success!");
                return;
            }
        }

        out.writeUTF("The folder doesn't exist!");
        return;
    }

    public ArrayList<String> getUserFoldersInClient(String clientPath, String username, String server) {
        ArrayList<String> folders = new ArrayList<String>();

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();

        String path = "/src/com/ucdrive/server/" + server + "/Home/" + username;
        String totalPath = Paths.get(s, path).toString();
        System.out.println(totalPath);

        File dir = new File(totalPath);
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
