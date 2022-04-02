package com.ucdrive.server.heartbeats;

import java.io.*;
import java.net.*;

public class SecondaryHeartbeats extends Thread {
    private final int maxFailedRounds = 5;
    private final int timeout = 10;
    private final int bufsize = 4096;
    private final int period = 10000;
    private final int port;
    private final String host;

    public SecondaryHeartbeats(int port, String host) {
        this.port = port;
        this.host = host;
        this.start();
    }

    public void run() {
        InetAddress ia = null;
        try {
            ia = InetAddress.getByName(host);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }

        int count = 1;
        try (DatagramSocket ds = new DatagramSocket()) {
            ds.setSoTimeout(timeout);

            int failedHeartbeats = 0;
            while (failedHeartbeats < maxFailedRounds) {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);
                    dos.writeInt(count++);
                    byte[] buf = baos.toByteArray();

                    DatagramPacket dp = new DatagramPacket(buf, buf.length, ia, port);
                    ds.send(dp);

                    byte[] rbuf = new byte[bufsize];
                    DatagramPacket dr = new DatagramPacket(rbuf, rbuf.length);

                    ds.receive(dr);
                    ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
                    DataInputStream dis = new DataInputStream(bais);
                    int n = dis.readInt();

                    failedHeartbeats = 0;
                    Thread.sleep(period);
                } catch (SocketTimeoutException e) {
                    failedHeartbeats++;
                    e.getStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }

        } catch (SocketException se) {
            se.printStackTrace();
        }
    }
}
