package com.cons;

import com.cons.services.HTTPService;
import com.cons.services.ServiceFactory;
import com.cons.services.ServiceOrchestrator;
import com.cons.ui.MainFrame;
import com.cons.ui.ServicesTableModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Hello world!
 *
 */
public class Main {
    public static void main(String[] args) {
        boolean externalPropertiesFile = false;
        String externalConfigFile = null;
        boolean foundExpectedArg = false;
        if (args != null || args.length <= 0) {
            int i = 0;
            for (String s : args) {

                if (s.equalsIgnoreCase("-conf")) {
                    System.out.println("Configuration Argument:" + s);
                    externalPropertiesFile = true;
                    foundExpectedArg = true;
                    if (args.length >= i + 1) {
                        externalConfigFile = args[i + 1];
                        System.out.println(externalConfigFile);
                    } else {
                        System.out.println("Arguments -conf must be followed By <config file Name>");
                    }
                    i++;
                } else {
                    foundExpectedArg = false;
                }
            }
            if (!foundExpectedArg) {
                System.out.println("wrong Argument found expected  something like -conf <config file Name>");
            }
        }
        //Read Configuration (From Property File)
        Configuration conf = new Configuration();
        if (externalPropertiesFile) {
            conf.init(externalConfigFile);
        } else {
            conf.init();
        }
        if (!conf.isValid()) {
            System.out.println("Error reading configuration (" + conf.getError() + ")");
            System.exit(1);
        }

        // Open the UI and initialize it with a custom TableModel
        ServicesTableModel stm = new ServicesTableModel();
        stm.initFromConfiguration(conf);

        ServiceOrchestrator serviceOrchestrator = new ServiceOrchestrator();
        serviceOrchestrator.setServiceTableModel(stm);
        serviceOrchestrator.setConfiguration(conf);
        //serviceOrchestrator.start();

        MainFrame mf = new MainFrame();
        mf.initModel(stm);
        mf.setServiceOrchestrator(serviceOrchestrator);
        mf.setVisible(true);


    }


}
