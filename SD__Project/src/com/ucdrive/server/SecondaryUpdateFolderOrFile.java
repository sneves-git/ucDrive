package com.ucdrive.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import com.ucdrive.refactorLater.MD5;

public class SecondaryUpdateFolderOrFile extends Thread {
    private int filePort;
    private String host;
    private int bufsize = 1024;
    private String filePath = null;
    private InetAddress ia = null;
    private ByteArrayOutputStream baos = null;
    private DataOutputStream dos = null;
    private DatagramPacket dp = null;
    private DatagramPacket dr = null;
    private int myPort;
    private String serverName = null;

    public SecondaryUpdateFolderOrFile(int myPort, int filePort, String host, String serverName) {
        this.filePort = filePort;
        this.host = host;
        this.myPort = myPort;
        this.serverName = serverName;
        this.start();
    }

    public void run() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString() + "/src/com/ucdrive/server/"
                + this.serverName + "/";

        try (DatagramSocket ds = new DatagramSocket(myPort)) {
            try {
                try {
                    this.ia = InetAddress.getByName(host);
                } catch (UnknownHostException e1) {
                    return;
                }

                while (true) {
                    String ack = receiveAndSend_(ds, false);
                    if (ack.equals("Download")) {
                        String path = receiveAndSend_(ds, false);
                        this.filePath = s + path;
                        receiveFile(ds);
                    } else if (ack.equals("Delete")) {
                        String fileDirectory = receiveAndSend_(ds, false);
                        File f = new File(s + fileDirectory);
                        if (f.delete()) {
                            System.out.println("Folder deleted!");
                        } else {
                            System.out.println("Could not delete folder!");
                        }
                    } else if (ack.equals("CreateNewFolder")) {
                        String folderPath = receiveAndSend_(ds, false);
                        File f = new File(s + folderPath);
                        if (f.mkdir()) {
                            System.out.println("Directory created!");
                        } else {
                            System.out.println("Directory cannot be created!");
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                // receiveAndSendAck(ds);
            }
        } catch (SocketException se) {
            se.printStackTrace();
        }
    }

    public String receiveAndSend_(DatagramSocket ds, boolean receiveFile) {
        String ack = null;
        try {
            // Receive Ack
            byte[] rbuf = new byte[bufsize];
            dr = new DatagramPacket(rbuf, rbuf.length);
            ds.receive(dr);
            ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
            DataInputStream dis = new DataInputStream(bais);
            ack = dis.readUTF();
            System.out.println("[Secondary] 1- Recebi: " + ack);

            if (receiveFile) {
                MD5 obj = new MD5();
                if (!(obj.md5(filePath).equals(ack))) {
                    sendAck(ds, "Not done!");
                    receiveFile(ds);
                } else {
                    sendAck(ds, "Done!");
                }
            } else {
                // Send byte array
                baos = new ByteArrayOutputStream();
                dos = new DataOutputStream(baos);
                dos.writeUTF(ack);
                System.out.println("[Secondary] 1- Mandei: " + ack);

                byte[] buf = baos.toByteArray();
                dp = new DatagramPacket(buf, buf.length, ia, filePort);
                ds.send(dp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // receiveAndSendAck(ds);
        }
        return ack;
    }

    public void sendAck(DatagramSocket ds, String str) {
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeUTF(str);
            byte[] buf = baos.toByteArray();
            dp = new DatagramPacket(buf, buf.length, ia, filePort);
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveFile(DatagramSocket ds) {

        try {

            // Receive
            byte[] rbuf = new byte[bufsize];
            dr = new DatagramPacket(rbuf, rbuf.length);

            int length, count = 0;
            FileOutputStream fos = new FileOutputStream(filePath);
            do {
                ds.setSoTimeout(10);
                System.out.println("COUNT: " + count++);
                try {
                    ds.receive(dr);
                } catch (SocketTimeoutException ste) {
                    break;
                }

                ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
                DataInputStream dis = new DataInputStream(bais);

                length = dis.read(rbuf, 0, rbuf.length);
                if (length > 0) {
                    fos.write(rbuf, 0, length);
                }
            } while (true);
            fos.close();
            sendAck(ds, "Done!");
            receiveAndSend_(ds, true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
