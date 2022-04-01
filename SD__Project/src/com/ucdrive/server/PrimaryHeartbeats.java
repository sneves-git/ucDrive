package com.ucdrive.server;

import java.io.*;
import java.net.*;

public class PrimaryHeartbeats extends Thread {
    private final int port;
    private static final int bufsize = 4096;

    public PrimaryHeartbeats(int port) {
        this.port = port;
        this.start();
    }

    public void run() {
        try (DatagramSocket ds = new DatagramSocket(port)) {
            while (true) {
                try {
                    byte buf[] = new byte[bufsize];
                    DatagramPacket dp = new DatagramPacket(buf, buf.length);
                    ds.receive(dp);
                    ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
                    DataInputStream dis = new DataInputStream(bais);
                    int count = dis.readInt();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);
                    dos.writeInt(count);
                    byte resp[] = baos.toByteArray();
                    DatagramPacket dpresp = new DatagramPacket(resp, resp.length, dp.getAddress(), dp.getPort());
                    ds.send(dpresp);

                    /*
                     * ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     * byte[] buf = baos.toByteArray();
                     * 
                     * DatagramPacket dp = new DatagramPacket(buf, buf.length, ia, port);
                     * System.out.println("TOU A MANDAR HEARTBEATS");
                     * ds.send(dp);
                     */

                } catch (IOException ioe) {
                    // ds.close();
                    // ioe.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
