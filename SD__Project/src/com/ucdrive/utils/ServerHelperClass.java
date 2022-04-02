package com.ucdrive.utils;

import java.util.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;

public class ServerHelperClass {

	public ServerHelperClass() {
	}

	// Function to search for all folders and files within a certain starting
	// directory
	public void listFoldersAndFiles(DataOutputStream out, String startDir, boolean client) throws IOException {

		File dir = new File(startDir);
		File[] files = dir.listFiles();

		if (files != null && files.length > 0) {
			for (File file : files) {
				// Check if the file is a directory
				if (file.isDirectory()) {
					if (client) {
						System.out.println("[Folder] " + file.getName());
					} else {
						out.writeUTF("[Folder] " + file.getName());
					}
				} else {
					if (client) {
						System.out.println("[File] " + file.getName());
					} else {
						out.writeUTF("[File] " + file.getName());
					}
				}
			}
		}
		if (!client) {
			out.writeUTF("done");
		}
	}

	public ArrayList<String> returnFilesList(String path) {
		ArrayList<String> filesList = new ArrayList<String>();
		File dir = new File(path);
		File[] files = dir.listFiles();

		if (files != null && files.length > 0) {
			for (File file : files) {
				if (!file.isDirectory()) {
					filesList.add(file.getName());
				}
			}
		}

		return filesList;
	}

	public String determineIfPrimaryOrSecondary(int hostPort, String host) {
		final int timeout = 10;
		final int bufsize = 4096;

		int failedHeartbeats = 0;

		InetAddress ia = null;
		try {
			ia = InetAddress.getByName(host);
			System.out.println(ia);
			try (DatagramSocket ds = new DatagramSocket()) {
				ds.setSoTimeout(timeout);

				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					DataOutputStream dos = new DataOutputStream(baos);
					dos.writeInt(0);
					byte[] buf = baos.toByteArray();

					DatagramPacket dp = new DatagramPacket(buf, buf.length, ia, hostPort);
					ds.send(dp);

					byte[] rbuf = new byte[bufsize];
					DatagramPacket dr = new DatagramPacket(rbuf, rbuf.length);

					ds.receive(dr);
					ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
					DataInputStream dis = new DataInputStream(bais);
					int n = dis.readInt();

				} catch (SocketTimeoutException e) {
					failedHeartbeats++;
					e.getStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ds.close();

			} catch (SocketException se) {
				se.printStackTrace();
			}
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		if (failedHeartbeats == 0) {
			return "Secondary";
		} else {
			return "Primary";
		}

	}

	public String convertPath(String path, String serverName) {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		String p = "/src/com/ucdrive/server/" + serverName + "/";
		String totalPath = Paths.get(s, p).toString();
		path = path.replace(totalPath, "");
		return path;
	}

}