package com.ucdrive.server.commands;

import java.io.DataOutputStream;
import java.io.IOException;

public class CreateNewClientFolder {
	public CreateNewClientFolder(){}
	public void createNewClientFolder(String ClientPath,DataOutputStream out) throws IOException {
		out.writeUTF(ClientPath);
	}
}
