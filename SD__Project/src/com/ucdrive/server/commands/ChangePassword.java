package com.ucdrive.server.commands;

import com.ucdrive.configs.UsersConfigsFile;
import com.ucdrive.refactorLater.User;
import com.ucdrive.refactorLater.Users;

import java.io.*;

public class ChangePassword {

    public ChangePassword() {
    }

    public void changePassword(User user, Users users, DataInputStream in, DataOutputStream out) throws IOException {
        String password = null, newPassword = null;

        // Checks if user knows current password
        do {
            password = in.readUTF();
        } while (!users.CheckIfPasswordIsCorrect(user.getUsername(), password, out));

        // New password
        newPassword = in.readUTF();
        out.writeUTF("Password changed successfully!");

        // Updates user with new password
        User u = users.getUserWithGivenUsername(user.getUsername());
        u.setPassword(newPassword);

        // Rewrites to users.txt updated information
        String fileName = "src/com/ucdrive/configs/users.txt";
        UsersConfigsFile conf = new UsersConfigsFile();
        conf.updateUsersFile(users);

    }

}
