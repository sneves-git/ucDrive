package com.ucdrive.server.copyAllFileStorage;

import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;

import com.ucdrive.utils.MD5;
import com.ucdrive.utils.ServerHelperClass;

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

        File dir = new File(startDirectory);
        File[] files = dir.listFiles();
        String ackReturn = null;

        try {
            if (files != null && files.length > 0) {
                for (File file : files) {
                    System.out.println("filename: " + file.getName());
                    ServerHelperClass shc = new ServerHelperClass();

                    // Check if the file is a directory
                    if (file.isDirectory()) {
                        // Send the word "Folder"
                        sendAndReceiveAck("Folder", ds, false);

                        // Send directory
                        sendAndReceiveAck(shc.convertPath(file.getPath(), this.server), ds, false);

                        // Recursive function
                        getAllDirectoryFiles(file.getPath(), ds);
                    } else {
                        // Send the word "File"
                        System.out.println("antes file");
                        sendAndReceiveAck("File", ds, false);

                        // Send File Path
                        System.out.println("antes path");

                        sendAndReceiveAck(shc.convertPath(file.getPath(), this.server), ds, false);

                        // Send file with MD5
                        MD5 obj = new MD5();
                        System.out.println("antes md5");

                        String md5 = obj.md5(file.getPath());
                        ackReturn = sendAndReceiveAck(md5, ds, true);
                        System.out.println("ackReturn: " + ackReturn);
                        if (ackReturn.equals("File doesn't exist!")) {
                            System.out.println("ficheiro n existe");

                            String ack = null;
                            do {
                                sendFile(file.getPath(), ds);

                                byte[] rbuf = new byte[bufsize];
                                dr = new DatagramPacket(rbuf, rbuf.length);
                                ds.receive(dr);
                                ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
                                DataInputStream dis = new DataInputStream(bais);
                                ack = dis.readUTF();
                                System.out.println("ACK FINAL: " + ack);
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

    private void sendFile(String filePath, DatagramSocket ds) throws NoSuchAlgorithmException {
        try {
            int nread;
            byte[] buf = new byte[bufsize];

            FileInputStream fis = new FileInputStream(filePath);
            try {
                do {
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
            System.out.println("TERMINEI ALBERTO");

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

            try {
                this.ia = InetAddress.getByName(host);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }

            if (ack.equals(word)) {
                // Send byte array
                baos = new ByteArrayOutputStream();
                dos = new DataOutputStream(baos);
                dos.writeUTF("ACK");

                byte[] buf = baos.toByteArray();
                dp = new DatagramPacket(buf, buf.length, ia, filePort);
                ds.send(dp);
            }

        } catch (Exception e) {
            e.printStackTrace();
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

            // Receive Ack
            byte[] rbuf = new byte[bufsize];
            dr = new DatagramPacket(rbuf, rbuf.length);
            ds.receive(dr);
            ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
            DataInputStream dis = new DataInputStream(bais);

            ack = dis.readUTF();
            return ack;

        } catch (Exception e) {
            System.out.println("CATCH");
        }
        return ack;
    }
}
