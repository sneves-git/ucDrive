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
			username = in.readUTF();
		} while (!users.CheckIfUsernameExists(username, out));

		do {
			password = in.readUTF();
		} while (!users.CheckIfPasswordIsCorrect(username, password, out));

		for (User user : users.getUsers()) {
			if (user.getUsername().equals(username)) {
				my_user = user;
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
