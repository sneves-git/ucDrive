package com.ucdrive.server;

import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;

import com.ucdrive.refactorLater.MD5;
import com.ucdrive.refactorLater.ServerHelperClass;

public class PrimaryUpdateFolderOrFile extends Thread {
    private int filePort, myPort;
    private String host;
    private String server;
    private String filePath;
    private int bufsize = 1024;
    private InetAddress ia = null;
    private ByteArrayOutputStream baos = null;
    private DataOutputStream dos = null;
    private DatagramPacket dp = null;
    private DatagramPacket dr = null;
    private String type = null;

    public PrimaryUpdateFolderOrFile(int myPort, int filePort, String host, String server, String filePath,
            String type) {
        this.myPort = myPort;
        this.filePort = filePort;
        this.host = host;
        this.server = server;
        baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
        this.filePath = filePath;
        this.type = type;
        this.start();
    }

    public void run() {
        try (DatagramSocket ds = new DatagramSocket(myPort)) {
            try {

                try {
                    this.ia = InetAddress.getByName(host);
                } catch (UnknownHostException e1) {
                    return;
                }

                if (this.type.equals("Download")) {
                    sendReceiveAck("Sending file!", ds, false);
                    ServerHelperClass shc = new ServerHelperClass();
                    sendReceiveAck(shc.convertPath(filePath, server), ds, false);
                    sendFile(ds);
                } else if (this.type.equals("Delete")) {
                    sendReceiveAck("Delete", ds, false);
                    deleteOrCreateNewFolder(ds);
                } else if (this.type.equals("CreateNewFolder")) {
                    sendReceiveAck("CreateNewFolder", ds, false);
                    deleteOrCreateNewFolder(ds);
                }

            } catch (Exception e) {
                e.printStackTrace();
                // receiveAndSendAck(ds);
            }
        } catch (SocketException se) {
            se.printStackTrace();
        }

    }

    public void sendReceiveAck(String str, DatagramSocket ds, boolean sendFile) {
        try {
            // Send "Sending file!"
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeUTF(str);
            System.out.println("[Primary] 1- Mandei: " + str);

            byte[] buf = baos.toByteArray();
            dp = new DatagramPacket(buf, buf.length, ia, filePort);
            ds.send(dp);

            // Receive Ack
            byte[] rbuf = new byte[bufsize];
            dr = new DatagramPacket(rbuf, rbuf.length);
            ds.receive(dr);
            ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
            DataInputStream dis = new DataInputStream(bais);
            String ack = dis.readUTF();
            System.out.println("[Primary] 1- Recebi: " + ack);

            if (sendFile) {
                if (ack.equals("Not done!")) {
                    sendFile(ds);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receive(DatagramSocket ds) {
        try {
            // Receive Ack
            byte[] rbuf = new byte[bufsize];
            dr = new DatagramPacket(rbuf, rbuf.length);
            ds.receive(dr);
            ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
            DataInputStream dis = new DataInputStream(bais);
            String ack = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(DatagramSocket ds) {
        System.out.println(" -- SEND A FILE --");

        try {
            int nread;
            byte[] buf = new byte[bufsize];
            System.out.println("File Path: " + filePath);

            FileInputStream fis = new FileInputStream(filePath);
            try {
                int count = 0;
                do {
                    System.out.println("COUNT: " + count++);
                    nread = fis.read(buf);

                    if (nread > 0) {
                        dp = new DatagramPacket(buf, nread, ia, filePort);
                        ds.send(dp);
                    }
                } while (nread > -1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fis.close();

            receive(ds);
            MD5 md5 = new MD5();
            try {
                sendReceiveAck(md5.md5(filePath), ds, true);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteOrCreateNewFolder(DatagramSocket ds) {
        ServerHelperClass shc = new ServerHelperClass();
        sendReceiveAck(shc.convertPath(filePath, server), ds, false);
    }

}
