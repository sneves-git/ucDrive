package com.ucdrive.server.commands;

import com.ucdrive.configs.UsersConfigsFile;
import com.ucdrive.refactorLater.User;
import com.ucdrive.refactorLater.Users;

import java.util.*;
import java.io.*;

public class ChangePassword {
    Scanner sc;

    public ChangePassword() {
        sc = new Scanner(System.in);
    }

    public void changePassword(User user, Users users, DataInputStream in, DataOutputStream out) throws IOException {
        String password = null, newPassword = null;
        boolean flag = false;

        do {
            password = in.readUTF();
        } while (!users.CheckIfPasswordIsCorrect(user.getUsername(), password, out));

        newPassword = in.readUTF();

        out.writeUTF("Password changed successfully!");
        for (User u : users.getUsers()) {
            if (u.getUsername().equals(user.getUsername())) {
                u.setPassword(newPassword);
            }
        }
        String fileName = "src/com/ucdrive/configs/users.txt";
        UsersConfigsFile conf = new UsersConfigsFile();
        conf.updateUsersFile(fileName, users);

    }

}
