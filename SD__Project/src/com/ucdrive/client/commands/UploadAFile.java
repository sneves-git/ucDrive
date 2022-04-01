package com.ucdrive.client.commands;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class UploadAFile extends Thread {
    private DataOutputStream out;
    private Socket socket;
    private int buffsize;
    private String clientPath, fileName;

    public UploadAFile(Socket aClientSocket, String clientPath, String fileName ){
        try {
            this.buffsize = 1024;
            this.clientPath = clientPath;
            this.fileName = fileName;
            this.socket = aClientSocket;
            this.out = new DataOutputStream(socket.getOutputStream());

            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        try {
            int nread;
            byte[] buf = new byte[buffsize];

            FileInputStream fis = new FileInputStream(clientPath + "/" + fileName);
            try {
                do {
                    nread = fis.read(buf);
                    if (nread > 0) {
                        out.write(buf, 0, nread);
                    }
                } while (nread > -1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
