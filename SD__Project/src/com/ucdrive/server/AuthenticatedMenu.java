package com.ucdrive.server;

import com.ucdrive.server.commands.*;
import com.ucdrive.configs.UsersConfigsFile;
import com.ucdrive.utils.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class AuthenticatedMenu {

    public AuthenticatedMenu() {
    }

    public int authenticatedMenu(User user, Users users, DataInputStream in, DataOutputStream out, String server)
            throws IOException {
        int choice = 0;
        UsersConfigsFile conf = new UsersConfigsFile();
        choice = user.getLastChoice();

        while (choice != 13) {
            switch (choice) {
                case -1:
                    out.writeUTF(ConsoleColors.RED + "Invalid option.\n\n" + ConsoleColors.RESET
                            + "Choose one of the following options:\n"
                            + "\t1  - Change password\n"
                            + "\t2  - Change client's directory\n"
                            + "\t3  - Change server's directory\n"
                            + "\t4  - Configure IP and port of servers\n"
                            + "\t5  - Create new folder in client\n"
                            + "\t6  - Create new folder in server\n"
                            + "\t7  - Delete folder/file in client\n"
                            + "\t8  - Delete folder/file in server\n"
                            + "\t9  - Download a file\n"
                            + "\t10 - List client files\n"
                            + "\t11 - List server files\n"
                            + "\t12 - Upload a file\n"
                            + "\t13 - Exit\n"
                            + "Choice: ");
                    try {
                        choice = Integer.parseInt(in.readUTF());
                        out.writeUTF(String.valueOf(choice));

                        if(choice < 1 || choice > 13){
                            choice = -1;
                        }
                    } catch (NumberFormatException e) {
                        choice = -1;
                        out.writeUTF("Invalid option.");
                    }
                    break;
                case 0:
                    out.writeUTF("Choose one of the following options:\n"
                            + "\t1  - Change password\n"
                            + "\t2  - Change client's directory\n"
                            + "\t3  - Change server's directory\n"
                            + "\t4  - Configure IP and port of servers\n"
                            + "\t5  - Create new folder in client\n"
                            + "\t6  - Create new folder in server\n"
                            + "\t7  - Delete folder/file in client\n"
                            + "\t8  - Delete folder/file in Server\n"
                            + "\t9  - Download a file\n"
                            + "\t10 - List client files\n"
                            + "\t11 - List server files\n"
                            + "\t12 - Upload a file\n"
                            + "\t13 - Logout\n"
                            + "Choice: ");

                    try {
                        choice = Integer.parseInt(in.readUTF());
                        out.writeUTF(String.valueOf(choice));

                        if(choice < 1 || choice > 13){
                            choice = -1;
                        }
                    } catch (NumberFormatException e) {
                        choice = -1;
                        out.writeUTF("Invalid option.");
                    }
                    break;
                case 1:
                    conf.updateLastChoice(1, user, users);

                    ChangePassword obj1 = new ChangePassword();
                    obj1.changePassword(user, users, in, out);

                    choice = 0;
                    conf.updateLastChoice(0, user, users);

                    break;
                case 2:
                    conf.updateLastChoice(2, user, users);
                    ChangeClientDirectory obj2 = new ChangeClientDirectory();

                    obj2.changeClientDirectory(user, users, in, out);
                    choice = 0;
                    conf.updateLastChoice(0, user, users);

                    break;
                case 3:
                    conf.updateLastChoice(3, user, users);

                    ChangeServerDirectory obj3 = new ChangeServerDirectory();
                    obj3.changeServerDirectory(user, users, in, out, server);
                    choice = 0;
                    conf.updateLastChoice(0, user, users);

                    break;
                case 4:
                    // Configure IPandPort of client
                    break;
                case 5:

                    CreateNewClientFolder obj5 = new CreateNewClientFolder();
                    obj5.createNewClientFolder(user.getClientPath(), out);
                    choice = 0;

                    break;
                case 6:
                    conf.updateLastChoice(6, user, users);

                    CreateNewServerFolder obj6 = new CreateNewServerFolder();
                    obj6.createNewServerFolder(user.getLastSessionServer(), in, out, server);
                    choice = 0;
                    conf.updateLastChoice(0, user, users);

                    break;
                case 7:
                    ListClientFiles obj14 = new ListClientFiles();
                    obj14.listClientFiles(user.getClientPath(), out);

                    conf.updateLastChoice(7, user, users);

                    DeleteClientFolderOrFile obj7 = new DeleteClientFolderOrFile();
                    obj7.deleteClientFolder(user.getClientPath(), out);
                    choice = 0;
                    conf.updateLastChoice(0, user, users);

                    break;
                case 8:
                    ListServerFiles obj15 = new ListServerFiles();
                    obj15.listServerFiles(user.getLastSessionServer(), in, out, server);

                    conf.updateLastChoice(8, user, users);

                    DeleteServerFolderOrFile obj8 = new DeleteServerFolderOrFile();
                    obj8.deleteServerFolder(user.getLastSessionServer(), in, out, server);
                    choice = 0;
                    conf.updateLastChoice(0, user, users);

                    break;
                case 9:
                    conf.updateLastChoice(9, user, users);

                    try (ServerSocket folderSocket = new ServerSocket(0)) {
                        int filePort = folderSocket.getLocalPort();
                        out.writeUTF(Integer.toString(filePort));

                        Socket clientSocket_ = folderSocket.accept(); // BLOQUEANTE

                        System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket_);

                        DownloadHelper dh = new DownloadHelper();
                        String fileName = dh.downloadHelper(user.getClientPath(), user.getLastSessionServer(), in, out,
                                server, 0, user, users);

                        if (!fileName.equals("File does not exist!")) {
                            new DownloadAFile(clientSocket_, user.getLastSessionServer(), server, fileName, user, users,
                                    0);
                        }

                    } catch (Exception e) {
                        System.out.println("Error with server socket: " + e);
                    }

                    choice = 0;

                    break;
                case 10:
                    conf.updateLastChoice(10, user, users);

                    ListClientFiles obj10 = new ListClientFiles();
                    obj10.listClientFiles(user.getClientPath(), out);
                    choice = 0;
                    conf.updateLastChoice(0, user, users);

                    break;
                case 11:
                    conf.updateLastChoice(11, user, users);

                    ListServerFiles obj11 = new ListServerFiles();
                    obj11.listServerFiles(user.getLastSessionServer(), in, out, server);
                    choice = 0;
                    conf.updateLastChoice(0, user, users);

                    break;
                case 12:
                    conf.updateLastChoice(12, user, users);

                    try (ServerSocket folderSocket = new ServerSocket(0)) {
                        int filePort = folderSocket.getLocalPort();
                        out.writeUTF(Integer.toString(filePort));
                        Socket clientSocket_ = folderSocket.accept(); // BLOQUEANTE

                        System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket_);

                        UploadHelper helper = new UploadHelper();
                        helper.uploadHelper(user.getClientPath(), user.getLastSessionServer(), server, in, out,
                                clientSocket_, user, users);
                    } catch (Exception e) {
                        System.out.println("Error with server socket: " + e);
                    }

                    choice = 0;
                    break;
                case 13:
                    conf.updateLastChoice(0, user, users);
                    // exit();
                    break;
                default:
                    choice = -1;
                    break;
            }
        }
        return choice;

    }
}
