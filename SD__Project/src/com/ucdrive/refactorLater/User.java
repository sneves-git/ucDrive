package com.ucdrive.refactorLater;

import java.io.*;
import java.util.ArrayList;

public class User implements Serializable {
	private String username,
			password,
			department,
			address,
			expirationDate,
			phoneNumber,
			CCnumber,
			lastSession;

	// Constructors
	public User() {
		this.username = "";
		this.password = "";
		this.department = "";
		this.address = "";
		this.expirationDate = "";
		this.phoneNumber = "";
		this.CCnumber = "";
		this.lastSession = "";
	}

	public User(String username, String password, String department, String address, String expirationDate,
			String phoneNumber, String CCnumber, String lastSession) {
		this.username = username;
		this.password = password;
		this.department = department;
		this.address = address;
		this.expirationDate = expirationDate;
		this.phoneNumber = phoneNumber;
		this.CCnumber = CCnumber;
		this.lastSession = lastSession;
	}

	// functions
	public ArrayList<String> getUserFolders(String path) {
		ArrayList<String> folders = new ArrayList<String>();

		File dir = new File(path + lastSession);
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

	public void setDepartament(String newDepartment) {
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

	public String getLastSession() {
		return lastSession;
	}

	public void setLastSession(String newLastSession) {
		this.lastSession = newLastSession;
	}

	// toString method
	public String toString() {
		return "Username: " + username + "\tPassword: " + password +
				"\tDepartment: " + department + "\tAddress: " + address + "\tCC Expiration Date:" + expirationDate
				+ "\tPhone Number: " + phoneNumber + "\t CC Number: " + CCnumber + "\tLast Session: " + lastSession
				+ "\n";
	}

}
