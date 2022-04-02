package com.ucdrive.client.commands;

import com.ucdrive.utils.ConsoleColors;
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
		System.out.println("\n---------------- Upload a file from --------------\n" +
				ConsoleColors.GREEN + clientPath + ConsoleColors.RESET + "\n");

		ServerHelperClass helper = new ServerHelperClass();
		ArrayList<String> files = helper.returnFilesList(clientPath);
		String s = "";
		for (int i = 0; i < files.size(); ++i) {
			s += "[File] " + files.get(i) + "\n";
		}
		s += "Choice: ";

		// Sends menu of existing files
		System.out.print(s);

		// Receive user's file choice for download
		String choice = reader.readLine();

		// Confirms if user's choice is valid (if file exists)
		for (String f : files) {
			if (f.equals(choice)) {
				out.writeUTF("File exists");
				out.writeUTF(f);
				System.out.println(ConsoleColors.GREEN + "Download for " + choice + " has started!" + ConsoleColors.RESET +
						"\n-----------------------------------------------------\n");
				new UploadAFile(sFile, clientPath, choice);
				return;
			}
		}

		out.writeUTF("File does not exists");
		System.out.println(ConsoleColors.RED + "File " + choice + " does not exist!" + ConsoleColors.RESET
				+ "\n---------------------------------------------------\n");

	}

}
