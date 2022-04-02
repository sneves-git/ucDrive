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
        /*
         * 1. Crio uma nova thread (enviar out)
         * 2. criar um novo socket com o porto 0 (na nova thread)
         * 3. Enviar ao servidor que porto quero (usando o 0) atraves do out
         * 4. Criar novo out e novo in e reader
         * 5. tentar conectar-me ao servidor com o novo socket
         * 6. ligaçao established
         * 7. recebo o client path
         * 8. recebo o que posso fzr download e escolho
         * 9. envio a escolha
         * 10. recebo ficheiro em chunks ate à mensagem do terminei
         */

        try {
            FileOutputStream fos = new FileOutputStream(clientPath + "/" + fileName);
            byte[] buf = new byte[buffsize];

            int length;
            while ((length = in.read(buf, 0, buf.length)) > 0) {
                fos.write(buf, 0, length);

                Thread.sleep(10000);
            }
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
