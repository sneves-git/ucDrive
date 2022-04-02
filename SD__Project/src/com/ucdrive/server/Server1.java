package com.ucdrive.server;

import com.ucdrive.configs.UsersConfigsFile;
import com.ucdrive.refactorLater.ServerHelperClass;
import com.ucdrive.refactorLater.User;
import com.ucdrive.refactorLater.Users;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Server1 {
    private static String server = "server1";

    public static void main(String[] args) throws InterruptedException {
        // Extracts users information from users.txt
        Users users = new Users();
        UsersConfigsFile usersConfigsFile = new UsersConfigsFile();
        usersConfigsFile.readUsersInfo(users);

        // Checks if server already has a Home directory, if not, one is created
        ServerFoldersCheck check = new ServerFoldersCheck();
        check.checkingFolders(users.getUsers(), server);

        // Extracts ip address of server, client port and udp port
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String path = "/src/com/ucdrive/configs/primary_server_ip_port.txt";
        String totalPath = Paths.get(s, path).toString();
        File file = new File(totalPath);

        String server2Ip = null;
        int serverPort = -1, udpPortServer1 = -1, udpPortServer2 = -1, numero = 0, filePortServer1 = -1,
                filePortServer2 = -1, updateFolderOrFilePort1 = -1, updateFolderOrFilePort2 = -1;
        Scanner sc = null;
        try {
            sc = new Scanner(file);

            if (sc.hasNextLine()) {
                sc.nextLine();
            }
            if (sc.hasNextLine()) {
                serverPort = Integer.parseInt(sc.nextLine());
            }
            if (sc.hasNextLine()) {
                server2Ip = sc.nextLine();
            }
            if (sc.hasNextLine()) {
                udpPortServer1 = Integer.parseInt(sc.nextLine());
            }
            if (sc.hasNextLine()) {
                udpPortServer2 = Integer.parseInt(sc.nextLine());
            }
            if (sc.hasNextLine()) {
                filePortServer1 = Integer.parseInt(sc.nextLine());
            }
            if (sc.hasNextLine()) {
                filePortServer2 = Integer.parseInt(sc.nextLine());
            }
            if (sc.hasNextLine()) {
                updateFolderOrFilePort1 = Integer.parseInt(sc.nextLine());
            }
            if (sc.hasNextLine()) {
                updateFolderOrFilePort2 = Integer.parseInt(sc.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (sc != null) {
                sc.close();
            }
        }

        // UDP connection
        System.out.println("Socket Datagram à escuta no porto " + udpPortServer1);
        String a = currentRelativePath.toAbsolutePath().toString();
        String p = "/src/com/ucdrive/server/" + server + "/Home/";
        String startDir = Paths.get(a, p).toString();

        ServerHelperClass helper = new ServerHelperClass();
        String status = helper.determineIfPrimaryOrSecondary(udpPortServer2,
                server2Ip);
        if (status.equals("Primary")) {
            System.out.println("sou primario");
            new PrimaryHeartbeats(udpPortServer1);
            new PrimaryFileStorage(filePortServer1, filePortServer2, server2Ip, startDir, server);

            // TCP connection
            System.out.println("A Escuta no Porto " + serverPort);
            try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
                System.out.println("LISTEN SOCKET=" + listenSocket);
                while (true) {
                    Socket clientSocket = listenSocket.accept(); // BLOQUEANTE

                    System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                    numero++;
                    new TCPConnection(users, clientSocket, numero, server);
                }
            } catch (Exception e) {
                System.out.println("Error with server socket: " + e);
            }
        } else {
            Thread heartbeatThread = new SecondaryHeartbeats(udpPortServer2, server2Ip);
            Thread fileThread = new SecondaryFileStorage(filePortServer1,
                    filePortServer2, server2Ip, server);
            heartbeatThread.join();
            fileThread.join();

            System.out.println("SOU PRIMARIO DEPOIS DE O OUTRO TER morrido");

            new PrimaryHeartbeats(udpPortServer1);
            new PrimaryFileStorage(filePortServer1, filePortServer2, server2Ip, startDir,
                    server);

            // TCP connection
            System.out.println("A Escuta no Porto " + serverPort);
            try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
                System.out.println("LISTEN SOCKET=" + listenSocket);
                while (true) {
                    Socket clientSocket = listenSocket.accept(); // BLOQUEANTE

                    System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                    numero++;
                    new TCPConnection(users, clientSocket, numero, server);
                }
            } catch (Exception e) {
                System.out.println("Error with server socket: " + e);
            }
        }
    }

}

class TCPConnection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int thread_number;
    Users users;
    User user;
    String server;

    public TCPConnection(Users users, Socket aClientSocket, int numero, String server) {
        thread_number = numero;
        this.users = users;
        this.server = server;
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
        // First menu from server
        FirstMenu menu = new FirstMenu();
        menu.firstMenu(users, in, out, server);
    }
}
