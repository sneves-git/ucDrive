package com.ucdrive.server.commands;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadHelper {
	public UploadHelper(){}

	public void uploadHelper(String clientPath, String serverPath, DataInputStream in, DataOutputStream out, Socket sFile){

		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString() + "/src/com/ucdrive/server/" + serverPath;


		try {
			// Send client's path
			out.writeUTF(clientPath);
			if(in.readUTF().equals("File exists")){
				String fileName = in.readUTF();
				new UploadAFile(sFile, path, fileName);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
