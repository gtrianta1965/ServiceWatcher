package com.cons.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Properties;


public class Properties_example {

    private String url;
    private String description;
    private String type;
    private String group;
    private String searchString;
    private static String configFileName = "config.properties";


    public Properties_example() {
        super();
    }

    private void loadProperties() {
        //get properties from config.properties
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            FileInputStream outpout = new FileInputStream(configFileName);

            //Set values
            prop.load(outpout);

            System.out.println(prop.getProperty("url"));
            System.out.println(prop.getProperty("dbuser"));
            System.out.println(prop.getProperty("dbpassword"));


        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
