package com.ucdrive.refactorLater;

import java.util.*;
import java.io.*;

public class ServerHelperClass{
	ArrayList<String> directories;

	public ServerHelperClass() {
		directories = new ArrayList<String>();
	}

	// Function to search for all folders and files within a certain starting directory
	// Stores all the directories and folders
	public void listFiles(Folder directory, String startDir) {

		File dir = new File(startDir);
		File[] files = dir.listFiles();

		if (files != null && files.length > 0) {
			for (File file : files) {
				// Check if the file is a directory
				if (file.isDirectory()) {
					// We will not print the directory name, just use it as a new
					// starting point to list files from
					Folder new_folder = new Folder(file.getPath());
					directory.addFolder(new_folder);
					listFiles(new_folder, file.getPath());
				} else {
					// We can use .length() to get the file size

					File_ f = new File_(file.getPath());
					directory.addFolder(f);
					// directories.add(file.getName());
					// System.out.println(file.getName() + " (size in bytes: " + file.length()+")");
				}
			}
		}
	}
}
