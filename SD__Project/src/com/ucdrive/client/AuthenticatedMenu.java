package com.ucdrive.client;

import com.ucdrive.client.commands.*;

import java.io.*;
import java.net.Socket;

public class AuthenticatedMenu {
    public AuthenticatedMenu() {
    }

    public int authenticatedMenu(Socket s, DataInputStream in, DataOutputStream out, BufferedReader reader,
            IpAndPort ipAndPort) {
        // Checks if user's input is valid
        int choice = 0;
        try {
            do {
                //System.out.println("before reading");
                // printing Menu
                System.out.print(in.readUTF());
                //System.out.println("after reading");

                // Read and send to server choice
                out.writeUTF(reader.readLine());
                //System.out.println("after writing");

                try {
                    //System.out.println("before choice: " + choice);

                    // Read choice back and confirms if choice is invalid
                    choice = Integer.parseInt(in.readUTF());

                    //System.out.println("after choice: " + choice);

                } catch (NumberFormatException e) {
                    choice = 0;
                }

                switch (choice) {
                    case 1:
                        ChangePassword obj1 = new ChangePassword();
                        obj1.changePassword(in, out, reader);
                        break;
                    case 2:
                        ChangeClientDirectory obj2 = new ChangeClientDirectory();
                        obj2.changeClientDirectory(in, out, reader);
                        break;
                    case 3:
                        ChangeServerDirectory obj3 = new ChangeServerDirectory();
                        obj3.changeServerDirectory(in, out, reader);
                        break;
                    case 4:
                        ipAndPort.configureIpAndPortAfterLogin(reader);

                        in.close();
                        out.close();
                        s.close();

                        System.out.println(ipAndPort);
                        choice = 4;
                        break;
                    case 5:
                        CreateNewClientFolder obj5 = new CreateNewClientFolder();
                        obj5.createNewClientFolder(in, out, reader);
                        break;
                    case 6:
                        CreateNewServerFolder obj6 = new CreateNewServerFolder();
                        obj6.createNewServerFolder(in, out, reader);
                        break;
                    case 7:
                        DeleteClientFolder obj7 = new DeleteClientFolder();
                        // obj7.deleteClientFolder();
                        break;
                    case 9:
                        DownloadAFile obj9 = new DownloadAFile();
                        // obj9.downloadAFile();
                        break;
                    case 10:
                        ListClientFiles obj10 = new ListClientFiles();
                        // obj10.listClientFiles();
                        break;
                    case 12:
                        UploadAFile obj12 = new UploadAFile();
                        // obj12.uploadAFile();
                        break;
                    case 13:
                        // exit();
                        break;
                }
            } while (choice != 13 && choice != 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return choice;
    }
}
