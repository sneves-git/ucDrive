package com.ucdrive.client.commands;

import com.ucdrive.utils.ConsoleColors;

import java.io.*;
import java.net.Socket;

public class DownloadHelper {
	public DownloadHelper() {}

	public void downloadHelper( DataInputStream in, DataOutputStream out, BufferedReader reader, Socket sFile){

		try {
            String clientPath = in.readUTF();
			System.out.println("\n---------------- Download a file from --------------\n" +
								ConsoleColors.GREEN + clientPath + ConsoleColors.RESET + "\n");

			// Menu
            System.out.print(in.readUTF());
            
			// Read client's choice
			String fileName = reader.readLine();
            
			// Client's path
			out.writeUTF(fileName);

            // File's name
            if (in.readUTF().equals("File exists!")) {

				// Creates thread to handle file download concurrently
				System.out.println(ConsoleColors.GREEN + "Download for " + fileName + " has started!" + ConsoleColors.RESET +
						"\n-----------------------------------------------------\n");
				new DownloadAFile(sFile, clientPath, fileName);
			}else{
				System.out.println(ConsoleColors.RED + "File " + fileName + " does not exist!" + ConsoleColors.RESET
						+ "\n---------------------------------------------------\n");
			}
		}catch(IOException e){
			e.printStackTrace();
		}

	}
}

