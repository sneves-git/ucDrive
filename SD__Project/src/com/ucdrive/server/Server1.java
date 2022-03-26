package com.ucdrive.server;

import com.ucdrive.server.commands.Login;
import com.ucdrive.server.commands.ChangePassword;

import com.ucdrive.configs.UsersConfigsFile;
import com.ucdrive.refactorLater.Folder;
import com.ucdrive.refactorLater.ServerHelperClass;
import com.ucdrive.refactorLater.User;
import com.ucdrive.refactorLater.Users;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Server1 {

    public static void main(String[] args) {
        Users users = new Users();
        UsersConfigsFile usersConfigsFile = new UsersConfigsFile();
        usersConfigsFile.readUsersInfo(users);

        ServerHelperClass helper = new ServerHelperClass();
        String startDir = ".";

        ServerFoldersCheck check = new ServerFoldersCheck();
        check.checkingFolders(users.getUsers());

        Folder home = new Folder(startDir);
        helper.listFiles(home, startDir);

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
                    new Connection(users, clientSocket, numero);
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
    Users users;
    User user;

    public Connection(Users users, Socket aClientSocket, int numero) {
        thread_number = numero;
        this.users = users;
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
                // Receives signal that user wants to log in
                String data = in.readUTF();
                switch (data) {
                    case "login":
                        Login login = new Login();
                        this.user = login.login_(this.users, in, out);

                        AuthenticatedMenu authMenu = new AuthenticatedMenu();
                        authMenu.authenticatedMenu(this.user, this.users, this.in, this.out);

                        break;
                    case "changePassword":
                        ChangePassword change = new ChangePassword();
                        change.changePassword(user, users, in, out);

                }

                /*
                 * // an echo server
                 * String data = in.readUTF();
                 * System.out.println("T[" + thread_number + "] Recebeu: " + data);
                 * resposta = data.toUpperCase();
                 * out.writeUTF(resposta);
                 */
            }
        } catch (EOFException e) {
            System.out.println("EOF:" + e);
        } catch (IOException e) {
            System.out.println("IO:" + e);
        }
    }
}
