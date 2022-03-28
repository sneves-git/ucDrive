package com.ucdrive.refactorLater;

import java.io.*;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;

public class User implements Serializable {
	private String username,
			password,
			department,
			address,
			expirationDate,
			phoneNumber,
			CCnumber,
			clientPath,
			lastSessionServer;

	// Constructors
	public User() {
		this.username = null;
		this.password = null;
		this.department = null;
		this.address = null;
		this.expirationDate = null;
		this.phoneNumber = null;
		this.CCnumber = null;
		this.clientPath = null;
		this.lastSessionServer = null;
	}

	public User(String username, String password, String department, String address, String expirationDate,
			String phoneNumber, String CCnumber, String lastSessionServer) {
		this.username = username;
		this.password = password;
		this.department = department;
		this.address = address;
		this.expirationDate = expirationDate;
		this.phoneNumber = phoneNumber;
		this.CCnumber = CCnumber;
		this.lastSessionServer = lastSessionServer;

		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		this.clientPath = s;
	}

	// functions
	public ArrayList<String> getUserFoldersInServer(String path) {
		ArrayList<String> folders = new ArrayList<String>();

		File dir = new File(path + lastSessionServer);
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

	public ArrayList<String> getUserFoldersInClient() {
		ArrayList<String> folders = new ArrayList<String>();

		File dir = new File(clientPath);
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

	// getters and setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String newName) {
		this.username = newName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String newPassword) {
		this.password = newPassword;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String newDepartment) {
		this.department = newDepartment;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String newAddress) {
		this.address = newAddress;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String newExpirationDate) {
		this.expirationDate = newExpirationDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String newPhoneNumber) {
		this.phoneNumber = newPhoneNumber;
	}

	public String getCCnumber() {
		return CCnumber;
	}

	public void setCCnumber(String newCCnumber) {
		this.phoneNumber = newCCnumber;
	}

	public String getClientPath() {
		return clientPath;
	}

	public void setClientPath(String newClientPath) {
		this.clientPath = newClientPath;
	}

	public String getLastSessionServer() {
		return lastSessionServer;
	}

	public void setLastSessionServer(String lastSessionServer) {
		this.lastSessionServer = lastSessionServer;
	}

	// toString method
	public String toString() {
		return "Username: " + username + "\tPassword: " + password +
				"\tDepartment: " + department + "\tAddress: " + address + "\tCC Expiration Date:" + expirationDate
				+ "\tPhone Number: " + phoneNumber + "\t CC Number: " + CCnumber + "\t Client Path: " + clientPath
				+ "\tLast Session Server: "
				+ lastSessionServer
				+ "\n";
	}

}
