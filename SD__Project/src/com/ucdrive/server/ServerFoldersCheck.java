package com.ucdrive.server;

import java.io.IOException;
import java.util.ArrayList;
import com.ucdrive.utils.User;
import java.io.File;
import java.nio.file.*;
import java.util.List;

public class ServerFoldersCheck {

    public ServerFoldersCheck() {
    }

    public void checkingFolders(ArrayList<User> users, String server) {
        String s= null, path=null;
        for (User user : users) {
            Path currentRelativePath = Paths.get("");
            s = currentRelativePath.toAbsolutePath().toString();
            path = "/src/com/ucdrive/server/" + server + "/Home";
            String totalPath = Paths.get(s, path).toString() + "/" + user.getUsername() + "/";

            File file = new File(totalPath);
            file.mkdir();

        }
        File f = new File(Paths.get(s, path).toString() + "/hidden.txt");
        setHiddenAttrib(f);
    }


    private static void setHiddenAttrib(File file) {
        try {
            List<String> cmdList = new ArrayList<String>();
            cmdList.add("attrib");
            cmdList.add("-H");
            cmdList.add(file.getPath());
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(cmdList);
            Process p;
            p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}