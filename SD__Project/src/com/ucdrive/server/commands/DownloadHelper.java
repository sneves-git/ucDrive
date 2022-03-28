package com.ucdrive.server.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import com.ucdrive.refactorLater.*;

public class DownloadHelper {
	public DownloadHelper() {}
	public String downloadHelper(String clientPath, String serverPath,  DataInputStream in, DataOutputStream out, String server){
		ServerHelperClass shc = new ServerHelperClass();
		ArrayList<String> files = new ArrayList<>();
		String s = "";
		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString() + "/src/com/ucdrive/server/"+ server + "/" + serverPath;
		
		
		try {
			// Send client's path
			out.writeUTF(clientPath);

			// Joins all existing files in a string
			files = shc.returnFilesList(path);
			for (int i = 0; i < files.size(); ++i) {
				s += "- " + files.get(i) + "\n";
			}
			s += "choice: ";

			// Sends menu of existing files
			out.writeUTF(s);

			// Receive user's file choice for download
			String choice = in.readUTF();

			// Confirms if user's choice is valid (if file exists)
			for (String f : files) {
				if (f.equals(choice)) {
					out.writeUTF("File exists!");
					return f;
				}
			}
			out.writeUTF("File does not exist!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "File does not exist!";
	}
}
