package com.ucdrive.server.commands;

import java.io.*;
import java.net.Socket;

public class DownloadAFile extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int thread_number;

    public DownloadAFile(Socket aClientSocket){
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    // =============================
    public void run() {
        /*
        1. recebo porto a usar
        2. crio a thread dando-lhe o porto, 
        3. Crio novo socket com porto a usar (na nova thread)
        4. Fico à escuta
        5. recebo a nova ligação 
        6. crio o novo out, in, reader
        7. envio o client path
        8. envia opcões de download para o cliente (ficheiros) na diretoria onde está
        9. recebo escolha do utilizador
        10. envio ao client o nome do ficheiro
        11. começo a enviar ficheiro em chunks
        12. envio mensagem a dizer que terminei
        13.
        */
    }
    

}
