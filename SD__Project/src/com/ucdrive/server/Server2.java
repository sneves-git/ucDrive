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

public class Server2 {
    private static String server = "server2";

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
        String path = "/src/com/ucdrive/configs/secondary_server_ip_port.txt";
        String totalPath = Paths.get(s, path).toString();
        File file = new File(totalPath);

        String server1Ip = null;
        int serverPort = -1, udpPortServer1 = -1, udpPortServer2 = -1, numero = 0;
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
                server1Ip = sc.nextLine();
            }
            if (sc.hasNextLine()) {
                udpPortServer2 = Integer.parseInt(sc.nextLine());
            }
            if (sc.hasNextLine()) {
                udpPortServer1 = Integer.parseInt(sc.nextLine());
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
        System.out.println("Socket Datagram à escuta no porto " + udpPortServer2);

        ServerHelperClass helper = new ServerHelperClass();
        String status = helper.determineIfPrimaryOrSecondary(udpPortServer1, server1Ip);
        if (status.equals("Primary")) {
            System.out.println("sou primario");
            new PrimaryHeartbeats(udpPortServer2);

            // TCP connection
            System.out.println("A Escuta no Porto " + serverPort);
            try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
                System.out.println("LISTEN SOCKET=" + listenSocket);
                while (true) {
                    Socket clientSocket = listenSocket.accept(); // BLOQUEANTE

                    System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                    numero++;
                    new TCPConnection_2(users, clientSocket, numero, server);
                }
            } catch (Exception e) {
                System.out.println("Error with server socket: " + e);
            }

        } else {

            Thread thread = new SecondaryHeartbeats(udpPortServer1, server1Ip);
            thread.join();
            System.out.println("SOU PRIMARIO DEPOIS DE O OUTRO TER morrido");
            new PrimaryHeartbeats(udpPortServer2);

            // TCP connection
            System.out.println("A Escuta no Porto " + serverPort);
            try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
                System.out.println("LISTEN SOCKET=" + listenSocket);
                while (true) {
                    Socket clientSocket = listenSocket.accept(); // BLOQUEANTE

                    System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                    numero++;
                    new TCPConnection_2(users, clientSocket, numero, server);
                }
            } catch (Exception e) {
                System.out.println("Error with server socket: " + e);
            }
        }

    }
}

class TCPConnection_2 extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int thread_number;
    Users users;
    User user;
    String server;

    public TCPConnection_2(Users users, Socket aClientSocket, int numero, String server) {
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

class UDPFileStorageReceiver_2 extends Thread {
    private DatagramSocket socket;
    private String serverName;

    public UDPFileStorageReceiver_2() {
        this.serverName = serverName;
        this.socket = socket;
        this.start();
    }

    // =============================
    public void run() {
        // If it is receiving then it means that the primary server is Server1

        /*
         * RECEIVE SOMETHING IN UDP
         * while(true){
         * byte[] buffer = new byte[1000];
         * DatagramPacket request = new DatagramPacket(buffer, buffer.length);
         * socket.receive(request);
         * s=new String(request.getData(), 0, request.getLength());
         * System.out.println("Server Recebeu: " + s);
         * 
         * DatagramPacket reply = new DatagramPacket(request.getData(),
         * request.getLength(), request.getAddress(), request.getPort());
         * socket.send(reply);
         * }
         */
    }
}

class UDPFileStorageSender_2 extends Thread {
    private DatagramSocket socket;
    private String server2Name;

    public UDPFileStorageSender_2() {
        this.server2Name = server2Name;
        this.socket = socket;
        this.start();
    }

    // =============================
    public void run() {
        // If it is sending then it means that I (server2) am the primary server

        /*
         * SEND SOMETHING IN UDP
         * InputStreamReader input = new InputStreamReader(System.in);
         * BufferedReader reader = new BufferedReader(input);
         * 
         * while (true) {
         * System.out.print("Mensagem a enviar = ");
         * // READ STRING FROM KEYBOARD
         * try {
         * texto = reader.readLine();
         * 
         * } catch (Exception e) {
         * }
         * 
         * byte[] m = texto.getBytes();
         * 
         * InetAddress aHost = InetAddress.getByName("10.16.1.47"); <--- ip do servidor
         * server1
         * int serverPort = 6789;
         * DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
         * socket.send(request);
         * byte[] buffer = new byte[1000];
         * DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
         * socket.receive(reply);
         * System.out.println("Recebeu: " + new String(reply.getData(), 0,
         * reply.getLength()));
         * }
         */
    }
}

class UDPHeartbeatSender_2 extends Thread {
    private DatagramSocket socket;

    public UDPHeartbeatSender_2() {
        this.socket = socket;
        this.start();
    }

    // =============================
    public void run() {
        // If it is sending heartbeats then it means that I (server 2) am the primary
        // server

        /*
         * SEND SOMETHING IN UDP - Heartbeats example sender
         * try (DatagramSocket ds = new DatagramSocket(port)) {
         * while (true) {
         * byte buf[] = new byte[bufsize];
         * DatagramPacket dp = new DatagramPacket(buf, buf.length);
         * ds.receive(dp);
         * ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
         * DataInputStream dis = new DataInputStream(bais);
         * int count = dis.readInt();
         * 
         * ByteArrayOutputStream baos = new ByteArrayOutputStream();
         * DataOutputStream dos = new DataOutputStream(baos);
         * dos.writeInt(count);
         * byte resp[] = baos.toByteArray();
         * DatagramPacket dpresp = new DatagramPacket(resp, resp.length,
         * dp.getAddress(), dp.getPort());
         * ds.send(dpresp);
         * }
         * }
         */
    }
}

class UDPHeartbeatReceiver_2 extends Thread {
    private DatagramSocket socket;
    private static final int maxfailedrounds = 5;
    private static final int timeout = 5000;
    private static final int bufsize = 4096;
    private static final int port = 7000;
    private static final int period = 10000;

    public UDPHeartbeatReceiver_2() {
        this.socket = socket;
        this.start();
    }

    // =============================
    public void run() {
        // If it is receiving heartbeats then it means that I (server 2) am the
        // secondary server

        /*
         * SEND SOMETHING IN UDP - Heartbeats example receiver
         * InetAddress ia = InetAddress.getByName("localhost"); <--- ip do servidor
         * server1
         * try (DatagramSocket ds = new DatagramSocket()) {
         * ds.setSoTimeout(timeout);
         * int failedheartbeats = 0;
         * while (failedheartbeats < maxfailedrounds) {
         * try {
         * ByteArrayOutputStream baos = new ByteArrayOutputStream();
         * DataOutputStream dos = new DataOutputStream(baos);
         * dos.writeInt(count++);
         * byte [] buf = baos.toByteArray();
         * 
         * DatagramPacket dp = new DatagramPacket(buf, buf.length, ia, port);
         * ds.send(dp);
         * 
         * byte [] rbuf = new byte[bufsize];
         * DatagramPacket dr = new DatagramPacket(rbuf, rbuf.length);
         * 
         * ds.receive(dr);
         * failedheartbeats = 0;
         * ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0,
         * dr.getLength());
         * DataInputStream dis = new DataInputStream(bais);
         * int n = dis.readInt();
         * System.out.println("Got: " + n + ".");
         * }
         * catch (SocketTimeoutException ste) {
         * failedheartbeats++;
         * System.out.println("Failed heartbeats: " + failedheartbeats);
         * }
         * Thread.sleep(period);
         * }
         * }
         */
    }
}
