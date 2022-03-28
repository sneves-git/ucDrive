package com.ucdrive.client.commands;

import com.ucdrive.refactorLater.ServerHelperClass;

import java.io.*;

public class ListServerFiles {
    public ListServerFiles() {
    }

    public void listServerFiles(DataInputStream in) throws IOException {
        System.out.println("------------ List Server Files ----------");

        String aux = in.readUTF();
        while (!aux.equals("done")) {
            System.out.println(aux);
            aux = in.readUTF();
        }

        System.out.println("-----------------------------------------");

    }
}
