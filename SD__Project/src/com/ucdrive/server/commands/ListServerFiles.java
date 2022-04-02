package com.ucdrive.server.commands;

import java.io.*;
import java.nio.file.*;

import com.ucdrive.utils.ServerHelperClass;

public class ListServerFiles {
    public ListServerFiles() {
    }

    public void listServerFiles(String serverPath, DataInputStream in, DataOutputStream out, String server)
            throws IOException {
        ServerHelperClass obj = new ServerHelperClass();

        serverPath = serverPath.replace("/", "\\");
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString() + "\\src\\com\\ucdrive\\server\\" + server + "\\"
                + serverPath;

        out.writeUTF(obj.convertPath(path, server));

        obj.listFoldersAndFiles(out, path, false);
    }
}
