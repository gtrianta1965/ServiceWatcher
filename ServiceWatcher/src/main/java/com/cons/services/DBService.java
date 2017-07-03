package com.cons.services;

import com.cons.services.ServiceParameter;

import com.cons.test.DBServiceExample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Driver;

public class DBService extends Service {
    public DBService() {
    }

    public DBService(ServiceParameter sp) {
        super(sp);
    }


    @Override
    public void service() {
        // TODO Implement this method
        Connection conn = null;

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
            this.setErrorCall(e.getMessage());
        }

        serviceParameter.getType();
        String currentUrl=serviceParameter.getUrl();
        
            try {

                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                conn = DriverManager.getConnection(currentUrl);
                
            } catch (ClassNotFoundException ex) {
                //ex.printStackTrace();
                System.err.println(ex.getMessage());
                this.setErrorCall(ex.getMessage());
                this.setSuccessfulCall(false);
            } catch (IllegalAccessException ex) {
                //ex.printStackTrace();
                System.err.println(ex.getMessage());
                this.setErrorCall(ex.getMessage());
                this.setSuccessfulCall(false);
            } catch (InstantiationException ex) {
                //ex.printStackTrace();
                this.setSuccessfulCall(false);
                System.err.println(ex.getMessage());
                this.setErrorCall(ex.getMessage());
                this.setSuccessfulCall(false);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                //ex.printStackTrace();
                this.setErrorCall(ex.getMessage());
                this.setSuccessfulCall(false);
            }

            if ((this.getErrorCall() == null || this.getErrorCall().equalsIgnoreCase("") ||
                 this.getErrorCall().isEmpty())) {
                System.out.println("Connected to database URL::" + currentUrl);
                this.setSuccessfulCall(true);
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }

    }

    public static void main(String args[]) {

        DBService dbs = new DBService();
        dbs.run();

    }

}
