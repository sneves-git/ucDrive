package com.ucdrive.server;

import com.ucdrive.server.commands.*;
import com.ucdrive.refactorLater.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class AuthenticatedMenu {

    public AuthenticatedMenu() {
    }

    public int authenticatedMenu(User user, Users users, DataInputStream in, DataOutputStream out) throws IOException {
        int choice = 0;

        while (choice != 13) {
            switch (choice) {
                case -1:
                    out.writeUTF("\nInvalid option.\n\n"
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
                            + "\t13 - Exit\n"
                            + "Choice: ");

                    try {
                        choice = Integer.parseInt(in.readUTF());
                        out.writeUTF(String.valueOf(choice));
                    } catch (NumberFormatException e) {
                        choice = -1;
                        out.writeUTF("Invalid option.");
                    }
                    break;
                case 1:
                    ChangePassword obj1 = new ChangePassword();
                    obj1.changePassword(user, users, in, out);
                    choice = 0;
                    break;
                case 2:
                    ChangeClientDirectory obj2 = new ChangeClientDirectory();
                    System.out.println(user.getClientPath());
                    obj2.changeClientDirectory(user, users, in, out);
                    choice = 0;
                    break;
                case 3:
                    ChangeServerDirectory obj3 = new ChangeServerDirectory();
                    obj3.changeServerDirectory(user, users, in, out);
                    choice = 0;
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
                    CreateNewServerFolder obj6 = new CreateNewServerFolder();
                    obj6.createNewServerFolder(user.getLastSessionServer(), in, out);
                    choice = 0;
                    break;
                case 7:
                    DeleteClientFolderOrFile obj7 = new DeleteClientFolderOrFile();
                    obj7.deleteClientFolder(user.getClientPath(), out);
                    choice = 0;
                    break;
                case 8:
                    DeleteServerFolderOrFile obj8 = new DeleteServerFolderOrFile();
                    obj8.deleteServerFolder(user.getLastSessionServer(), in, out);
                    choice = 0;
                    break;
                case 9:
                    try (ServerSocket folderSocket = new ServerSocket(0)) {
                        int filePort = folderSocket.getLocalPort();
                        System.out.println("A Escuta no Porto " + filePort);

                        out.writeUTF(Integer.toString(filePort));

                        System.out.println("LISTEN SOCKET=" + folderSocket);

                        Socket clientSocket_ = folderSocket.accept(); // BLOQUEANTE

                        System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket_);

                        DownloadHelper dh = new DownloadHelper();
                        String fileName = dh.downloadHelper(user.getClientPath(), user.getLastSessionServer(), in, out);

                        if(!fileName.equals("File does not exist!")){
                            new DownloadAFile(clientSocket_, user.getLastSessionServer(), fileName);
                        }

                    } catch (Exception e) {
                        System.out.println("Error with server socket: " + e);
                    }

                    choice = 0;
                    break;
                case 10:
                    ListClientFiles obj10 = new ListClientFiles();
                    obj10.listClientFiles(user.getClientPath(), out);
                    choice = 0;
                    break;
                case 11:
                    ListServerFiles obj11 = new ListServerFiles();
                    obj11.listServerFiles(user.getLastSessionServer(), in, out);
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
        return choice;

    }
}
