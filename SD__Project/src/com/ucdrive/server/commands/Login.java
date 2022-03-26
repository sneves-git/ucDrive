package com.ucdrive.server.commands;

import com.ucdrive.refactorLater.User;

import java.util.*;
import java.io.*;

import static com.ucdrive.configs.UsersConfigsFile.updateUsersFile;

/*
* do {

        } while (!CheckIfUsernameExists(username) && !CheckIfPasswordIsCorrect(username, password));
* */


public class Login {
	Scanner sc;

	public Login() {
		sc = new Scanner(System.in);
	}




	@Override
	public String toString() {
		return "server login";
	}

}
