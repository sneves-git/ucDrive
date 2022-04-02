package com.ucdrive.client.commands;

import java.io.*;
import java.net.Socket;

public class DownloadHelper {
	public DownloadHelper() {}

	public void downloadHelper( DataInputStream in, DataOutputStream out, BufferedReader reader, Socket sFile){
		try {
            String clientPath = in.readUTF();
            
            // Menu
            System.out.print(in.readUTF());
            
			// Read client's choice
			String fileName = reader.readLine();
            
			// Client's path
			out.writeUTF(fileName);

            // File's name
            if (in.readUTF().equals("File exists!")) {

				// Creates thread to handle file download concurrently
				new DownloadAFile(sFile, clientPath, fileName);
			}
		}catch(IOException e){
			e.printStackTrace();
		}

	}
}

