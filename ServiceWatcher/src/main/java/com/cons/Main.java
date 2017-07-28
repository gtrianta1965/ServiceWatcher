package com.cons;

import com.cons.services.ServiceOrchestrator;
import com.cons.ui.MainFrame;
import com.cons.ui.ServicesTableModel;

public class Main {
    public static void main(String[] args) {
        CommandLineRunner clr = null;
        CommandLineRunner cmdl= new CommandLineRunner();
        Configuration conf = new Configuration();

        CommandLineArgs cla = new CommandLineArgs();
        cla.setCommandLineArgs(args);
        conf.setCmdArguments(cla);
        conf.init();

        // TODO: CommandLineRunner instantiate

        // Open the UI and initialize it with a custom TableModel
        ServicesTableModel stm = new ServicesTableModel();
        stm.initFromConfiguration(conf);

        ServiceOrchestrator serviceOrchestrator = new ServiceOrchestrator();
        serviceOrchestrator.setServiceTableModel(stm);
        serviceOrchestrator.setConfiguration(conf);

        if (cla.isNoGUI() && cla.getAutoRefreshTime() == 0) {
            clr = new CommandLineRunner();
            clr.setServiceOrchestrator(serviceOrchestrator);
            clr.run();
        } else if (cla.isNoGUI() && cla.getAutoRefreshTime() != 0) {
            clr = new CommandLineRunner(cla.getAutoRefreshTime());
            clr.setServiceOrchestrator(serviceOrchestrator);
            clr.autorefresh();
        } else if(cla.setHelp()){
            cmdl.help();
        }else{
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