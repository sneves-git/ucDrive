package com.ucdrive.client.commands;

import com.ucdrive.utils.ConsoleColors;

import java.io.*;

public class ListServerFiles {
    public ListServerFiles() {
    }

    public void listServerFiles(DataInputStream in, boolean flag) throws IOException {
        if(flag){
            System.out.println("\n------------ List Server Files in ----------");
        }
        System.out.println("\t\t\t\t"+ ConsoleColors.GREEN + in.readUTF() + ConsoleColors.RESET + "\n");

        String aux = in.readUTF();
        while (!aux.equals("done")) {
            System.out.println(aux);
            aux = in.readUTF();
        }

        if(flag){
            System.out.println("-----------------------------------------\n");
        }

    }
}
