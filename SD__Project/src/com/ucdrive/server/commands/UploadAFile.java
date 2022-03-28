package com.ucdrive.server.commands;

import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class UploadAFile extends Thread {
	private DataInputStream in;
	private Socket socket;
	private int buffsize;
	private String ServerPath, fileName;

	public UploadAFile(Socket aClientSocket, String ServerPath, String fileName){
		try {
			this.buffsize = 1024;
			this.ServerPath = ServerPath;
			this.fileName = fileName;
			this.socket = aClientSocket;
			this.in = new DataInputStream(socket.getInputStream());

			this.start();
		} catch (IOException e) {
			System.out.println("Connection:" + e.getMessage());
		}
	}

	// =============================
	public void run() {

		try {
			FileOutputStream fos = new FileOutputStream(ServerPath + "/" + fileName);
			byte[] buf = new byte[buffsize];

			String s, a;

			int length;
			while((length = in.read(buf, 0, buf.length)) >0){
				fos.write(buf, 0, length);

			}
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
