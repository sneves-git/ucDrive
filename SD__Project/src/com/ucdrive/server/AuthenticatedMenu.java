package com.ucdrive.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.ucdrive.client.commands.*;
import com.ucdrive.server.commands.*;
import com.ucdrive.refactorLater.User;
import com.ucdrive.refactorLater.Users;
import com.ucdrive.server.commands.ChangePassword;

public class AuthenticatedMenu {

    public AuthenticatedMenu() {
    }

    public void authenticatedMenu(User user, Users users, DataInputStream in, DataOutputStream out) throws IOException {
        int choice = 0;

        while (choice != 13) {
            switch (choice) {
                case -1:
                    out.writeUTF("\nInvalid option.\n\n"
                            + "\t1  - Change password\n"
                            + "\t2  - Change client's directory\n"
                            + "\t3  - Change server's directory\n"
                            + "\t4  - Configure IP and port of servers\n"
                            + "\t5  - Create new folder/file in client\n"
                            + "\t6  - Create new folder/file in server\n"
                            + "\t7  - Delete folder/file in client\n"
                            + "\t8  - Delete folder/file in client\n"
                            + "\t9  - Download a file\n"
                            + "\t10 - List client files\n"
                            + "\t11 - List server files\n"
                            + "\t12 - Upload a file\n"
                            + "\t13 - Exit\n"
                            + "Choice: ");
                    try {
                        choice = Integer.parseInt(in.readUTF());
                        out.writeUTF(String.valueOf(choice));
                    } catch (NumberFormatException e) {
                        choice = -1;
                        out.writeUTF("Invalid option.");
                    }
                case 0:
                    out.writeUTF("Choose one of the following options:\n"
                            + "\t1  - Change password\n"
                            + "\t2  - Change client's directory\n"
                            + "\t3  - Change server's directory\n"
                            + "\t4  - Configure IP and port of servers\n"
                            + "\t5  - Create new folder/file in client\n"
                            + "\t6  - Create new folder/file in server\n"
                            + "\t7  - Delete folder/file in client\n"
                            + "\t8  - Delete folder/file in client\n"
                            + "\t9  - Download a file\n"
                            + "\t10 - List client files\n"
                            + "\t11 - List server files\n"
                            + "\t12 - Upload a file\n"
                            + "\t13 - Exit\n"
                            + "Choice: ");

                    try {
                        choice = Integer.parseInt(in.readUTF());
                        out.writeUTF(String.valueOf(choice));
                    } catch (NumberFormatException e) {
                        choice = -1;
                        out.writeUTF("Invalid option.");
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
                    CreateNewClientFolder obj5 = new CreateNewClientFolder();
                    // obj5.createNewClientFolder();
                    choice = 0;
                    break;
                case 6:
                    CreateNewServerFolder obj6 = new CreateNewServerFolder();
                    // obj6.createNewServerFolder();
                    choice = 0;
                    break;
                case 7:
                    DeleteClientFolder obj7 = new DeleteClientFolder();
                    // obj7.deleteClientFolder();
                    choice = 0;
                    break;
                case 8:
                    DeleteServerFolder obj8 = new DeleteServerFolder();
                    // obj8.deleteServerFolder();
                    choice = 0;
                    break;
                case 9:
                    DownloadAFile obj9 = new DownloadAFile();
                    // obj9.downloadAFile();
                    choice = 0;
                    break;
                case 10:
                    ListClientFiles obj10 = new ListClientFiles();
                    // obj10.listClientFiles();
                    choice = 0;
                    break;
                case 11:
                    ListServerFiles obj11 = new ListServerFiles();
                    // obj11.listServerFiles(user);
                    choice = 0;
                    break;
                case 12:
                    UploadAFile obj12 = new UploadAFile();
                    // obj12.uploadAFile();
                    choice = 0;
                    break;
                case 13:
                    // exit();
                    break;
            }
        }
    }
}
