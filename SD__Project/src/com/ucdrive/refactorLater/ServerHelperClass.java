package com.ucdrive.refactorLater;

import java.util.*;
import java.io.*;

public class ServerHelperClass {

	public ServerHelperClass() {
	}

	// Function to search for all folders and files within a certain starting
	// directory
	public void listFiles(DataOutputStream out, String startDir, boolean client) throws IOException {

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
					// listFiles(new_folder, file.getPath()); <-- recursive step
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

}
