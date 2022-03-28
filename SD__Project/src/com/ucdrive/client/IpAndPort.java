package com.ucdrive.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class IpAndPort {
    private int primaryCommandPort, primaryFilePort, secondaryCommandPort, secondaryFilePort;
    private String primaryIp, secondaryIp;
    private Scanner sc;

    public IpAndPort() {
        this.primaryCommandPort = -1;
        this.primaryFilePort = -1;
        this.primaryIp = null;

        this.secondaryCommandPort = -1;
        this.secondaryFilePort = -1;
        this.secondaryIp = null;

        sc = new Scanner(System.in);
    }

    public IpAndPort(String primaryIp, int primaryCommandPort, String secondaryIp, int secondaryCommandPort) {
        this.primaryIp = primaryIp;
        this.primaryCommandPort = primaryCommandPort;

        this.secondaryIp = secondaryIp;
        this.secondaryCommandPort = secondaryCommandPort;
    }

    // functions
    public void configureIpAndPortBeforeLogin() throws IOException {
        System.out.println("Primary server IP: ");
        if (sc.hasNextLine()) {
            primaryIp = sc.nextLine();
        }

        int aux = -1;
        do {
            System.out.println("Primary server Port: ");
            if (sc.hasNextLine()) {
                try {
                    aux = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    aux = -1;
                }
            }
        } while (aux == -1);
        primaryCommandPort = aux;

        System.out.println("Secondary server IP: ");
        if (sc.hasNextLine()) {
            secondaryIp = sc.nextLine();
        }

        aux = -1;
        do {
            System.out.println("Secondary server Port: ");
            if (sc.hasNextLine()) {
                try {
                    aux = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    aux = -1;
                }
            }
        } while (aux == -1);
        secondaryCommandPort = aux;
    }

    public void configureIpAndPortAfterLogin(BufferedReader reader)
            throws IOException {
        System.out.println("Primary server IP: ");
        primaryIp = reader.readLine();

        System.out.println("Primary server Port: ");
        int aux = -1;
        do {
            try {
                aux = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number, please choose an integer number.");
                aux = -1;
            }
        } while (aux == -1);
        primaryCommandPort = aux;

        System.out.println("Secondary server IP: ");
        secondaryIp = reader.readLine();

        System.out.println("Secondary server Port: ");
        aux = -1;
        do {
            try {
                aux = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number, please choose an integer number.");
                aux = -1;
            }
        } while (aux == -1);
        secondaryCommandPort = aux;
    }

    // getters and setters
    public int getPrimaryCommandPort() {
        return primaryCommandPort;
    }

    public void setPrimaryCommandPort(int primaryCommandPort) {
        this.primaryCommandPort = primaryCommandPort;
    }

    public int getPrimaryFilePort() {
        return primaryFilePort;
    }

    public void setPrimaryFilePort(int primaryFilePort) {
        this.primaryFilePort = primaryFilePort;
    }

    public int getSecondaryCommandPort() {
        return secondaryCommandPort;
    }

    public void setSecondaryCommandPort(int secondaryCommandPort) {
        this.secondaryCommandPort = secondaryCommandPort;
    }

    public int getSecondaryFilePort() {
        return secondaryFilePort;
    }

    public void setSecondaryFilePort(int secondaryFilePort) {
        this.secondaryFilePort = secondaryFilePort;
    }

    public String getPrimaryIp() {
        return primaryIp;
    }

    public void setPrimaryIp(String primaryIp) {
        this.primaryIp = primaryIp;
    }

    public String getSecondaryIp() {
        return secondaryIp;
    }

    public void setSecondaryIp(String secondaryIp) {
        this.secondaryIp = secondaryIp;
    }

    @Override
    public String toString() {
        return "IpAndPort{" +
                "primaryCommandPort=" + primaryCommandPort +
                ", primaryFilePort=" + primaryFilePort +
                ", secondaryCommandPort=" + secondaryCommandPort +
                ", secondaryFilePort=" + secondaryFilePort +
                ", primaryIp='" + primaryIp + '\'' +
                ", secondaryIp='" + secondaryIp + '\'' +
                '}';
    }
}
