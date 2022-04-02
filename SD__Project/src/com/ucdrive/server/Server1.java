package com.ucdrive.server;

import com.ucdrive.configs.UsersConfigsFile;
import com.ucdrive.utils.ServerHelperClass;
import com.ucdrive.utils.Users;
import com.ucdrive.server.copyAllFileStorage.PrimaryFileStorage;
import com.ucdrive.server.heartbeats.PrimaryHeartbeats;
import com.ucdrive.server.copyAllFileStorage.SecondaryFileStorage;
import com.ucdrive.server.heartbeats.SecondaryHeartbeats;

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
        int serverPort = -1, udpPortServer1 = -1, udpPortServer2 = -1, filePortServer1 = -1,
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
        String a = currentRelativePath.toAbsolutePath().toString();
        String p = "/src/com/ucdrive/server/" + server + "/Home/";
        String startDir = Paths.get(a, p).toString();

        ServerHelperClass helper = new ServerHelperClass();
        String status = helper.determineIfPrimaryOrSecondary(udpPortServer2,
                server2Ip);
        if (status.equals("Primary")) {
            new PrimaryHeartbeats(udpPortServer1);
            new PrimaryFileStorage(filePortServer1, filePortServer2, server2Ip, startDir, server);

            // TCP connection
            try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
                System.out.println("[TCP] IP="+ listenSocket.getInetAddress() + " Port="+serverPort);

                while (true) {
                    Socket clientSocket = listenSocket.accept(); // BLOQUEANTE

                    System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                    new TCPConnection(users, clientSocket, server);
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


            new PrimaryHeartbeats(udpPortServer1);
            new PrimaryFileStorage(filePortServer1, filePortServer2, server2Ip, startDir,
                    server);

            // TCP connection
            try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
                System.out.println("[TCP] IP="+ listenSocket.getInetAddress() + " Port="+serverPort);

                while (true) {
                    Socket clientSocket = listenSocket.accept(); // BLOQUEANTE

                    System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                    new TCPConnection(users, clientSocket, server);
                }
            } catch (Exception e) {
                System.out.println("Error with server socket: " + e);
            }
        }
    }

}

