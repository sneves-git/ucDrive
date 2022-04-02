package com.ucdrive.server.copyAllFileStorage;

import com.ucdrive.utils.MD5;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class SecondaryFileStorage extends Thread {
    private int filePort;
    private String host;
    private int bufsize = 1024;

    private InetAddress ia = null;
    private ByteArrayOutputStream baos = null;
    private DataOutputStream dos = null;
    private DatagramPacket dp = null;
    private DatagramPacket dr = null;
    private int myPort;
    private String serverName = null;

    public SecondaryFileStorage(int myPort, int filePort, String host, String serverName) {
        this.filePort = filePort;
        this.host = host;
        this.myPort = myPort;
        this.serverName = serverName;
        this.start();
    }

    public void run() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String p = "/src/com/ucdrive/server/" + this.serverName + "/Home";
        String totalPath = Paths.get(s, p).toString();
        baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);

        try {
            this.ia = InetAddress.getByName(host);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }

        try (DatagramSocket ds = new DatagramSocket(myPort)) {
            sendAndReceiveAck("Start", ds);
            receiveAndSendAck(ds, totalPath);
        } catch (SocketException se) {
            se.printStackTrace();
        }

    }

    private boolean receiveAndSendAck_(DatagramSocket ds, String word) {
        String ack = null;
        try {
            // Receive Ack
            byte[] rbuf = new byte[bufsize];
            dr = new DatagramPacket(rbuf, rbuf.length);
            ds.receive(dr);
            ByteArrayInputStream bais_ = new ByteArrayInputStream(rbuf, 0, dr.getLength());
            DataInputStream dis_ = new DataInputStream(bais_);
            ack = dis_.readUTF();

            try {
                this.ia = InetAddress.getByName(host);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }

            if (ack.equals(word)) {

                // Send byte array
                ByteArrayOutputStream baos_ = new ByteArrayOutputStream();
                DataOutputStream dos_ = new DataOutputStream(baos_);
                dos_.writeUTF(ack);

                byte[] buf = baos_.toByteArray();
                dp = new DatagramPacket(buf, buf.length, ia, filePort);
                ds.send(dp);

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendAndReceiveAck(String str, DatagramSocket ds) {
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeUTF(str);

            byte[] buf = baos.toByteArray();

            dp = new DatagramPacket(buf, buf.length, ia, filePort);
            ds.send(dp);

            byte[] rbuf = new byte[bufsize];
            dr = new DatagramPacket(rbuf, rbuf.length);

            ds.receive(dr);
            ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
            DataInputStream dis = new DataInputStream(bais);
            String n = dis.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveAndSendAckMD5(String path, String md5, DatagramSocket ds, boolean fileExists) {
        String ack = null;
        try {
            // Receive md5
            byte[] rbuf = new byte[bufsize];
            dr = new DatagramPacket(rbuf, rbuf.length);
            ds.receive(dr);
            ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
            DataInputStream dis = new DataInputStream(bais);
            ack = dis.readUTF();
            System.out.println("ack:" + ack);
            if (fileExists && ack.equals(md5)) {
                // Send byte array
                System.out.println("FIle exists ");

                baos = new ByteArrayOutputStream();
                dos = new DataOutputStream(baos);
                dos.writeUTF(ack);
                byte[] buf = baos.toByteArray();
                dp = new DatagramPacket(buf, buf.length, ia, filePort);
                ds.send(dp);
            } else{
                baos = new ByteArrayOutputStream();
                dos = new DataOutputStream(baos);
                System.out.println("FIle dowsn't exist ");
                dos.writeUTF("File doesn't exist!");
                byte[] buf = baos.toByteArray();
                dp = new DatagramPacket(buf, buf.length, ia, filePort);
                ds.send(dp);

                receiveFileViaUDP(path, ack, ds);
            }
        } catch (Exception e) {
            System.out.println("CATCH");
        }
        System.out.println("EXITS ");

    }

    public Hashtable<String, String> initializeHashtable(Hashtable<String, String> dict, String startDirectory) {
        File dir = new File(startDirectory);
        File[] files = dir.listFiles();
        try {
            if (files != null && files.length > 0) {
                for (File file : files) {
                    dict.put(file.getPath(), "0");
                    if (file.isDirectory()) {
                        dict = initializeHashtable(dict, file.getPath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Enumeration<String> e = dict.keys();

        while (e.hasMoreElements()) {
            String key = e.nextElement();
            System.out.println("KEY: " + key + " VALUE: " + dict.get(key));
        }

        return dict;
    }

    public void removeUnusedDirectories(Hashtable<String, String> my_dict) {
        Enumeration<String> e = my_dict.keys();

        while (e.hasMoreElements()) {
            String key = e.nextElement();
            if (my_dict.get(key).equals("0")) {
                File f = new File(key);
                f.delete();
            }
        }
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

    public void receiveFileViaUDP(String path, String originalMD5, DatagramSocket ds) {
        try {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString() + "/src/com/ucdrive/server/"
                    + this.serverName + "/" + path;

            // Receive
            byte[] rbuf = new byte[bufsize];
            dr = new DatagramPacket(rbuf, rbuf.length);

            int length;
            FileOutputStream fos = new FileOutputStream(s);
            do {
                ds.setSoTimeout(10);

                // receives part of file
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
            System.out.println("TERMINEI ALBERTO");
            fos.close();

            try {
                MD5 obj = new MD5();
                if (!(obj.md5(s).equals(originalMD5))) {
                    System.out.println("ALBERTO MAL");

                    sendAck(ds, "Not done!");
                    receiveFileViaUDP(path, originalMD5, ds);
                } else {
                    System.out.println("ALBERTO BEM");

                    sendAck(ds, "Done!");
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println("ALBERTO MAL");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("ALBERTO MAL");

                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveAndSendAck(DatagramSocket ds, String startDir) {
        Path currentRelativePath = Paths.get("");

        Hashtable<String, String> my_dict = new Hashtable<String, String>();
        my_dict.clear();
        my_dict = initializeHashtable(my_dict, startDir);

        String ack = null;
        try {
            do {
                String s = currentRelativePath.toAbsolutePath().toString() + "\\src\\com\\ucdrive\\server\\"
                        + this.serverName;

                // Receive the type of the existing files
                byte[] rbuf = new byte[bufsize];
                dr = new DatagramPacket(rbuf, rbuf.length);
                ds.receive(dr);
                ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
                DataInputStream dis = new DataInputStream(bais);

                ack = dis.readUTF();

                // receive word "Folder"
                if (ack.equals("Folder")) {
                    sendAck(ds, "Folder");

                    // receive folder path
                    rbuf = new byte[bufsize];
                    dr = new DatagramPacket(rbuf, rbuf.length);
                    ds.receive(dr);
                    bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
                    dis = new DataInputStream(bais);

                    // Home/../
                    String folderPath = dis.readUTF();
                    s = s + folderPath;

                    if (my_dict.containsKey(s)) {
                        my_dict.replace(s, "1");
                    } else {
                        File f = new File(s);
                        f.mkdir();
                    }
                    sendAck(ds, folderPath);

                }
                // receive word "File"
                else if (ack.equals("File")) {
                    sendAck(ds, "File");

                    // receive file's path
                    rbuf = new byte[bufsize];
                    dr = new DatagramPacket(rbuf, rbuf.length);
                    ds.receive(dr);
                    bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
                    dis = new DataInputStream(bais);

                    String filePath = dis.readUTF();

                    sendAck(ds, filePath);
                    s += filePath;
                    if (my_dict.containsKey(s)) {
                        my_dict.replace(s, "1");

                        // confirms md5
                        MD5 obj = new MD5();
                        String md5 = obj.md5(s);

                        receiveAndSendAckMD5(filePath, md5, ds, true);
                    } else {
                        receiveAndSendAckMD5(filePath, "", ds, false);
                    }

                }
            } while (!ack.equals("Finished"));

        } catch (Exception e) {
        }
        sendAck(ds, "Finished");
        removeUnusedDirectories(my_dict);
    }

}