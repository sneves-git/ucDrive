package com.ucdrive.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

import com.ucdrive.client.commands.ListClientFiles;
import com.ucdrive.client.commands.UploadAFile;
import com.ucdrive.server.commands.*;
import com.ucdrive.client.commands.ChangeClientDirectory;
import com.ucdrive.client.commands.ConfigureIpAndPort;
import com.ucdrive.refactorLater.User;
import com.ucdrive.refactorLater.Users;

public class AuthenticatedMenu {

    public AuthenticatedMenu() {
    }


    public void authenticatedMenu(User user, Users users, DataInputStream in, DataOutputStream out) throws IOException {
        int choice = 0;

        while (choice != 11) {
            switch (choice) {
                case -1:
                    out.writeUTF("\nInvalid option.\n\n"
                            + "\t1  - Change password\n"
                            + "\t2  - Change client's directory\n"
                            + "\t3  - Change server's directory\n"
                            + "\t4  - Configure IP and port of servers\n"
                            + "\t5  - Create new folder\n"
                            + "\t6  - Delete folder\n"
                            + "\t7  - Download a file\n"
                            + "\t8  - List client files\n"
                            + "\t9  - List server files\n"
                            + "\t10 - Upload a file\n"
                            + "\t11 - Exit\n"
                            + "Choice: ");
                    try {
                        choice = Integer.parseInt(in.readUTF());
                    } catch (NumberFormatException e) {
                        choice = -1;
                    }
                case 0:
                    out.writeUTF("Choose one of the following options:\n"
                            + "\t1  - Change password\n"
                            + "\t2  - Change client's directory\n"
                            + "\t3  - Change server's directory\n"
                            + "\t4  - Configure IP and port of servers\n"
                            + "\t5  - Create new folder\n"
                            + "\t6  - Delete folder\n"
                            + "\t7  - Download a file\n"
                            + "\t8  - List client files\n"
                            + "\t9  - List server files\n"
                            + "\t10 - Upload a file\n"
                            + "\t11 - Exit\n"
                            + "Choice: ");

                    try {
                        choice = Integer.parseInt(in.readUTF());
                    } catch (NumberFormatException e) {
                        choice = -1;
                    }

                case 1:
                    ChangePassword obj1 = new ChangePassword();
                    obj1.changePassword(user, users, in, out);
                    choice = 0;
                    break;
                case 2:
                    ChangeClientDirectory obj2 = new ChangeClientDirectory();
                    // obj2.changeClientsDirectory();
                    choice = 0;
                    break;
                case 3:
                    ChangeServerDirectory obj3 = new ChangeServerDirectory();
                    // obj3.changeDirectory(user, users);
                    choice = 0;
                    break;
                case 4:
                    ConfigureIpAndPort obj4 = new ConfigureIpAndPort();
                    // obj4.configureIpAndPort();
                    choice = 0;
                    break;
                case 5:
                    CreateNewFolder obj5 = new CreateNewFolder();
                    // createNewFolder();
                    choice = 0;
                    break;
                case 6:
                    DeleteFolder obj6 = new DeleteFolder();
                    // obj6.deleteFolder();
                    choice = 0;
                    break;
                case 7:
                    DownloadAFile obj7 = new DownloadAFile();
                    // obj7.downloadAFile();
                    choice = 0;
                    break;
                case 8:
                    ListClientFiles obj8 = new ListClientFiles();
                    // obj8.listClientFiles();
                    choice = 0;
                    break;
                case 9:
                    ListServerFiles obj9 = new ListServerFiles();
                    // obj9.listServerFiles(user);
                    choice = 0;
                    break;
                case 10:
                    UploadAFile obj10 = new UploadAFile();
                    // obj10.uploadAFile();
                    choice = 0;
                    break;
                case 11:
                    // exit();
                    break;
            }
        }
    }
}
