package com.cons.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


/**
 * Helper Class for obfuscating passwords in a config file
 */
public class CryptoUtils {
    private String fileName;
    private static final String initializationVector = System.getProperty("java.class.path")
                                                             .replaceAll(System.getProperty("file.separator"), "")
                                                             .trim()
                                                             .substring(0, 16);
    private static final String key = System.getProperty("java.class.path")
                                            .replaceAll(System.getProperty("file.separator"), "")
                                            .trim()
                                            .substring(17, 33);
    private static boolean valid;
    private static String error;

    public CryptoUtils() {
    }

    public CryptoUtils(String fileName) {
        this.fileName = fileName;
    }

    public static void obfuscatePasswordInConfig(String fileName) {

        // This will reference one line at a time
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("password.")) {
                    if (CryptoUtils.decrypt(line.split("=", 2)[1]) == null) {
                        line = line.split("=", 2)[0] + "=" + CryptoUtils.encrypt(line.split("=", 2)[1]);
                    }
                }
                sb.append(line);
                sb.append(System.getProperty("line.separator"));
            }

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            CryptoUtils.setValid(false);
            CryptoUtils.setError("Property file " + fileName + " not found.");
        } catch (IOException ex) {
            ex.printStackTrace();
            CryptoUtils.setValid(false);
            CryptoUtils.setError("Error reading file '" + fileName + "'");
        }

        FileWriter fileWriter;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            // Always wrap FileReader in BufferedReader.
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(sb.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
            CryptoUtils.setValid(false);
            CryptoUtils.setError("Error reading file '" + fileName + "'");
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void deObfuscatePasswordInConfig(String fileName) {
        // This will reference one line at a time
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("password.")) {
                    if (CryptoUtils.decrypt(line.split("=", 2)[1]) != null) {
                        line = line.split("=", 2)[0] + "=" + CryptoUtils.decrypt(line.split("=", 2)[1]);
                    }
                }
                sb.append(line);
                sb.append(System.getProperty("line.separator"));
            }

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            CryptoUtils.setValid(false);
            CryptoUtils.setError("Property file " + fileName + " not found.");
        } catch (IOException ex) {
            ex.printStackTrace();
            CryptoUtils.setValid(false);
            CryptoUtils.setError("Error reading file '" + fileName + "'");
        }

        FileWriter fileWriter;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            // Always wrap FileReader in BufferedReader.
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(sb.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
            CryptoUtils.setValid(false);
            CryptoUtils.setError("Error reading file '" + fileName + "'");
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Encrypts given string value end returns encrypted string.
     *
     * @param value String to encrypt
     * @return Encrypted string
     */
    private static String encrypt(String value) {

        IvParameterSpec iv;
        try {
            iv = new IvParameterSpec((initializationVector).getBytes("UTF-8"));

            SecretKeySpec skeySpec = new SecretKeySpec((key).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return new String(Base64.encodeBase64(encrypted), "UTF-8");

        } catch (UnsupportedEncodingException ex) {
            CryptoUtils.setValid(false);
            CryptoUtils.setError("UnsupportedEncodingException " + ex.getMessage());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            CryptoUtils.setValid(false);
            CryptoUtils.setError("NoSuchAlgorithmException | NoSuchPaddingException " + ex.getMessage());
        } catch (InvalidAlgorithmParameterException | InvalidKeyException ex) {
            CryptoUtils.setValid(false);
            CryptoUtils.setError("InvalidAlgorithmParameterException | InvalidKeyException " + ex.getMessage());
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            CryptoUtils.setValid(false);
            CryptoUtils.setError("BadPaddingException | IllegalBlockSizeException " + ex.getMessage());
        }

        return null;
    }


    /**
     * Decrypts given string value end returns decrypted string.
     *
     * @param value Encrypted string to decrypt
     * @return Decryprted string
     */
    public static String decrypt(String value) {

        IvParameterSpec iv;
        try {
            iv = new IvParameterSpec((initializationVector).getBytes("UTF-8"));

            SecretKeySpec skeySpec = new SecretKeySpec((key).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(value.getBytes()));

            return new String(original);
        } catch (UnsupportedEncodingException ex) {
            setValid(false);
            setError("UnsupportedEncodingException " + ex.getMessage());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            setValid(false);
            setError("NoSuchAlgorithmException | NoSuchPaddingException " + ex.getMessage());
        } catch (InvalidAlgorithmParameterException | InvalidKeyException ex) {
            setValid(false);
            setError("InvalidAlgorithmParameterException | InvalidKeyException " + ex.getMessage());
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            setValid(false);
            setError("BadPaddingException | IllegalBlockSizeException " + ex.getMessage());
        }

        return null;
    }

    public String getError() {
        return CryptoUtils.error;
    }

    public static void setValid(boolean valid) {
        CryptoUtils.valid = valid;
    }

    public boolean isValid() {
        return CryptoUtils.valid;
    }

    public static void setError(String error) {
        CryptoUtils.error = error;
    }

}
