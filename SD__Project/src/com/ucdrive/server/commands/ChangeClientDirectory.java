package com.ucdrive.server.commands;

import com.ucdrive.utils.User;
import com.ucdrive.utils.Users;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChangeClientDirectory {
	public ChangeClientDirectory() {
	}

	public void changeClientDirectory(User user, Users users, DataInputStream in, DataOutputStream out)
			throws IOException {
		out.writeUTF(user.getClientPath());

		if (in.readUTF().equals("Success!")) {
			for (User u : users.getUsers()) {
				if (user.getUsername().equals(u.getUsername())) {
					u.setClientPath(in.readUTF());
					break;
				}
			}
		}

	}

}
