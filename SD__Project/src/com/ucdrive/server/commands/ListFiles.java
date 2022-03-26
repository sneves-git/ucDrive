package com.ucdrive.server.commands;

import com.ucdrive.refactorLater.User;

import java.io.File;

public class ListFiles {
    private void listServerFiles(User user) {
        String path = "src/com/ucdrive/server/Home/";

        File dir = new File(path + user.getLastSession());
        File[] files = dir.listFiles();

        if (files != null && files.length > 0) {
            for (File file : files) {
                // Check if the file is a directory
                if (file.isDirectory()) {
                    System.out.println("[Folder]  " + file.getName());
                } else {
                    System.out.println("[File]    " + file.getName());
                }
            }
        }
    }
}
