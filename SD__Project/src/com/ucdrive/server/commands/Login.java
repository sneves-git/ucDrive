package com.ucdrive.server.commands;

import com.ucdrive.utils.Users;
import com.ucdrive.utils.User;

import java.io.*;
import java.nio.file.*;

public class Login {
	private String username, password;

	public Login() {
		username = null;
		password = null;
	}

	public User login_(Users users, DataInputStream in, DataOutputStream out) throws IOException {
		User my_user = null;

		Path currentRelativePath = Paths.get("");
		String newClientPath = currentRelativePath.toAbsolutePath().toString();

		do {
			username = in.readUTF();
		} while (!users.CheckIfUsernameExists(username, out));

		do {
			password = in.readUTF();
		} while (!users.CheckIfPasswordIsCorrect(username, password, out));

		for (User user : users.getUsers()) {
			if (user.getUsername().equals(username)) {
				my_user = user;
				user.setClientPath(newClientPath);
				return my_user;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return "server login";
	}

}
