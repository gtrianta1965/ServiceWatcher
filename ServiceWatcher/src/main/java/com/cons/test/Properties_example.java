package com.cons.test;

import com.cons.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Enumeration;
import java.util.Properties;


public class Properties_example {

    private String url;
    private String description;
    private String type;
    private String group;
    private String searchString;
    private static String configFileName = "config.properties";
    public Enumeration e = null;

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
            int i = 1;
            boolean b = true;
            while (b) {
                System.out.println("url." + i+" "+prop.getProperty("url." + i));
                System.out.println("description." + i+" "+prop.getProperty("description." + i));
                System.out.println("type." + i+" "+prop.getProperty("type." + i));
                System.out.println("searchString." + i+" "+prop.getProperty("searchString." + i));
                System.out.println("group." + i+" "+prop.getProperty("group." + i));
                i++;
                b = prop.containsKey("url." + i);
                System.out.println("b:"+b) ;
               
            }
            //            System.out.println(prop.getProperty("url.1"));
            //            System.out.println(prop.getProperty("description.1"));
            //            System.out.println(prop.getProperty("type.1"));
            //            System.out.println(prop.getProperty("searchString.1"));
            //            System.out.println(prop.getProperty("group.1"));


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

    private void loadPropertiesEnumExample() {
        //get properties from config.properties
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            FileInputStream outpout = new FileInputStream(configFileName);

            //Set values
            prop.load(outpout);
            e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                System.out.println(key + " :: " + prop.getProperty(key));
            }

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

    public static void main(String[] args) {
        //System.out.println( "Hello World!" );
        //Properties props = System.getProperties();
        //   Enumeration e = props.propertyNames();
        Properties_example runme = new Properties_example();
        //runme.loadPropertiesEnumExample();
        runme.loadProperties();
    }


}
