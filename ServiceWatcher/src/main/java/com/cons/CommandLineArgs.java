package com.cons;


public class CommandLineArgs {

    private String configFile = "config.properties";
    private boolean encrypt = false;
    private boolean noGUI = false;
    private long autoRefreshTime = 0;

    public CommandLineArgs() {
    }

    public void setCommandLineArgs(String[] args) {
        init(args);
    }

    private void init(String[] args) {
        if (args.length == 0) {
            System.out.println("Empty args.");
        } else {
            String tmp;
            for (int i = 0; i < args.length; i++) {
                // -conf
                if (args[i].toLowerCase().startsWith("-conf:")) {
                    tmp = (args[i].split(":")[1]).trim();
                    if (tmp.length() > 0) {
                        setConfigFile(tmp);
                        System.out.println("Custom configuration specified (" + getConfigFile() + ")");
                    } else {
                        System.out.println("Invalid  <config file Name>");
                    }
                } else {
                    System.out.println("Arguments -conf must be followed by a colon (:) and the <config file Name>");
                }
                // -encrypt
                if (args[i].toLowerCase().equals("-encrypt")) {
                    setEncrypt(true);
                    System.out.println("Encrypt passwords: " + getEncrypt());
                }
                // -autorefresh
                if (args[i].toLowerCase().startsWith("-autorefresh:")) {
                    tmp = (args[i].split(":")[1]).trim();
                    if (tmp.length() > 0) {
                        try {
                            setAutoRefreshTime(Long.parseLong(tmp));
                            System.out.println("Auto refresh time is: " + getAutoRefreshTime());
                        } catch (NumberFormatException e) {
                            setAutoRefreshTime(1);
                            System.out.println("Wrong value for auto refresh time, using the default one: " +
                                               getAutoRefreshTime());
                        }
                    } else {
                        setAutoRefreshTime(1);
                        System.out.println("Undefined value for auto refresh time, using the default one: " +
                                           getAutoRefreshTime());
                    }
                } else if (args[i].toLowerCase().equals("-autorefresh")) {
                    setAutoRefreshTime(1);
                } else {
                    System.out.println("Arguments -autorefresh must be followed by a colon (:) and the refreshTime value.");
                }
                // -nogui
                if (args[i].toLowerCase().equals("-nogui")) {
                    setNoGUI(true);
                    System.out.println("GUI is disabled.");
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
}
