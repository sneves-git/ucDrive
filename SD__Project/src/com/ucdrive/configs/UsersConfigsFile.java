package com.ucdrive.configs;

import com.ucdrive.refactorLater.User;
import com.ucdrive.refactorLater.Users;

import java.io.*;
import java.util.Scanner;

// Class to: extract from the file configs the user's information (readUsersInfo)
//           update content of user's configs file                (updateUsersFile)
public class UsersConfigsFile {

    public UsersConfigsFile() {
    }

    public void readUsersInfo(Users users) {
        String users_file_name = "src/com/ucdrive/configs/users.txt";

        try {
            File myObj = new File(users_file_name);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                String[] array_info = data.split("::", 8);

                User user = new User(array_info[0], array_info[1], array_info[2], array_info[3], array_info[4],
                        array_info[5], array_info[6], array_info[7]);
                (users.getUsers()).add(user);

                System.out.println(user);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred with " + users_file_name + ".");
            e.printStackTrace();
        }
    }

    public void updateUsersFile(String fileName, Users users) {
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter(fileName));
            for (User user : users.getUsers()) {
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
}
