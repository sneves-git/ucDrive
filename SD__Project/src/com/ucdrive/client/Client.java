package com.ucdrive.client;

import com.ucdrive.client.commands.Login;
import com.ucdrive.client.commands.ConfigureIpAndPort;
import com.ucdrive.refactorLater.User;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.net.*;

public class Client {
    private static Socket sCommand = null, sFile = null;
    private static int serverPort = -1;
    private static String primaryIpAddress = null, secondaryIpAddress = null;
    private static IpAndPort ipAndPort = new IpAndPort();

    public static void main(String[] args) {

        // Login
        int choice = 0;
        String path = "src/com/ucdrive/configs/";
        String fileName = "primary_server_ip_port.txt";

        // Read ip and port info
        try {
            File myObj = new File(path + fileName);
            Scanner myReader = new Scanner(myObj);

            if (myReader.hasNextLine()) {
                ipAndPort.setPrimaryIp(myReader.nextLine());
            }
            if (myReader.hasNextLine()) {
                ipAndPort.setPrimaryCommandPort(Integer.parseInt(myReader.nextLine()));
            }
            myReader.close();
        } catch (IOException e1) {
            System.out.println("An error occurred with " + fileName + ".");
            e1.printStackTrace();
        }

        FirstMenu menu = new FirstMenu();

        do {
            choice = menu.chooseOption();
            switch (choice) {
                case 1:
                    try {
                        System.out.println(ipAndPort);

                        ipAndPort.configureIpAndPortBeforeLogin();

                        System.out.println(ipAndPort);

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    break;
                case 2:
                    try {
                        // 1o passo
                        sCommand = new Socket(ipAndPort.getPrimaryIp(), ipAndPort.getPrimaryCommandPort());
                        System.out.println("SERVER PORT = " + ipAndPort.getPrimaryCommandPort());
                        System.out.println("SOCKET=" + sCommand);

                        // 2o passo
                        DataInputStream in = new DataInputStream(sCommand.getInputStream());
                        DataOutputStream out = new DataOutputStream(sCommand.getOutputStream());

                        String texto = "";
                        InputStreamReader input = new InputStreamReader(System.in);
                        BufferedReader reader = new BufferedReader(input);

                        out.writeUTF("login");
                        Login login = new Login();
                        login.login_(in, out, reader);
                        System.out.println("Logged in!");

                        System.out.println(ipAndPort);
                        AuthenticatedMenu authMenu = new AuthenticatedMenu();

                        choice = authMenu.authenticatedMenu(sCommand, sFile, in, out, reader, ipAndPort);

                        /*
                         * 
                         * System.out.println("Introduza texto:");
                         * 
                         * // 3o passo
                         * while (true) {
                         * // READ STRING FROM KEYBOARD
                         * try {
                         * texto = reader.readLine();
                         * } catch (Exception e) {
                         * }
                         * 
                         * // WRITE INTO THE SOCKET
                         * out.writeUTF(texto);
                         * 
                         * // READ FROM SOCKET
                         * String data = in.readUTF();
                         * 
                         * // DISPLAY WHAT WAS READ
                         * System.out.println("Received: " + data);
                         * }
                         * 
                         */

                    } catch (UnknownHostException e) {
                        System.out.println("Sock:" + e.getMessage());
                    } catch (EOFException e) {
                        System.out.println("EOF:" + e.getMessage());
                    } catch (IOException e) {
                        System.out.println("IO:" + e.getMessage());
                    } finally {
                        if (sCommand != null)
                            try {
                                sCommand.close();
                            } catch (IOException e) {
                                System.out.println("close:" + e.getMessage());
                            }
                    }
                    break;
                case 3:
                    break;
            }
        } while (choice != 3);

    }

}