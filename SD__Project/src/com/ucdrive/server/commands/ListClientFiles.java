package com.ucdrive.server.commands;

import java.io.*;

public class ListClientFiles {
    public ListClientFiles() {
    }

    public void listClientFiles(String clientPath, DataOutputStream out) throws IOException{
        out.writeUTF(clientPath);
    }
}
