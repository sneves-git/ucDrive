package com.ucdrive.client.commands;
import com.ucdrive.utils.ConsoleColors;
import com.ucdrive.utils.ServerHelperClass;

import java.io.*;

public class ListClientFiles {
    public ListClientFiles(){}
    public void listClientFiles(DataInputStream in, DataOutputStream out, boolean flag)  throws IOException {

        String ClientPath = in.readUTF();
        if(flag){
            System.out.println("\n------------ List Client Files in ----------\n" +
                    ConsoleColors.GREEN +ClientPath+ ConsoleColors.RESET + "\n");
        }

        ServerHelperClass helper = new ServerHelperClass();
        helper.listFoldersAndFiles(out, ClientPath,true);
        if(flag){
            System.out.println("-----------------------------------------\n");
        }
    }
}
