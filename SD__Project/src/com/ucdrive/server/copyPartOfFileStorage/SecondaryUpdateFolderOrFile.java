package com.ucdrive.server.copyPartOfFileStorage;

import java.io.*;
import java.net.*;
import java.nio.file.*;

import com.ucdrive.utils.MD5;
import com.ucdrive.utils.ServerHelperClass;

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


                while(true){
                    ds.setSoTimeout(0);
                    try {
                        this.ia = InetAddress.getByName(host);
                        String ack = receiveAndSend_(ds, false);
                        if (ack.equals("Download")) {
                            String path = receiveAndSend_(ds, false);
                            this.filePath = s + path;
                            receiveFile(ds);
                        } else if (ack.equals("Delete")) {
                            String fileDirectory = receiveAndSend_(ds, false);
                            ServerHelperClass shc = new ServerHelperClass();
                            String serverNameNew = "server1";
                            if(serverName.equals("server1")){
                                serverNameNew = "server2";
                            }
                            fileDirectory = Paths.get(fileDirectory).toString();
                            String path_ = shc.convertPath(fileDirectory, serverNameNew);
                            path_ = path_.replace("/","\\" );
                            File f = new File(s+path_);
                            f.delete();

                        } else if (ack.equals("CreateNewFolder")) {
                            String folderPath = receiveAndSend_(ds, false);
                            folderPath = Paths.get(folderPath).toString();
                            String serverNameNew = "server1";
                            if(serverName.equals("server1")){
                                serverNameNew = "server2";
                            }
                            ServerHelperClass shc = new ServerHelperClass();
                            String path_ = shc.convertPath(folderPath, serverNameNew);


                            File f = new File(s + path_);
                            f.mkdir();

                        }
                        ia = null;
                    } catch (UnknownHostException e1) {
                        return;
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SocketException se) {
            se.printStackTrace();
        }
    }

    public String receiveAndSend_(DatagramSocket ds, boolean receiveFile_) {
        String ack = null;
        try {
            // Receive Ack
            byte[] rbuf = new byte[bufsize];
            dr = new DatagramPacket(rbuf, rbuf.length);
            ds.receive(dr);
            ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
            DataInputStream dis = new DataInputStream(bais);
            ack = dis.readUTF();

            if (receiveFile_) {
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

                byte[] buf = baos.toByteArray();
                dp = new DatagramPacket(buf, buf.length, ia, filePort);
                ds.send(dp);
            }

        } catch (Exception e) {
            e.printStackTrace();
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

            int length;
            FileOutputStream fos = new FileOutputStream(filePath);
            do {
                ds.setSoTimeout(10);
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
            sendAck(ds, "Done");
            receiveAndSend_(ds, true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
