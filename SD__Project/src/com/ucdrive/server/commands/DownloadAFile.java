package com.ucdrive.server.commands;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;

public class DownloadAFile extends Thread {
    private DataOutputStream out;
    private Socket clientSocket;
    private String serverPath;
    private int buffsize;
    private String choice, server;

    public DownloadAFile(Socket aClientSocket, String serverPath, String server, String choice) {
        try {
            this.buffsize = 1024;
            this.choice = choice;
            this.clientSocket = aClientSocket;
            this.serverPath = serverPath;
            this.server = server;

            this.out = new DataOutputStream(clientSocket.getOutputStream());

            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    // =============================
    public void run() {
        /*
         * 1. recebo porto a usar
         * 2. crio a thread dando-lhe o porto,
         * 3. Crio novo socket com porto a usar (na nova thread)
         * 4. Fico à escuta
         * 5. recebo a nova ligação
         * 6. crio o novo out, in, reader
         * 7. envio o client path
         * 8. envia opcões de download para o cliente (ficheiros) na diretoria onde está
         * 9. recebo escolha do utilizador
         * 10. envio ao client o nome do ficheiro
         * 11. começo a enviar ficheiro em chunks
         * 12. envio mensagem a dizer que terminei
         * 13.
         */

        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString() + "/src/com/ucdrive/server/" + server + "/" + serverPath;
        try {
            int nread;
            byte[] buf = new byte[buffsize];

            FileInputStream fis = new FileInputStream(path + "/" + choice);
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
