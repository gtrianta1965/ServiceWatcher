package com.cons;

import com.cons.services.ServiceOrchestrator;
import com.cons.ui.MainFrame;
import com.cons.ui.ServicesTableModel;
import com.cons.utils.CryptoUtils;

public class Main {
    public static void main(String[] args) {
        CommandLineRunner clr = null;
        Configuration conf = new Configuration();
        
        if (args.length != 0) {
            getCommandLineArgs(args, conf);
            clr = new CommandLineRunner(conf.getAutoRefresh());
        }
        
        conf.init();

        // Open the UI and initialize it with a custom TableModel
        ServicesTableModel stm = new ServicesTableModel();
        stm.initFromConfiguration(conf);

        ServiceOrchestrator serviceOrchestrator = new ServiceOrchestrator();
        serviceOrchestrator.setServiceTableModel(stm);
        serviceOrchestrator.setConfiguration(conf);

        if (conf.getNoGUI() && conf.getAutoRefresh() == 0) {
            clr.setServiceOrchestrator(serviceOrchestrator);
            clr.run();
        } else if (conf.getNoGUI() && conf.getAutoRefresh() != 0) {
            clr.setServiceOrchestrator(serviceOrchestrator);
            clr.autorefresh();
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
            mf.setAutoRefreshEnabled();
        }
    }
    
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
