package com.ucdrive.client;

import com.ucdrive.client.commands.*;
import com.ucdrive.server.commands.DownloadAFile;

import java.io.*;

public class AuthenticatedMenu {
    public AuthenticatedMenu() {
    }

    public void authenticatedMenu(DataInputStream in, DataOutputStream out, BufferedReader reader) {
        // Checks if user's input is valid
        int choice = 0;
        try {
            do {
                // System.out.println("before reading");
                System.out.print(in.readUTF());
                // System.out.println("after reading");

                out.writeUTF(reader.readLine());
                // System.out.println("after writing");

                try {
                    // System.out.println("before choice: " + choice);

                    choice = Integer.parseInt(in.readUTF());
                    // System.out.println("after choice: " + choice);

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
                        // obj2.changeClientsDirectory();
                        break;
                    case 4:
                        ConfigureIpAndPort obj4 = new ConfigureIpAndPort();
                        // obj4.configureIpAndPort();
                        break;
                    case 5:
                        CreateNewClientFolder obj5 = new CreateNewClientFolder();
                        // obj5.createNewClientFolder();
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
            } while (choice != 13);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}
