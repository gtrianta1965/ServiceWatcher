package com.cons.utils;

import java.io.File;

import java.nio.file.Files;

import java.nio.file.Paths;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ClientLogger {
    public ClientLogger() {
        super();
    }
    private final Logger logger = Logger.getLogger(ClientLogger.class.getName());
    private FileHandler fh = null;        

    private void init(String logFileName) {
        LoggerConfig(logFileName);
    }

    public Logger LoggerConfig(String logFileName) {
        //just to make our log file nicer :)
        SimpleDateFormat format = new SimpleDateFormat("M-d-yyyy");
        try {
            String logsDirectoryFolder = "logs";
            Files.createDirectories(Paths.get(logsDirectoryFolder));
            SimpleFormatter formatter = new SimpleFormatter();
            fh =
                new FileHandler("./" + logsDirectoryFolder + "/" + logFileName +
                                format.format(Calendar.getInstance().getTime()) + ".log", 1024 * 1024, 10, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
        return logger;
    }
}
