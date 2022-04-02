package com.ucdrive.client;

import com.ucdrive.client.commands.*;
import com.ucdrive.utils.ConsoleColors;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class AuthenticatedMenu {
    public AuthenticatedMenu() {
    }

    public int authenticatedMenu(Socket s, DataInputStream in, DataOutputStream out,
            BufferedReader reader,
            IpAndPort ipAndPort) throws IOException {
        // Checks if user's input is valid
        Socket sFile;
        int choice = 0;
        String option = in.readUTF();

        if (!option.equals("Menu!")) {
            try {
                choice = Integer.parseInt(option);
                System.out.println(ConsoleColors.RED +"Due to an error in the last session your previous operation did not finish successfully!" + ConsoleColors.RESET);
            } catch (NumberFormatException e) {
                choice = 0;
            }
        }

        try {
            do {
                if (choice == 0) {
                    // printing Menu
                    System.out.print(in.readUTF());

                    // Read and send to server choice
                    out.writeUTF(reader.readLine());

                    try {
                        // Read choice back and confirms if choice is invalid
                        choice = Integer.parseInt(in.readUTF());
                        if(choice < 1 || choice > 13){
                            choice = 0;
                        }
                    } catch (NumberFormatException e) {
                        choice = 0;
                    }
                }

                switch (choice) {

                    case 1:
                        ChangePassword obj1 = new ChangePassword();
                        obj1.changePassword(in, out, reader);
                        break;

                    case 2:
                        ChangeClientDirectory obj2 = new ChangeClientDirectory();
                        obj2.changeClientDirectory(in, out, reader);
                        choice = 0;
                        break;
                    case 3:
                        ChangeServerDirectory obj3 = new ChangeServerDirectory();
                        obj3.changeServerDirectory(in, out, reader);
                        choice = 0;
                        break;
                    case 4:
                        ipAndPort.configureIpAndPortAfterLogin(reader);

                        in.close();
                        out.close();
                        s.close();

                        System.out.println(ipAndPort);
                        break;
                    case 5:
                        CreateNewClientFolder obj5 = new CreateNewClientFolder();
                        obj5.createNewClientFolder(in, out, reader);
                        choice = 0;
                        break;
                    case 6:
                        CreateNewServerFolder obj6 = new CreateNewServerFolder();
                        obj6.createNewServerFolder(in, out, reader);
                        choice = 0;
                        break;
                    case 7:

                        DeleteClientFolderOrFile obj7 = new DeleteClientFolderOrFile();
                        obj7.deleteClientFolder(in, out, reader);
                        choice = 0;
                        break;
                    case 8:
                        DeleteServerFolderOrFile obj8 = new DeleteServerFolderOrFile();
                        obj8.deleteServerFolder(in, out, reader);
                        choice = 0;
                        break;
                    case 9:
                        // Receives new port to use for download
                        ipAndPort.setPrimaryFilePort(Integer.parseInt(in.readUTF()));

                        // Establishes connection with server for download
                        sFile = new Socket(ipAndPort.getPrimaryIp(), ipAndPort.getPrimaryFilePort());

                        // Asks user which file he wants to download
                        DownloadHelper helper = new DownloadHelper();
                        helper.downloadHelper(in, out, reader, sFile);
                        choice = 0;
                        break;
                    case 10:
                        ListClientFiles obj10 = new ListClientFiles();
                        obj10.listClientFiles(in, out, true);
                        choice = 0;
                        break;
                    case 11:
                        ListServerFiles obj11 = new ListServerFiles();
                        obj11.listServerFiles(in, true);
                        choice = 0;
                        break;
                    case 12:
                        // Receives new port to use for download
                        ipAndPort.setPrimaryFilePort(Integer.parseInt(in.readUTF()));

                        // Establishes connection with server for download
                        sFile = new Socket(ipAndPort.getPrimaryIp(), ipAndPort.getPrimaryFilePort());

                        UploadHelper obj12 = new UploadHelper();
                        obj12.uploadHelper(in, out, reader, sFile);
                        choice = 0;
                        break;
                    case 13:
                        System.out.println(ConsoleColors.GREEN + "Logging out...\n" + ConsoleColors.RESET);
                        break;
                }
            } while (choice != 13 && choice != 4 && choice != 1);
        } catch (SocketException b) {
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return choice;
    }
}
