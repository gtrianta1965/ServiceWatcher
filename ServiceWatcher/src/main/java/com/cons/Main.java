package com.cons;

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

 
        //Read Configuration (From Property File)
        Configuration conf = new Configuration();
        conf.init();
        if (!conf.isValid()){
            System.out.println("Error reading configuration (" + conf.getError() + ")");
            System.exit(1);
        }
        
        // Open the UI and initialize it with a custom TableModel
        ServicesTableModel sdm = new ServicesTableModel();
        sdm.initFromConfiguration(conf);
        MainFrame mf = new MainFrame();
        mf.initModel(sdm);
        mf.setVisible(true);
        
        //Start Thread Pooling with services
        ExecutorService executor = Executors.newFixedThreadPool(5);
        int threads = 10;
        
        for (int i = 0; i < threads; i++) {
            Runnable worker = new HTTPService();
            executor.execute(worker);
        }
        

        
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        
        System.out.println("Jobs Submitted");
        sdm.changeModel();
        
        
        System.out.println("Finished all threads");
        
    }
}
