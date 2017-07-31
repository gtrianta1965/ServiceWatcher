package com.cons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class CommandLineArgs {

    private String configFile = "config.properties";
    private boolean encrypt = false;
    private boolean noGUI = false;
    private long autoRefreshTime = 0;


    public CommandLineArgs() {
        
    }


    public void init(String[] args) {
        if (args.length == 0) {
            System.out.println("Empty args.");
        } else {
            String tmp;
            for (int i = 0; i < args.length; i++) {
                // -conf
                if (args[i].toLowerCase().startsWith("-conf")) {
                    if (args[i].split(":").length != 2) {
                        System.out.println("Arguments -conf must be followed by a colon (:) and the <config file Name>");                        
                    } else {
                        tmp = (args[i].split(":")[1]).trim();
                        if (tmp.length() > 0) {
                            setConfigFile(tmp);
                            System.out.println("Custom configuration specified (" + getConfigFile() + ")");
                        } else {
                            System.out.println("Invalid  <config file Name>");
                        }
                    }
                }
                // -encrypt
                if (args[i].toLowerCase().equals("-encrypt")) {
                    setEncrypt(true);
                    System.out.println("Encrypt passwords: " + getEncrypt());
                }
                // -autorefresh
                if (args[i].toLowerCase().startsWith("-autorefresh")) {
                    long interval = 1;
                    if (args[i].split(":").length == 2) {
                        tmp = (args[i].split(":")[1]).trim(); 
                        try {
                           interval = Long.parseLong(tmp);
                        } catch (NumberFormatException e) {
                            System.out.println("Wrong value for auto refresh time (" + tmp + "), using the default one: " +
                                  interval);
                        }
                    }                     
                    setAutoRefreshTime(interval);
                }                    
                // -nogui
                if (args[i].toLowerCase().equals("-nogui")) {
                    setNoGUI(true);
                    System.out.println("GUI is disabled.");
                }
                //-help
                if (args[i].toLowerCase().equals("-help")) {
                    help();
                }
            }
        }
    }


    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public boolean getEncrypt() {
        return encrypt;
    }

    public void setNoGUI(boolean noGUI) {
        this.noGUI = noGUI;
    }

    public boolean isNoGUI() {
        return noGUI;
    }

    public void setAutoRefreshTime(long autoRefreshTime) {
        this.autoRefreshTime = autoRefreshTime;
    }

    public long getAutoRefreshTime() {
        return autoRefreshTime;
    }


    public void help() {

        try {
            InputStream in = getClass().getResourceAsStream("/src/images/help.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in)) ;
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.exit(0);
        } catch (IOException e) {
            System.out.println("file does not exist.");
        } catch (Exception e) {
            System.out.println("Help is not available.");
        }
    }
}
