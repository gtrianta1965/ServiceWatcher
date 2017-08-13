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
import org.apache.log4j.Logger;

/**
 * Helper Class for obfuscating passwords in a config file
 */
public class CryptoUtils {
    
    static final Logger logger = Logger.getLogger(CryptoUtils.class);    

    private static String initializationVector;
    private static String key;
    private static boolean valid;
    private static String error;

    static {
        
        logger.info("Initializing key and initialization vector.");
        String systemProperties =
            getStringto16Length(System.getProperty("user.name") + System.getProperty("os.name") +
                                System.getProperty("os.version") + System.getProperty("os.arch"));
        key = "1234567890123456";//systemProperties;
        logger.debug("key=[" + systemProperties + "]");        
        initializationVector = new StringBuilder(systemProperties).reverse().toString();
        logger.debug("initializationVector=" + initializationVector);
    }

    public CryptoUtils() {
    }

    public static void obfuscatePasswordInConfig(String fileName) {
        logger.debug("Initializing Obfuscation procedure");
        // This will reference one line at a time
        String line = null;
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("password") && !line.startsWith("#")) {
                    if (CryptoUtils.decrypt(line.split("=", 2)[1]) == null) {
                        line = line.split("=", 2)[0] + "=" + CryptoUtils.encrypt(line.split("=", 2)[1]);
                    }
                }
                sb.append(line);
                sb.append(System.getProperty("line.separator"));
            }
            logger.info("File: " + fileName + " obfuscated");
        } catch (FileNotFoundException ex) {
            logger.error("Property file '" + fileName + "' not found.\nIOException: " + ex.getMessage());
            CryptoUtils.setValid(false);
            CryptoUtils.setError("Property file " + fileName + " not found.");
        } catch (IOException ex) {
            logger.error("Error reading file '" + fileName + "'\nIOException: " + ex.getMessage());
            CryptoUtils.setValid(false);
            CryptoUtils.setError("Error reading file '" + fileName + "'");
        } finally {
            try {
                // Always close files.
                bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        logger.debug("Writing changes to file");

        FileWriter fileWriter;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            // Always wrap FileReader in BufferedReader.
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(sb.toString());

            logger.debug("Obfuscation completed");
        } catch (IOException ex) {
            logger.error("Error reading file '" + fileName + "'\nIOException: " + ex.getMessage());
            CryptoUtils.setValid(false);
            CryptoUtils.setError("Error reading file '" + fileName + "'");
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException ex) {
                logger.error("Error while closing bufferWriter\nIOException: " + ex.getMessage());
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
            logger.error("UnsupportedEncodingException " + ex.getMessage());
            CryptoUtils.setValid(false);
            CryptoUtils.setError("UnsupportedEncodingException " + ex.getMessage());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            logger.error("NoSuchAlgorithmException | NoSuchPaddingException " + ex.getMessage());
            CryptoUtils.setValid(false);
            CryptoUtils.setError("NoSuchAlgorithmException | NoSuchPaddingException " + ex.getMessage());
        } catch (InvalidAlgorithmParameterException | InvalidKeyException ex) {
            logger.error("InvalidAlgorithmParameterException | InvalidKeyException " + ex.getMessage());
            CryptoUtils.setValid(false);
            CryptoUtils.setError("InvalidAlgorithmParameterException | InvalidKeyException " + ex.getMessage());
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            logger.error("BadPaddingException | IllegalBlockSizeException " + ex.getMessage());
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

            logger.trace("value=" + value);
            logger.trace("Base64 Value=" + Base64.decodeBase64(value.getBytes()));
            
            byte[] original = cipher.doFinal(Base64.decodeBase64(value.getBytes()));

            return new String(original);
        } catch (UnsupportedEncodingException ex) {
            logger.error("UnsupportedEncodingException " + ex.getMessage());
            setValid(false);
            setError("UnsupportedEncodingException " + ex.getMessage());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            logger.error("NoSuchAlgorithmException | NoSuchPaddingException " + ex.getMessage());
            setValid(false);
            setError("NoSuchAlgorithmException | NoSuchPaddingException " + ex.getMessage());
        } catch (InvalidAlgorithmParameterException | InvalidKeyException ex) {
            logger.error("InvalidAlgorithmParameterException | InvalidKeyException " + ex.getMessage());
            setValid(false);
            setError("InvalidAlgorithmParameterException | InvalidKeyException " + ex.getMessage());
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            logger.error("BadPaddingException | IllegalBlockSizeException " + ex.getMessage());
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

    private static String getStringto16Length(String toPad) {
        if (toPad.length() < 16) {
            String padded = String.format("%16s", toPad).replace(' ', '0');
            return padded;
        } else if (toPad.length() > 16) {
            toPad = toPad.substring(0, 16);
            return toPad;
        }
        logger.warn("Error while padding String");
        return null;
    }

}
