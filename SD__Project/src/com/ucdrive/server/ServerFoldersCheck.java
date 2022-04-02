package com.ucdrive.server;

import java.util.ArrayList;
import com.ucdrive.utils.User;
import java.io.File;
import java.nio.file.*;

public class ServerFoldersCheck {

    public ServerFoldersCheck() {
    }

    public void checkingFolders(ArrayList<User> users, String server) {
        for (User user : users) {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            String path = "/src/com/ucdrive/server/" + server + "/Home";
            String totalPath = Paths.get(s, path).toString() + "/" + user.getUsername() + "/";

            File file = new File(totalPath);
            file.mkdir();

        }
    }
}
