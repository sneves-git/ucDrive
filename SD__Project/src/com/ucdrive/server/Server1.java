package com.ucdrive.server;

import com.ucdrive.refactorLater.Folder;
import com.ucdrive.refactorLater.ServerHelperClass;


import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Server1 {

    public static void main(String[] args) {

        ServerHelperClass helper = new ServerHelperClass();
        String startDir = ".";

        Folder home = new Folder(startDir);
        helper.listFiles(home, startDir);

        System.out.println(home);


        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();

        String path = "/src/com/ucdrive/configs/configs_ip_port.txt";
        String totalPath = Paths.get(s, path).toString();
        File file = new File(totalPath);

        try {
            Scanner sc = new Scanner(file);

            int serverPort = -1, numero = 0;
            if (sc.hasNextLine()) {
                sc.nextLine();
            }
            if (sc.hasNextLine()) {
                serverPort = Integer.parseInt(sc.nextLine());
            }

            System.out.println("A Escuta no Porto " + serverPort);
            try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
                System.out.println("LISTEN SOCKET=" + listenSocket);
                while (true) {
                    Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                    System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                    numero++;
                    new Connection(clientSocket, numero);
                }
            } catch (Exception e) {
                System.out.println("Error with server socket: " + e);
            }
        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
            e.printStackTrace();
        }

    }
}

class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int thread_number;

    public Connection(Socket aClientSocket, int numero) {
        thread_number = numero;
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
        String resposta;
        try {
            while (true) {
                // an echo server
                String data = in.readUTF();
                System.out.println("T[" + thread_number + "] Recebeu: " + data);
                resposta = data.toUpperCase();
                out.writeUTF(resposta);
            }
        } catch (EOFException e) {
            System.out.println("EOF:" + e);
        } catch (IOException e) {
            System.out.println("IO:" + e);
        }
    }
}
