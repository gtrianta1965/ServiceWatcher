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
        String externalConfigFile = getConfigurationFileFromCommandLine(args);

        //Read Configuration (From Property File)
        Configuration conf = new Configuration();
        if (externalConfigFile != null) {
            conf.init(externalConfigFile);
        } else {
            conf.init();
        }
        if (!conf.isValid()) {
            System.out.println("Error reading configuration (" + conf.getError() + ")");
            System.exit(1);
        } else {
            getencryptFromCommandLine(args, conf.getConfigFile());
        }

        // Open the UI and initialize it with a custom TableModel
        ServicesTableModel stm = new ServicesTableModel();
        stm.initFromConfiguration(conf);

        ServiceOrchestrator serviceOrchestrator = new ServiceOrchestrator();
        serviceOrchestrator.setServiceTableModel(stm);
        serviceOrchestrator.setConfiguration(conf);
        //serviceOrchestrator.start();

        if (getNoGuiFromCommandLine(args)) {
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

    private static void getencryptFromCommandLine(String[] args, String configFile) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-encrypt")) {
                System.out.println("Encrypt passwords on " + configFile + " file");
                CryptoUtils.obfuscatePasswordInConfig(configFile);
            }
        }
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
}
