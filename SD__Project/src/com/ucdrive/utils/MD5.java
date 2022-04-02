package com.ucdrive.utils;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.security.*;


public class MD5 {

    public MD5() {
    }

    public String md5(String fileName) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(Files.readAllBytes(Paths.get(fileName)));

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, digest);

        // Convert message digest into hex value
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }

        return hashtext;
    }

}
