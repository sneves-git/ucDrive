package com.ucdrive.client.commands;

import com.ucdrive.utils.ServerHelperClass;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class UploadHelper {
	public UploadHelper() {
	}

	public void uploadHelper(DataInputStream in, DataOutputStream out, BufferedReader reader, Socket sFile)
			throws IOException {
		String clientPath = in.readUTF();

		ServerHelperClass helper = new ServerHelperClass();
		ArrayList<String> files = helper.returnFilesList(clientPath);
		String s = "";
		for (int i = 0; i < files.size(); ++i) {
			s += "- " + files.get(i) + "\n";
		}
		s += "choice: ";

		// Sends menu of existing files
		System.out.print(s);

		// Receive user's file choice for download
		String choice = reader.readLine();

		// Confirms if user's choice is valid (if file exists)
		for (String f : files) {
			if (f.equals(choice)) {
				out.writeUTF("File exists");
				out.writeUTF(f);
				new UploadAFile(sFile, clientPath, choice);
				return;
			}
		}

		out.writeUTF("File does not exists");

	}

}
