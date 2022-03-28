package com.ucdrive.client.commands;

import java.io.*;
import java.net.Socket;

public class DownloadAFile extends Thread {
    DataInputStream inPreviousSocket, in;
    DataOutputStream outPreviousSocket, out;
    BufferedReader readerPreviousSocket, reader;
    Socket socket;

    public DownloadAFile(Socket aClientSocket, DataInputStream inPreviousSocket, DataOutputStream outPreviousSocket,
            BufferedReader readerPreviousSocket) {
        try {
            this.socket = aClientSocket;
            this.inPreviousSocket = in;
            this.outPreviousSocket = out;
            this.readerPreviousSocket = readerPreviousSocket;

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

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
        String fileChoice = "", clientPath = "", choice = "";

        try {
            clientPath = in.readUTF();
            System.out.println(in.readUTF());
            choice = reader.readLine();
            out.writeUTF(choice);
            fileChoice = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            char[] myBuffer = new char[1024];
            int bytesRead = 0;
            BufferedReader in = new BufferedReader(new FileReader(clientPath + "/" + fileChoice));
            StringBuilder line = new StringBuilder();
            while ((bytesRead = in.read(myBuffer, 0, 1024)) != -1) {
                for (int i = 0; i < myBuffer.length; i++) {
                    line.append(myBuffer[i]);
                }
                line.delete(0, line.toString().length());
            }
            in.close();
        } catch (FileNotFoundException fnfE) {
            // file not found, handle case
        } catch (IOException ioE) {
            // problem reading, handle case
        }

    }

}
