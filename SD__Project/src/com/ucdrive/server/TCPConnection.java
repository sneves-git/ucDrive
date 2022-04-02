package com.ucdrive.server;

import com.ucdrive.utils.Users;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class TCPConnection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    Users users;
    String server;
    int myPort, filePort;
    String host;

    public TCPConnection(Users users, Socket aClientSocket, String server, int myPort, int filePort, String host) {
        this.users = users;
        this.server = server;
        this.myPort = myPort;
        this.filePort = filePort;
        this.host = host;
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
        }
    }

    // =============================
    public void run() {
        // First menu from server
        FirstMenu menu = new FirstMenu();
        menu.firstMenu(users, in, out, server, myPort, filePort, host);
    }
}
