package com.ucdrive.client;

import com.ucdrive.client.commands.Login;

import java.util.*;
import java.io.*;
import java.net.*;

public class Client {
    private static Socket sCommand = null;
    private static IpAndPort ipAndPort = new IpAndPort();

    public static void main(String[] args) {

        // Login
        int choice = 0;
        String path = "src/com/ucdrive/configs/";
        String primaryFileName = "primary_server_ip_port.txt";
        String secondaryFileName = "secondary_server_ip_port.txt";

        // Read ip and port info
        try {
            File myObj = new File(path + primaryFileName);
            Scanner myReader = new Scanner(myObj);

            if (myReader.hasNextLine()) {
                ipAndPort.setPrimaryIp(myReader.nextLine());
            }
            if (myReader.hasNextLine()) {
                ipAndPort.setPrimaryCommandPort(Integer.parseInt(myReader.nextLine()));
            }
            myReader.close();
        } catch (IOException e1) {
            System.out.println("An error occurred with " + primaryFileName + ".");
            e1.printStackTrace();
        }

        // Read ip and port info
        try {
            File myObj = new File(path + secondaryFileName);
            Scanner myReader = new Scanner(myObj);

            if (myReader.hasNextLine()) {
                ipAndPort.setSecondaryIp(myReader.nextLine());
            }
            if (myReader.hasNextLine()) {
                ipAndPort.setSecondaryCommandPort(Integer.parseInt(myReader.nextLine()));
            }
            myReader.close();
        } catch (IOException e1) {
            System.out.println("An error occurred with " + secondaryFileName + ".");
            e1.printStackTrace();
        }

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        FirstMenu menu = new FirstMenu(reader);

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
                        try {
                            sCommand = new Socket(ipAndPort.getPrimaryIp(), ipAndPort.getPrimaryCommandPort());

                        } catch (SocketException e) {
                            try {
                                String auxIp = ipAndPort.getSecondaryIp();
                                int auxPort = ipAndPort.getSecondaryCommandPort();

                                ipAndPort.setSecondaryIp(ipAndPort.getPrimaryIp());
                                ipAndPort.setSecondaryCommandPort(ipAndPort.getPrimaryCommandPort());
                                ipAndPort.setPrimaryIp(auxIp);
                                ipAndPort.setPrimaryCommandPort(auxPort);
                                System.out.println(ipAndPort.getPrimaryIp());
                                System.out.println(ipAndPort.getPrimaryCommandPort());
                                System.out.println(ipAndPort.getSecondaryIp());
                                System.out.println(ipAndPort.getSecondaryCommandPort());

                                sCommand = new Socket(ipAndPort.getPrimaryIp(), ipAndPort.getPrimaryCommandPort());

                            } catch (SocketException se) {
                                break;
                            }
                        }

                        System.out.println("SERVER PORT = " + ipAndPort.getPrimaryCommandPort());
                        System.out.println("SOCKET=" + sCommand);

                        // 2o passo
                        DataInputStream in = new DataInputStream(sCommand.getInputStream());
                        DataOutputStream out = new DataOutputStream(sCommand.getOutputStream());

                        out.writeUTF("login");
                        Login login = new Login();
                        login.login_(in, out, reader);
                        System.out.println("Logged in!");

                        System.out.println(ipAndPort);

                        AuthenticatedMenu authMenu = new AuthenticatedMenu();
                        choice = authMenu.authenticatedMenu(sCommand, in, out, reader, ipAndPort);

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