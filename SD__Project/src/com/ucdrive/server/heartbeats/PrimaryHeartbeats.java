package com.ucdrive.server.heartbeats;

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
            System.out.println("[UDP for heartbeats] IP="+ds.getInetAddress() +" Port="+ port);
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

                } catch (IOException ioe) {
                    ds.close();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
