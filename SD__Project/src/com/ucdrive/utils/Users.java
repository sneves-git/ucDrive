package com.ucdrive.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Users implements Serializable {
    private ArrayList<User> users;

    public Users() {
        users = new ArrayList<User>();
    }

    public User getUserWithGivenUsername(String username) {
        User user = null;
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return user;
    }

    public boolean CheckIfUsernameExists(String username, DataOutputStream out) throws IOException {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                out.writeUTF("Username exists!");
                return true;
            }
        }
        out.writeUTF("Username does not exist!");
        return false;
    }

    public boolean CheckIfPasswordIsCorrect(String username, String password, DataOutputStream out) throws IOException {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    out.writeUTF("Valid password!");
                    return true;
                }
                out.writeUTF("Invalid password!");
                return false;
            }
        }
        out.writeUTF("Invalid password!");
        return false;
    }

    // getters and setters
    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> newUsers) {
        users = newUsers;
    }

    // toString
    @Override
    public String toString() {
        return "Users{" +
                "users=" + users +
                '}';
    }
}
