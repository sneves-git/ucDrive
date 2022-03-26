package com.ucdrive.server.commands;

import com.ucdrive.refactorLater.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static com.ucdrive.configs.UsersConfigsFile.updateUsersFile;

public class ChangeDirectory {
    private static Scanner sc = new Scanner(System.in);

    private boolean isValidCommand(String choice, User user) {
        String path = "src/com/ucdrive/server/Home/";
        String fileName = "src/com/ucdrive/configs/users.txt";

        ArrayList<String> folders = new ArrayList<String>();

        folders = user.getUserFolders(path);

        if (choice.equals("cd ..") || choice.equals("..")) {
            if (user.getLastSession().equals("Home/" + user.getUsername())) {
                // VER SE TEM PERMISSOES DE LEITURA
            } else {
                String newPath = "";
                List<String> list = new ArrayList<String>(Arrays.asList(user.getLastSession().split("/")));
                int i = 0;
                for (i = 0; i < list.size() - 2; ++i) {
                    newPath += list.get(i) + "/";
                }
                newPath += list.get(i);
                user.setLastSession(newPath);
            }
            updateUsersFile(fileName);
            return true;
        }
        for (String folder : folders) {
            if (choice.equals("cd " + folder) || choice.equals(folder)) {
                user.setLastSession(user.getLastSession() + "/" + folder);
                updateUsersFile(fileName);
                return true;
            }
        }
        System.out.println("The system cannot find the path specified.");
        return false;
    }

    private void changeServersDirectory(User user) {
        String choice = null;
        do {
            System.out.print(user.getLastSession() + "> ");
            if (sc.hasNextLine()) {
                choice = sc.nextLine();
            }

        } while (!isValidCommand(choice, user));

        // Update users configs with new directory

    }
}
