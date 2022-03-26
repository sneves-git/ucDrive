package com.ucdrive.server.commands;

import com.ucdrive.refactorLater.Users;
import com.ucdrive.refactorLater.User;

import java.util.*;
import java.io.*;

/*
* do {

        } while (!CheckIfUsernameExists(username) && !CheckIfPasswordIsCorrect(username, password));
* */

public class Login {
	private String username, password;

	public Login() {
		username = null;
		password = null;
	}

	public User login_(Users users, DataInputStream in, DataOutputStream out) throws IOException {
		User my_user = null;

		do {
			System.out.println("[Server] Username read from client");

			username = in.readUTF();
			System.out.println("[Server] after reading");

		} while (!users.CheckIfUsernameExists(username, out));

		do {
			System.out.println("[Server] Password read from client");

			password = in.readUTF();
			System.out.println("[Server] after reading");

		} while (!users.CheckIfPasswordIsCorrect(username, password, out));

		for (User user : users.getUsers()) {
			if (user.getUsername().equals(username)) {
				my_user = user;
				System.out.println("my_user");
				return my_user;
			}
		}

		return my_user;
	}

	@Override
	public String toString() {
		return "server login";
	}

}
