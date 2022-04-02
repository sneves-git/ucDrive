package com.ucdrive.client.commands;

import java.io.*;
import java.net.Socket;

public class DownloadAFile extends Thread {
    private DataInputStream in;
    private Socket socket;
    private int buffsize;
    private String clientPath, fileName;

    public DownloadAFile(Socket aClientSocket, String clientPath, String fileName) {
        try {
            this.buffsize = 1024;
            this.clientPath = clientPath;
            this.fileName = fileName;
            this.socket = aClientSocket;
            this.in = new DataInputStream(socket.getInputStream());

            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    // =============================
    public void run() {

        try {
            FileOutputStream fos = new FileOutputStream(clientPath + "/" + fileName);
            byte[] buf = new byte[buffsize];

            int length;
            while ((length = in.read(buf, 0, buf.length)) > 0) {
                fos.write(buf, 0, length);

            }
            fos.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
