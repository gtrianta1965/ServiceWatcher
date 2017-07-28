package com.cons;


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
        String NoGUI = "(-nogui) : runs the Service watcher without GUI(user interface\n ";
        String AutoRefresh =
            "(-autorefresh : X) : Starts the auto refresh. X sets the interval between the autorefreshes. \nIf you do not select an " +
            "interval number the programme starts with a default interval.\n" +
            "Also arguments -autorefresh must be followed by a colon (:) and the refreshTime value.\n";
        String Encrypt = "(-encrypt) : Obfuscates the passwords of an unencrypted file\n";
        String conf = "(-conf :) : loads a castum configuration file\n";
        System.out.println("\n\n\n" + NoGUI + "\n" + AutoRefresh + "\n" + Encrypt + "\n" + conf + "\n");
        System.exit(0);
    }
}
