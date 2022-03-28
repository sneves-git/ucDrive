package com.ucdrive.client.commands;
import com.ucdrive.refactorLater.ServerHelperClass;

import java.io.*;
import java.util.*;

public class ListClientFiles {
    public ListClientFiles(){}
    public void listClientFiles(DataInputStream in, DataOutputStream out)  throws IOException {
        System.out.println("------------ List Client Files ----------");
        String ClientPath = in.readUTF();
        ServerHelperClass helper = new ServerHelperClass();
        helper.listFiles(out, ClientPath,true);
        System.out.println("-----------------------------------------");
    }
}
