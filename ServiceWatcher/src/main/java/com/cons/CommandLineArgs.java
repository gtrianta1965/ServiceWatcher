package com.cons;

import com.cons.utils.GenericUtils;
import com.cons.utils.SWConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class CommandLineArgs {
    final static Logger logger = Logger.getLogger(CommandLineArgs.class); 

    private String configFile = "config.properties";
    private boolean encrypt = false;
    private boolean noGUI = false;
    private long autoRefreshTime = 0;
    private List<String> allConfigFiles = new ArrayList<String>();

    public CommandLineArgs() {
        
    }


    public void init(String[] args) {
        if (args.length == 0) {
            logger.info("No arguments specified. Default settings will be applied");
        } else {
            String tmp;
            for (int i = 0; i < args.length; i++) {
                // -conf
                if (args[i].toLowerCase().startsWith("-conf")) {
                    if (args[i].split(":").length < 2) {
                        System.out.println("Arguments -conf must be followed by a colon (:) and the <config file Name>");                        
                    } else {
                        //Fix bug with colon in file name (e.g. C:\something\config.properties)
                        //This bug appears only in windows
                        //tmp = args[i].toLowerCase().replaceAll("^-conf:", "");
                        tmp = (args[i].split(":", 2)[1]).trim();
                        if (tmp.length() > 0) {
                            if (getAllConfigFiles().size() == 0) {
                            setConfigFile(tmp);
                            }
                            getAllConfigFiles().add(tmp);
                            logger.info("Custom configuration specified (" + getConfigFile() + ")");
                        } else {
                            logger.error("Invalid  <config file Name>");
                        }
                    }
                }
                // -encrypt
                if (args[i].toLowerCase().equals("-encrypt")) {
                    setEncrypt(true);
                    logger.info("Encrypt passwords: " + getEncrypt());
                }
                // -autorefresh
                if (args[i].toLowerCase().startsWith("-autorefresh")) {
                    long interval = 1;
                    if (args[i].split(":").length == 2) {
                        tmp = (args[i].split(":")[1]).trim(); 
                        try {
                           interval = Long.parseLong(tmp);
                        } catch (NumberFormatException e) {
                                logger.info("Wrong value for auto refresh time (" + tmp + "), using the default one: " +
                                            interval);
                        }
                    }                     
                    setAutoRefreshTime(interval);
                }                    
                // -nogui
                if (args[i].toLowerCase().equals("-nogui")) {
                    setNoGUI(true);
                    logger.info("-nogui specified, GUI is disabled.");
                }
                //-help
                if (args[i].toLowerCase().equals("-help")) {
                    help();
                }
                //-version
                if (args[i].toLowerCase().equals("-version")) {
                    System.out.println(SWConstants.PROGRAM_NAME + " " + "Version: " + SWConstants.PROGRAM_VERSION);
                    System.exit(0); //Exit after displaying the version
            }
        }
    }
        debugArguments();
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
        String ANSI_RED_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";

        try {
            InputStream in = getClass().getResourceAsStream("/images/help.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in)) ;
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (GenericUtils.isUnix()) {
                line = line.replaceAll("<b>","\u001B[1m" );
                line = line.replaceAll("</b>","\u001B[0m" );
                    line = line.replaceAll("<red>", ANSI_RED);
                    line = line.replaceAll("</red>", ANSI_RED_RESET);
                } else if (GenericUtils.isWindows()) {
                    line = line.replaceAll("<b>", "").trim();
                    line = line.replaceAll("</b>", "").trim();
                    line = line.replaceAll("<red>", "").trim();
                    line = line.replaceAll("</red>", "").trim();
                }
                System.out.println(line);
            }
            System.exit(0);
        } catch (IOException e) {
            System.out.println("File does not exist.");
        } catch (Exception e) {
            System.out.println("Help is not available.");
        }
    }

    public void setAdditionalConfigFiles(List<String> allConfigFiles) {
        this.allConfigFiles = allConfigFiles;
}

    public List<String> getAllConfigFiles() {
        return allConfigFiles;
    }
    
    private void debugArguments() {
        logger.debug("Main configFile=" + configFile);
        for (String s : getAllConfigFiles()) {
            logger.debug("getAllConfigFiles:" + s);
        }
    }
}
