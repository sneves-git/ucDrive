package com.ucdrive.server.commands;

import com.ucdrive.configs.UsersConfigsFile;
import com.ucdrive.utils.User;
import com.ucdrive.utils.Users;

import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class UploadAFile extends Thread {
	private DataInputStream in;
	private Socket socket;
	private int buffsize;
	private String ServerPath, fileName;
	private User user;
	private Users users;

	public UploadAFile(Socket aClientSocket, String ServerPath, String fileName, User user, Users users) {
		try {
			this.buffsize = 1024;
			this.ServerPath = ServerPath;
			this.fileName = fileName;
			this.socket = aClientSocket;
			this.user = user;
			this.users = users;
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

			int length;
			while ((length = in.read(buf, 0, buf.length)) > 0) {
				fos.write(buf, 0, length);
			}
			fos.close();
			socket.close();

			UsersConfigsFile conf = new UsersConfigsFile();
			conf.updateLastChoice(0, user, users);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
