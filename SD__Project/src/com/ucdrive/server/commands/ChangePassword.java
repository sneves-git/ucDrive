package com.ucdrive.server.commands;

import com.ucdrive.refactorLater.User;

import java.util.Scanner;

import static com.ucdrive.configs.UsersConfigsFile.updateUsersFile;
import static com.ucdrive.refactorLater.Users.CheckIfPasswordIsCorrect;

public class ChangePassword {
    Scanner sc = new Scanner(System.in);

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

        String fileName = "src/com/ucdrive/configs/users.txt";
        updateUsersFile(fileName);

    }
}
