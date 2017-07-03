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


        serviceParameter.getType();
        String currentUrl=serviceParameter.getUrl();
        
            try {

                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                conn = DriverManager.getConnection(currentUrl);
                
            } catch (ClassNotFoundException ex) {
                this.setErrorCall(ex.getMessage());
                this.setSuccessfulCall(false);
            } catch (IllegalAccessException ex) {
                this.setErrorCall(ex.getMessage());
                this.setSuccessfulCall(false);
            } catch (InstantiationException ex) {
                this.setSuccessfulCall(false);
                this.setErrorCall(ex.getMessage());
            } catch (SQLException ex) {
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
