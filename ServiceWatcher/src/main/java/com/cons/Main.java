package com.cons;

import com.cons.services.ServiceOrchestrator;
import com.cons.ui.MainFrame;
import com.cons.ui.ServicesTableModel;

import org.apache.log4j.Logger;

public class Main {
    final static Logger logger = Logger.getLogger(Main.class);
    
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

        if (!conf.isValid()) {
            logger.error("Error reading configuration (" + conf.getError() + ")");
            System.exit(1);
        }

        // Open the UI and initialize it with a custom TableModel
        logger.debug("Instantiate ServiceTableModel");
        ServicesTableModel stm = new ServicesTableModel();
        stm.initFromConfiguration(conf);

        logger.debug("Instantiate ServiceOrchestrator");
        ServiceOrchestrator serviceOrchestrator = new ServiceOrchestrator();
        serviceOrchestrator.setServiceTableModel(stm);
        serviceOrchestrator.setConfiguration(conf);
        //serviceOrchestrator.start();

        if (cla.isNoGUI()) {
            logger.debug("Instatiate CommandLineRunner (-noGUI specified)");
            //Instanitiate command line runner
            clr = new CommandLineRunner(cla.getAutoRefreshTime());
            clr.setServiceOrchestrator(serviceOrchestrator);
            clr.run();
            System.exit(0);
        } else {
            logger.debug("Instatiate MainFrame (GUI)");
            MainFrame mf = new MainFrame();
            logger.debug("Pass ServiceOrchestrator to MainFrame");
            mf.setServiceOrchestrator(serviceOrchestrator);
            logger.debug("Initialize MainFrame from ServiceTableModel");
            mf.initModel(stm);
            mf.setVisible(true);
            try {
                mf.initialization();
            } catch (Exception ex) {
                logger.error("Error initialize MainFrame:" + ex.getMessage());
                ex.printStackTrace();
            }
            mf.setAutoRefreshEnabled();
        }
    }
}
