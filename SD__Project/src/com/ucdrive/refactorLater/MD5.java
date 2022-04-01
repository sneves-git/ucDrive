package com.ucdrive.refactorLater;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.*;


public class MD5 {

    public MD5() {
    }

    public String md5(String fileName) throws NoSuchAlgorithmException, IOException {
        // String checksum = "5EB63BBBE01EEED093CB22BB8F5ACDC3";

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(Files.readAllBytes(Paths.get(fileName)));
        System.out.println("BEFORE DIGEST");
        // digest() method is called to calculate message digest
        // of an input digest() return array of byte

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, digest);

        // Convert message digest into hex value
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }

        System.out.println("MD5: " + hashtext);
        return hashtext;
        // String myChecksum = DatatypeConverter.printHexBinary(digest).toUpperCase();

        // assertThat(myChecksum.equals(checksum)).isTrue();
    }

}
