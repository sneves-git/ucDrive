package com.ucdrive.server.commands;

import java.io.DataOutputStream;
import java.io.IOException;

public class DeleteClientFolderOrFile {
	public DeleteClientFolderOrFile(){

	}
	public void deleteClientFolder(String ClientPath,  DataOutputStream out) throws IOException {
		out.writeUTF(ClientPath);
	}
}
