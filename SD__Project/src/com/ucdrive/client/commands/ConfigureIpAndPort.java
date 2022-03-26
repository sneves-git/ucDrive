package com.ucdrive.client.commands;

import com.ucdrive.client.IpAndPort;

import java.util.Scanner;

public class ConfigureIpAndPort {
    private Scanner sc;

    public ConfigureIpAndPort() {
        sc = new Scanner(System.in);
    }

    public IpAndPort configureIpAndPort() {
        IpAndPort s = null;

        System.out.println("Primary server IP: ");
        if (sc.hasNextLine()) {
            // = sc.nextLine();
        }

        System.out.println("Primary server Port: ");
        if (sc.hasNextLine()) {
            // = Integer.parseInt(sc.nextLine());
        }

        System.out.println("Secondary server IP: ");
        if (sc.hasNextLine()) {
            // = sc.nextLine();
        }

        System.out.println("Secondary server Port: ");
        if (sc.hasNextLine()) {
            // = Integer.parseInt(sc.nextLine());
        }

        return s;
    }

}