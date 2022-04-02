package com.ucdrive.client.commands;
import com.ucdrive.utils.ServerHelperClass;

import java.io.*;

public class ListClientFiles {
    public ListClientFiles(){}
    public void listClientFiles(DataInputStream in, DataOutputStream out)  throws IOException {
        System.out.println("------------ List Client Files ----------");
        String ClientPath = in.readUTF();
        ServerHelperClass helper = new ServerHelperClass();
        helper.listFoldersAndFiles(out, ClientPath,true);
        System.out.println("-----------------------------------------");
    }
}
