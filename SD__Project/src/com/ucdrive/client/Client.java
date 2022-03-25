package com.ucdrive.client;

import java.util.*;
import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {
        // Login
        Socket s = null;
        int serverPort = -1, choice = 0;
        String path = "../configs/";
        String fileName = "configs_ip_port.txt";
        String ipAddress = null;
        Writer output;

        // Read ip and port info
        try {
            File myObj = new File(path + fileName);
            Scanner myReader = new Scanner(myObj);

            if (myReader.hasNextLine()) {
                ipAddress = myReader.nextLine();
            }
            if (myReader.hasNextLine()) {
                serverPort = Integer.parseInt(myReader.nextLine());
            }
            myReader.close();
        } catch (IOException e1) {
            System.out.println("An error occurred with " + fileName + ".");
            e1.printStackTrace();
        }
        Menu menu = new Menu();
        do {
            choice = 2;
            switch (choice) {
                case 1:
                    break;
                case 2:
                    try {
                        // 1o passo
                        s = new Socket(ipAddress, serverPort);
                        System.out.println("SERVER SOCKER = " + serverPort);
                        System.out.println("SOCKET=" + s);
                        // 2o passo
                        DataInputStream in = new DataInputStream(s.getInputStream());
                        DataOutputStream out = new DataOutputStream(s.getOutputStream());

                        String texto = "";
                        InputStreamReader input = new InputStreamReader(System.in);
                        BufferedReader reader = new BufferedReader(input);
                        System.out.println("Introduza texto:");

                        // 3o passo
                        while (true) {
                            // READ STRING FROM KEYBOARD
                            try {
                                texto = reader.readLine();
                            } catch (Exception e) {
                            }

                            // WRITE INTO THE SOCKET
                            out.writeUTF(texto);

                            // READ FROM SOCKET
                            String data = in.readUTF();

                            // DISPLAY WHAT WAS READ
                            System.out.println("Received: " + data);
                        }

                    } catch (UnknownHostException e) {
                        System.out.println("Sock:" + e.getMessage());
                    } catch (EOFException e) {
                        System.out.println("EOF:" + e.getMessage());
                    } catch (IOException e) {
                        System.out.println("IO:" + e.getMessage());
                    } finally {
                        if (s != null)
                            try {
                                s.close();
                            } catch (IOException e) {
                                System.out.println("close:" + e.getMessage());
                            }
                    }
                    break;
                case 3:
                    break;
            }
        } while (choice == 1);

    }

}

