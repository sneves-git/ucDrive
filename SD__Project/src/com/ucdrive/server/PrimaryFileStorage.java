package com.ucdrive.server;

import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;

import com.ucdrive.refactorLater.MD5;
import com.ucdrive.refactorLater.ServerHelperClass;

public class PrimaryFileStorage extends Thread {
    private int filePort, myPort;
    private String host;
    private String server;
    private int bufsize = 1024;
    private String startDir;
    private InetAddress ia = null;
    private ByteArrayOutputStream baos = null;
    private DataOutputStream dos = null;
    private DatagramPacket dp = null;
    private DatagramPacket dr = null;

    public PrimaryFileStorage(int myPort, int filePort, String host, String startDir, String server) {
        this.myPort = myPort;
        this.filePort = filePort;
        this.host = host;
        this.startDir = startDir;
        this.server = server;
        baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
        this.start();
    }

    public void run() {

        try (DatagramSocket ds = new DatagramSocket(myPort)) {
            receiveAndSendAck(ds, "Start");

            getAllDirectoryFiles(startDir, ds);
            sendAndReceiveAck("Finished", ds, false);
        } catch (SocketException se) {
            se.printStackTrace();
        } catch (NoSuchAlgorithmException nsa) {
            nsa.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public void getAllDirectoryFiles(String startDirectory, DatagramSocket ds)
            throws NoSuchAlgorithmException, IOException {

        System.out.println("[getAllDirectoryFiles] startDir: " + startDirectory);
        File dir = new File(startDirectory);
        File[] files = dir.listFiles();
        String ackReturn = null;

        try {
            if (files != null && files.length > 0) {
                for (File file : files) {
                    System.out.println("--------- FILE: " + file.getName() + " ---------------");
                    ServerHelperClass shc = new ServerHelperClass();

                    // Check if the file is a directory
                    if (file.isDirectory()) {
                        // Send the word "Folder"
                        System.out.println("before sending folder");

                        sendAndReceiveAck("Folder", ds, false);
                        System.out.println("After sending folder");

                        // Send directory
                        System.out.println("before sending path");

                        System.out.println("[getAllDirectoryFiles] shc.convertPath: "
                                + shc.convertPath(file.getPath(), this.server));

                        sendAndReceiveAck(shc.convertPath(file.getPath(), this.server), ds, false);
                        System.out.println("After sending path");

                        // Recursive function
                        System.out.println("[getAllDirectoryFiles] file.getPath: " + file.getPath());

                        getAllDirectoryFiles(file.getPath(), ds);
                    } else {
                        // Send the word "File"
                        System.out.println("before sending file");

                        sendAndReceiveAck("File", ds, false);
                        System.out.println("after sending file");

                        // Send File Path
                        sendAndReceiveAck(shc.convertPath(file.getPath(), this.server), ds, false);

                        // Send file with MD5
                        MD5 obj = new MD5();

                        String md5 = obj.md5(file.getPath());
                        ackReturn = sendAndReceiveAck(md5, ds, true);
                        if (ackReturn.equals("File doesn't exist!")) {
                            String ack = null;
                            do {
                                System.out.println("[getAllDirectoryFiles] file.getPath: " + file.getPath());

                                sendFile(file.getPath(), ds, md5);

                                byte[] rbuf = new byte[bufsize];
                                dr = new DatagramPacket(rbuf, rbuf.length);
                                ds.receive(dr);
                                ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
                                DataInputStream dis = new DataInputStream(bais);
                                ack = dis.readUTF();
                            } while (!ack.equals("Done!"));

                        }
                    }
                }
            }
        } catch (NoSuchAlgorithmException nsa) {
            nsa.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void sendFile(String filePath, DatagramSocket ds, String md5Primary) throws NoSuchAlgorithmException {
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
                        // sendAndReceiveAck("Packet!", ds, false);
                        dp = new DatagramPacket(buf, nread, ia, filePort);
                        ds.send(dp);
                    }
                } while (nread > -1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // sendAndReceiveAck("Parei!", ds, false);
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void receiveAndSendAck(DatagramSocket ds, String word) {
        String ack = null;
        try {
            // Receive Ack
            byte[] rbuf = new byte[bufsize];
            dr = new DatagramPacket(rbuf, rbuf.length);
            ds.receive(dr);
            ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
            DataInputStream dis = new DataInputStream(bais);
            ack = dis.readUTF();
            System.out.println("[Primary] 1- Recebi: " + ack);

            try {
                this.ia = InetAddress.getByName(host);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }

            if (ack.equals(word)) {
                // Send byte array
                baos = new ByteArrayOutputStream();
                dos = new DataOutputStream(baos);
                dos.writeUTF("OLA");
                System.out.println("[Primary] 1- Mandei: " + "OLA");

                byte[] buf = baos.toByteArray();
                dp = new DatagramPacket(buf, buf.length, ia, filePort);
                ds.send(dp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // receiveAndSendAck(ds);
        }
    }

    private String sendAndReceiveAck(String str, DatagramSocket ds, boolean file) {
        String ack = null;
        try {
            // Send byte array
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeUTF(str);
            byte[] buf = baos.toByteArray();
            dp = new DatagramPacket(buf, buf.length, ia, filePort);

            ds.send(dp);
            System.out.println("Waiting ack");

            // Receive Ack
            byte[] rbuf = new byte[bufsize];
            dr = new DatagramPacket(rbuf, rbuf.length);
            ds.receive(dr);
            ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
            DataInputStream dis = new DataInputStream(bais);

            ack = dis.readUTF();
            System.out.println("After receiving ack: " + ack);
            return ack;

        } catch (Exception e) {
            // sendAndReceiveAck(str, ds, file);
        }
        return ack;
    }
}
