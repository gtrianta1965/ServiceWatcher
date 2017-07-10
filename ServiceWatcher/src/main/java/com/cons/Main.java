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
    
    private static  String getConfigurationFileFromCommandLine(String[] args) {
        String configFile = null;
        for (int i = 0 ; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-conf")) {
                //Found switch, check next argument
                if (i + 1 < args.length) {
                    configFile = args[i+1];
                    System.out.println("Custom configuration specified (" + configFile + ")");
                } else {
                    System.out.println("Arguments -conf must be followed By <config file Name>");
                }
                
            }
        }
        
        return configFile;
    }


}
