package com.ucdrive.client.commands;

import java.io.*;

public class DeleteServerFolderOrFile {

    public DeleteServerFolderOrFile(){}

    public void deleteServerFolder(DataInputStream in, DataOutputStream out, BufferedReader reader) throws IOException{
        System.out.print(in.readUTF());
        out.writeUTF(reader.readLine());
        System.out.println();
    }
}
