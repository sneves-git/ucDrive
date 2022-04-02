package com.ucdrive.server.commands;

import com.ucdrive.configs.UsersConfigsFile;
import com.ucdrive.utils.User;
import com.ucdrive.utils.Users;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadHelper {
	public UploadHelper() {
	}

	public void uploadHelper(String clientPath, String serverPath, String server, DataInputStream in,
	                         DataOutputStream out, Socket sFile, User user, Users users, int myPort, int filePort, String host) {

		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString() + "/src/com/ucdrive/server/" + server + "/"
				+ serverPath;

		try {
			// Send client's path
			out.writeUTF(clientPath);
			if (in.readUTF().equals("File exists")) {
				String fileName = in.readUTF();
				new UploadAFile(sFile, path, fileName, user, users, myPort, filePort, host, server);
			}else{
				sFile.close();
				UsersConfigsFile conf = new UsersConfigsFile();
				conf.updateLastChoice(0, user, users);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
