package com.cons.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.Properties;

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
    private static String fileName;
    private String initializationVector;
    private String key;
    private boolean valid;
    private String error;

    public CryptoUtils(String key, String initializationVector) {
        this.initializationVector = initializationVector;
        this.key = key;
    }

    public CryptoUtils(String fileName) {
        CryptoUtils.fileName = fileName;
    }

    public void obfuscatePasswordInConfig() {

        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(CryptoUtils.fileName);
            prop.load(input);

            int i = 0;
            boolean hasMore = true;
            while (hasMore) {
                i++;
                prop.setProperty("password." + i, encrypt(prop.getProperty("password." + i)));
            }

        } catch (FileNotFoundException e) {
            setValid(false);
            setError("Property file " + CryptoUtils.fileName + " does not exist");
        } catch (IOException io) {
            setValid(false);
            setError("Error reading from Property file " + CryptoUtils.fileName);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    setValid(false);
                    setError("Error closing Property file " + CryptoUtils.fileName);
                }
            }
        }
    }

    public String getPassword(String obfuscatedPassword) {
        return decrypt(obfuscatedPassword);
    }

    /**
     * Encrypts given string value end returns encrypted string.
     *
     * @param value String to encrypt
     * @return Encrypted string
     */
    private String encrypt(String value) {

        IvParameterSpec iv;
        try {
            iv = new IvParameterSpec((initializationVector).getBytes("UTF-8"));

            SecretKeySpec skeySpec = new SecretKeySpec((key).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return new String(Base64.encodeBase64(encrypted), "UTF-8");

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

    /**
     * Decrypts given string value end returns decrypted string.
     *
     * @param value Encrypted string to decrypt
     * @return Decryprted string
     */
    private String decrypt(String value) {


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
        return error;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static void main(String[] args) {
        System.out.println(new CryptoUtils("1234567891234567", "1234567891234567").encrypt("Hello World!"));
        System.out.println(new CryptoUtils("1234567891234567", "1234567891234567").decrypt("wIK+ZM8VNYT2U/YaTYbAjg=="));
        System.out.println(new CryptoUtils("1234567891234567", "1234567891234567").decrypt(""));
    }

}
