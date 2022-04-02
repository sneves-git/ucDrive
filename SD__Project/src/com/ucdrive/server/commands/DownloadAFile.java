package com.ucdrive.server.commands;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;

import com.ucdrive.configs.UsersConfigsFile;
import com.ucdrive.utils.User;
import com.ucdrive.utils.Users;

public class DownloadAFile extends Thread {
    private DataOutputStream out;
    private Socket clientSocket;
    private String serverPath;
    private int buffsize;
    private String choice, server;
    private User user;
    private Users users;
    private int lastChoice;

    public DownloadAFile(Socket aClientSocket, String serverPath, String server, String choice, User user, Users users,
            int lastChoice) {
        try {
            this.buffsize = 1024;
            this.choice = choice;
            this.clientSocket = aClientSocket;
            this.serverPath = serverPath;
            this.server = server;
            this.user = user;
            this.users = users;
            this.lastChoice = lastChoice;

            this.out = new DataOutputStream(clientSocket.getOutputStream());

            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    // =============================
    public void run() {

        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString() + "/src/com/ucdrive/server/" + server + "/"
                + serverPath;
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
            } catch (Exception e) {
                e.printStackTrace();
                fis.close();
                return;

            }
            fis.close();
            clientSocket.close();

            UsersConfigsFile config = new UsersConfigsFile();
            config.updateLastChoice(lastChoice, user, users);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
