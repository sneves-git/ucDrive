package com.ucdrive.client.commands;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChangeServerDirectory {
	public ChangeServerDirectory() {
	}

	public void changeServerDirectory(DataInputStream in, DataOutputStream out, BufferedReader reader)
			throws IOException {
		System.out.println("------------ Change Server Directory -------------");

		// Receive current directory and print it
		System.out.print(in.readUTF());

		// Write to server the command "cd .." or "cd folderName"
		out.writeUTF(reader.readLine());

		// Print from server ACK
		System.out.println(in.readUTF());

	}

}
