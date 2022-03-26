package com.ucdrive.refactorLater;

import java.io.Serializable;
import java.util.ArrayList;

public class Users implements Serializable {
    private static ArrayList<User> users;

    public Users(){
        users = new ArrayList<User>();
    }

    public static boolean CheckIfUsernameExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        System.out.println("Username does not exist!");
        return false;
    }

    public static boolean CheckIfPasswordIsCorrect(String username, String password) {
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


    // getters and setters
    public static ArrayList<User> getUsers() {
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
