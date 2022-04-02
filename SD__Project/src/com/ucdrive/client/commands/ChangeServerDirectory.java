package com.ucdrive.client.commands;

import com.ucdrive.utils.ConsoleColors;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChangeServerDirectory {
	public ChangeServerDirectory() {
	}

	public void changeServerDirectory(DataInputStream in, DataOutputStream out, BufferedReader reader)
			throws IOException {
		System.out.println("\n------------ Change Server's Directory -------------");

		// Receive current directory and print it
		System.out.print(in.readUTF());

		// Write to server the command "cd .." or "cd folderName"
		out.writeUTF(reader.readLine());

		// Print from server ACK
		String ack = in.readUTF();
		if(ack.equals("Success!")){
			System.out.println(ConsoleColors.GREEN + "Directory changed successfully!" + ConsoleColors.RESET);
		}else{
			System.out.println(ConsoleColors.RED + "Wrong input: Could not change directory!" + ConsoleColors.RESET);
		}
		System.out.println("-------------------------------------------------\n");


	}

}
