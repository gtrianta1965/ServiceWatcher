package com.cons;

import com.cons.services.ServiceOrchestrator;
import com.cons.ui.MainFrame;
import com.cons.ui.ServicesTableModel;
import com.cons.utils.GenericUtils;

public class Main {
    public static void main(String[] args) {
        

        CommandLineRunner clr = null;
        Configuration conf = null;

        //Read,parse and store command line options
        CommandLineArgs cla = new CommandLineArgs();
        cla.init(args);
        
        //Setup and initialize of configuration (Read property file)
        conf = new Configuration();
        conf.setCmdArguments(cla); //Pass command line arguments to configuration it might be helpful
        conf.init(cla.getConfigFile()); 
        if (!conf.isValid()){
            System.out.println("Error reading configuration (" + conf.getError() + ")");
            System.exit(1);
        }

        // Open the UI and initialize it with a custom TableModel
        ServicesTableModel stm = new ServicesTableModel();
        stm.initFromConfiguration(conf);

        ServiceOrchestrator serviceOrchestrator = new ServiceOrchestrator();
        serviceOrchestrator.setServiceTableModel(stm);
        serviceOrchestrator.setConfiguration(conf);

        if (cla.isNoGUI()) {
            //Instanitiate command line runner
            clr = new CommandLineRunner(cla.getAutoRefreshTime());
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
            mf.setAutoRefreshEnabled();
        }
    }
}