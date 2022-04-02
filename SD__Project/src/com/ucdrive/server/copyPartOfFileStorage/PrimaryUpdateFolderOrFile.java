package com.ucdrive.server.copyPartOfFileStorage;

import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;

import com.ucdrive.utils.MD5;
import com.ucdrive.utils.ServerHelperClass;

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
                    if (this.type.equals("Download")) {
                        int res = sendReceiveAck("Download", ds, false);
                        if(res == 0){
                            ds.close();
                            return;
                        }
                        ServerHelperClass shc = new ServerHelperClass();
                        sendReceiveAck(shc.convertPath(filePath, server), ds, false);
                        sendFile(ds);
                    } else if (this.type.equals("Delete")) {
                        int res = sendReceiveAck("Delete", ds, false);
                        if(res == 0){
                            ds.close();
                            return;
                        }
                        deleteOrCreateNewFolder(ds);
                    } else if (this.type.equals("CreateNewFolder")) {
                        int res = sendReceiveAck("CreateNewFolder", ds, false);
                        if(res == 0){
                            ds.close();
                            return;
                        }
                        deleteOrCreateNewFolder(ds);
                    }
                } catch (UnknownHostException e1) {
                    ds.close();
                    return;
                }
                ds.close();

            } catch (Exception e) {
                ds.close();
                e.printStackTrace();
                // receiveAndSendAck(ds);
            }
        } catch (SocketException se) {
            se.printStackTrace();
        }

    }

    public int sendReceiveAck(String str, DatagramSocket ds, boolean sendFile) {
        try {
            ds.setSoTimeout(10);
            // Send "Sending file!"
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
            String ack = dis.readUTF();
            if (sendFile) {

                if (ack.equals("Not done!")) {
                    sendFile(ds);
                }
            }

        }catch( SocketTimeoutException e){
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
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
