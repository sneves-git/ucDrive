package com.ucdrive.server;

import com.ucdrive.refactorLater.User;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

public class Login {
	private ArrayList<User> users;
	Scanner sc;

	public Login() {
		users = new ArrayList<>();
		sc = new Scanner(System.in);
	}

	// getters and setters
	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> newUsers) {
		this.users = newUsers;
	}

	// Extracts from the file configs the user's information
	public void readUsersInfo() {
		String users_file_name = "../configs/users.txt";

		try {
			File myObj = new File(users_file_name);
			Scanner myReader = new Scanner(myObj);

			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();

				String[] array_info = data.split("::", 8);

				User user = new User(array_info[0], array_info[1], array_info[2], array_info[3], array_info[4],
						array_info[5], array_info[6], array_info[7]);
				users.add(user);

				System.out.println(user);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred with " + users_file_name + ".");
			e.printStackTrace();
		}
	}

	// Menu for user's input
	public void menu() {
		System.out.print("Choose one of the following options:\n"
				+ "\t1 - Configure IP and port of primary and secondary servers\n"
				+ "\t2 - Login\n"
				+ "\t3 - Exit\n"
				+ "Choice: ");

		// Checks if user's input is valid
		int choice = 0;
		try {
			do {
				if (sc.hasNextInt()) {
					choice = sc.nextInt();
					if (choice > 0 && choice < 4) {
						if (sc.hasNextLine()) {
							sc.nextLine();
						}
						continue;
					}
				} else {
					if (sc.hasNextLine()) {
						sc.nextLine();
					}
				}

				System.out.print("\nInvalid option.\n\n"
						+ "Choose one of the following options:\n"
						+ "\t1 - Configure IP and port of primary and secondary servers\n"
						+ "\t2 - Login\n"
						+ "\t3 - Exit\n"
						+ "Choice: ");
			} while (choice < 1 || choice > 3);
		} catch (Exception e) {
			System.out.println("Login.java in Menu() - An error occurred with scanner.");
			e.printStackTrace();
		}

		switch (choice) {
			case 1:
				configureIPandPortServers();
				break;
			case 2:
				login_();
				break;
			default:
				Exit();
		}

	}

	public void configureIPandPortServers() {
		System.out.print("Choose one of the following options:\n"
				+ "\t1 - Configure IP and port of primary and secondary servers\n"
				+ "\t2 - Login\n"
				+ "\t3 - Exit\n"
				+ "Choice: ");

		// Checks if user's input is valid
		int choice = 0;
		try {
			do {
				if (sc.hasNextInt()) {
					choice = sc.nextInt();
					if (choice > 0 && choice < 4) {
						if (sc.hasNextLine()) {
							sc.nextLine();
						}
						continue;
					}
				} else {
					if (sc.hasNextLine()) {
						sc.nextLine();
					}
				}

				System.out.print("\nInvalid option.\n\n"
						+ "Choose one of the following options:\n"
						+ "\t1 - Configure IP and port of primary and secondary servers\n"
						+ "\t2 - Login\n"
						+ "\t3 - Exit\n"
						+ "Choice: ");
			} while (choice < 1 || choice > 3);
		} catch (Exception e) {
			System.out.println("Login.java in Menu() - An error occurred with scanner.");
			e.printStackTrace();
		}
	}

	public void login_() {

		// MUDAR DE SITIO PARA O CLIENT
		readUsersInfo();
		String username = null, password = null;

		System.out.println("------------ Login -------------");
		do {
			System.out.print("Username: ");
			if (sc.hasNextLine()) {
				username = sc.nextLine();
			}
		} while (!CheckIfUsernameExists(username));

		do {
			System.out.print("Password: ");
			if (sc.hasNextLine()) {
				password = sc.nextLine();
			}
		} while (!CheckIfPasswordIsCorrect(username, password));
		System.out.println();

		User user = null;
		for (User u : users) {
			if (u.getUsername().equals(username)) {
				user = u;
			}
		}
		clientsApplication(user);
	}

	private void clientsApplication(User user) {
		int choice;
		// choice = authenticatedMenu();
		while ((choice = authenticatedMenu()) != 8) {
			switch (choice) {
				case 1:
					changePassword(user);
					break;
				case 2:
					changeClientsDirectory();
					break;
				case 3:
					changeServersDirectory(user);
					break;
				case 4:
					listClientFiles();
					break;
				case 5:
					listServerFiles(user);
					break;
				case 6:
					uploadAFile();
					break;
				case 7:
					downloadAFile();
					break;
			}
		}

	}

	private void changePassword(User user) {
		String password = null, newPassword = null;
		boolean flag = false;

		do {
			System.out.println("Insert your current password: ");
			if (sc.hasNextLine()) {
				password = sc.nextLine();
			}
		} while (!CheckIfPasswordIsCorrect(user.getUsername(), password));

		do {
			System.out.println("Insert your new password: ");
			if (sc.hasNextLine()) {
				newPassword = sc.nextLine();
				flag = true;
			}
		} while (!flag);
		System.out.println("Password changed successfully!");
		user.setPassword(newPassword);

		String fileName = "../configs/users.txt";
		updateUsersFile(fileName);

	}

	private void changeClientsDirectory() {

	}

	private ArrayList<String> getUserFolders(String path, User user) {
		ArrayList<String> folders = new ArrayList<String>();

		File dir = new File(path + user.getLastSession());
		File[] files = dir.listFiles();

		if (files != null && files.length > 0) {
			for (File file : files) {
				// Check if the file is a directory
				if (file.isDirectory()) {
					folders.add(file.getName());
				}
			}
		}
		return folders;

	}

	private boolean isValidCommand(String choice, User user) {
		String path = "../server_files/";
		ArrayList<String> folders = new ArrayList<String>();

		folders = getUserFolders(path, user);

		if (choice.equals("cd ..") || choice.equals("..")) {
			if (user.getLastSession().equals("Home/" + user.getUsername())) {
				// VER SE TEM PERMISSOES DE LEITURA
			} else {
				String newPath = "";
				List<String> list = new ArrayList<String>(Arrays.asList(user.getLastSession().split("/")));
				int i = 0;
				for (i = 0; i < list.size() - 2; ++i) {
					newPath += list.get(i) + "/";
				}
				newPath += list.get(i);
				user.setLastSession(newPath);
			}
			String fileName = "../configs/users.txt";
			updateUsersFile(fileName);
			return true;
		}
		for (String folder : folders) {
			if (choice.equals("cd " + folder) || choice.equals(folder)) {
				user.setLastSession(user.getLastSession() + "/" + folder);
				String fileName = "../configs/users.txt";
				updateUsersFile(fileName);
				return true;
			}
		}
		System.out.println("The system cannot find the path specified.");
		return false;
	}

	private void changeServersDirectory(User user) {
		String choice = null;
		do {
			System.out.print(user.getLastSession() + "> ");
			if (sc.hasNextLine()) {
				choice = sc.nextLine();
			}

		} while (!isValidCommand(choice, user));

		// Update users configs with new directory

	}

	private void listClientFiles() {

	}

	private void listServerFiles(User user) {
		String path = "../server_files/";

		File dir = new File(path + user.getLastSession());
		File[] files = dir.listFiles();

		if (files != null && files.length > 0) {
			for (File file : files) {
				// Check if the file is a directory
				if (file.isDirectory()) {
					System.out.println("[Folder]  " + file.getName());
				} else {
					System.out.println("[File]    " + file.getName());
				}
			}
		}

	}

	private void uploadAFile() {

	}

	private void downloadAFile() {

	}

	private int authenticatedMenu() {
		System.out.print("Choose one of the following options:\n"
				+ "\t1 - Change password\n"
				+ "\t2 - Change client's directory\n"
				+ "\t3 - Change server's directory\n"
				+ "\t4 - List client files\n"
				+ "\t5 - List server files\n"
				+ "\t6 - Upload a file\n"
				+ "\t7 - Download a file\n"
				+ "\t8 - Exit\n"
				+ "Choice: ");

		// Checks if user's input is valid
		int choice = 0;
		try {
			do {
				if (sc.hasNextInt()) {
					choice = sc.nextInt();
					if (choice > 0 && choice < 9) {
						if (sc.hasNextLine()) {
							sc.nextLine();
						}
						continue;
					}
				} else {
					if (sc.hasNextLine()) {
						sc.nextLine();
					}
				}

				System.out.print("\nInvalid option.\n\n"
						+ "Choose one of the following options:\n"
						+ "\t1 - Change password\n"
						+ "\t2 - Change client's directory\n"
						+ "\t3 - Change server's directory\n"
						+ "\t4 - List cleint files\n"
						+ "\t5 - List server files\n"
						+ "\t6 - Upload a file\n"
						+ "\t7 - Download a file\n"
						+ "\t8 - Exit\n"
						+ "Choice: ");
			} while (choice < 1 || choice > 8);
		} catch (Exception e) {
			System.out.println("Login.java in Menu() - An error occurred with scanner.");
			e.printStackTrace();
		}
		System.out.println();

		return choice;
	}

	private boolean CheckIfUsernameExists(String username) {
		for (User user : users) {
			if (user.getUsername().equals(username)) {
				return true;
			}
		}
		System.out.println("Username does not exist!");
		return false;
	}

	private boolean CheckIfPasswordIsCorrect(String username, String password) {
		for (User user : users) {
			if (user.getUsername().equals(username)) {
				if (user.getPassword().equals(password)) {
					return true;
				}
				System.out.println("Invalid password!");
				return false;
			}
		}
		return false;
	}

	private void updateUsersFile(String fileName) {
		Writer output;
		try {
			output = new BufferedWriter(new FileWriter(fileName));
			for (User user : users) {
				output.append(user.getUsername() + "::" + user.getPassword() + "::" + user.getDepartment() + "::"
						+ user.getAddress() + "::"
						+ user.getPhoneNumber() + "::" + user.getExpirationDate() + "::" + user.getCCnumber()
						+ "::"
						+ user.getLastSession()
						+ "\n");
			}
			output.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void Exit() {

	}

	@Override
	public String toString() {
		return "login";
	}

}
