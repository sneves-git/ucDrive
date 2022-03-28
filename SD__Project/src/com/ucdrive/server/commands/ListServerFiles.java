package com.ucdrive.server.commands;

import java.io.*;
import java.nio.file.*;

import com.ucdrive.refactorLater.ServerHelperClass;

public class ListServerFiles {
    public ListServerFiles() {
    }

    public void listServerFiles(String serverPath, DataInputStream in, DataOutputStream out) throws IOException {
        ServerHelperClass obj = new ServerHelperClass();

        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString() + "/src/com/ucdrive/server/" + serverPath;

        obj.listFoldersAndFiles(out, path, false);
    }
}
