package com.cons.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;


public class SHELLService extends Service {

    final static Logger logger = Logger.getLogger(SHELLService.class);


    public SHELLService() {
        super();
    }

    public SHELLService(ServiceParameter serviceParameter) {
        super(serviceParameter);
    }


    @Override
    public void service() {

        String out = executeCommand();
        System.out.println(out);
    }

    private String executeCommand() {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec("echo $USER");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        return output.toString();

    }

    public static void main(String[] args) {
        // Run command and wait till it's done
        Process p;
        try {
            p = Runtime.getRuntime().exec("ping -n 3 www.google.com");

            p.waitFor();

            // Grab output and print to display
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
    }
}
