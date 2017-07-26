package com.cons;

import com.cons.services.ServiceOrchestrator;
import com.cons.ui.MainFrame;
import com.cons.ui.ServicesTableModel;
import com.cons.utils.CryptoUtils;


/**
 * Hello world!
 *
 */
public class Main {
    public static void main(String[] args) {
        Configuration conf = new Configuration();
        
        if (args.length != 0) {
            getCommandLineArgs(args, conf);
        }
        
        conf.init();

        // Open the UI and initialize it with a custom TableModel
        ServicesTableModel stm = new ServicesTableModel();
        stm.initFromConfiguration(conf);

        ServiceOrchestrator serviceOrchestrator = new ServiceOrchestrator();
        serviceOrchestrator.setServiceTableModel(stm);
        serviceOrchestrator.setConfiguration(conf);

        if (conf.getNoGUI()) {
            CommandLineRunner clr = new CommandLineRunner();
            clr.setServiceOrchestrator(serviceOrchestrator);
            clr.run();
        } else {
            MainFrame mf = new MainFrame();
            mf.setServiceOrchestrator(serviceOrchestrator);
            mf.initModel(stm);
            mf.setVisible(true);
            try {
                mf.initialization();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
/*
    private static String getConfigurationFileFromCommandLine(String[] args) {
        String configFile = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-conf")) {
                //Found switch, check next argument
                if (i + 1 < args.length) {
                    configFile = args[i + 1];
                    System.out.println("Custom configuration specified (" + configFile + ")");
                } else {
                    System.out.println("Arguments -conf must be followed By <config file Name>");
                }
            }
        }

        return configFile;
    }

    private static boolean getencryptFromCommandLine(String[] args, String configFile) {
        boolean encrypt = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-encrypt")) {
                System.out.println("Encrypt passwords on " + configFile + " file");
                encrypt = true;
            }
        }
        return encrypt;
    }

    private static boolean getNoGuiFromCommandLine(String[] args) {
        boolean nogui = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-nogui")) {
                nogui = true;
            }
        }
        System.out.println("The GUI is " + (nogui ? "disabled" : "enabled"));
        return nogui;
    }

    private static long getAutoRefreshFromCommandLine(String[] args) {
        long refreshTime = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-autorefresh")) {
                if (i + 1 < args.length) {
                    try {
                        refreshTime = Long.parseLong(args[i + 1]);
                        System.out.println("Auto refresh time is: " + refreshTime);
                    } catch (NumberFormatException e) {
                        refreshTime = 1;
                        System.out.println("Wrong value for auto refresh time, using the default one: " + refreshTime);
                    }
                } else {
                    refreshTime = 1;
                    System.out.println("Undefined value for auto refresh time, using the default one: 1");
                }
            }
        }
        return refreshTime;
    }
*/
    private static void getCommandLineArgs(String[] args, Configuration conf) {
        // for each argument
        for (int i = 0; i < args.length; i++) {
            // -conf
            if (args[i].equalsIgnoreCase("-conf")) {
                //Found switch, check next argument
                if (i + 1 < args.length) {
                    conf.setConfigFile(args[i + 1]);
                    System.out.println("Custom configuration specified (" + conf.getConfigFile() + ")");
                } else {
                    System.out.println("Arguments -conf must be followed By <config file Name>");
                }
            }
            // -encrypt
            if (args[i].equalsIgnoreCase("-encrypt")) {
                System.out.println("Encrypt passwords on " + conf.getConfigFile() + " file");
                conf.setEncrypt(true);
            }
            // -autorefresh
            if (args[i].equalsIgnoreCase("-autorefresh")) {
                if (i + 1 < args.length) {
                    try {
                        conf.setAutoRefresh(Long.parseLong(args[i + 1]));
                        System.out.println("Auto refresh time is: " + conf.getAutoRefresh());
                    } catch (NumberFormatException e) {
                        conf.setAutoRefresh(1);
                        System.out.println("Wrong value for auto refresh time, using the default one: " + conf.getAutoRefresh());
                    }
                } else {
                    conf.setAutoRefresh(1);
                    System.out.println("Undefined value for auto refresh time, using the default one: " + conf.getAutoRefresh());
                }
            }
            // -nogui
            if (args[i].equalsIgnoreCase("-nogui")) {
                conf.setNoGUI(true);
            }
        }
    }
}
