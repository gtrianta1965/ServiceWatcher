package com.cons.services;

import com.cons.ServiceParameter;

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
    public void run() {
        ServiceParameter sp = new ServiceParameter();
        service(sp);
    }

    @Override
    public void service(ServiceParameter sp) {
        // TODO Implement this method
        Connection conn = null;

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //This Fails Logon
        String currentUrl1 =
            "jdbc:oracle:thin:@(DESCRIPTION=(LOAD_BALANCE=yes)(USER=epresbkp2)(PASSWORD=Manager1)(ADDRESS=(PROTOCOL=TCP)(HOST=shststdb1-vip.idika.gr)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=shststdb2-vip.idika.gr)(PORT=1521))(CONNECT_DATA=(SERVER=SHARED)(SERVICE_NAME=tstdb_taf)))";
        //Connected
        String currentUrl2 = "jdbc:oracle:thin:epresbkp2/Manager1@shstst-scan.idika.gr:1521/tstdb_taf";
        String currentUrl4 = "jdbc:oracle:thin:hr/oracle@192.168.6.67:1521/orcl";
        String db_username = "epresbkp2";
        String db_password = "Manager1";
        sp.getType();
        String currentUrl=sp.getUrl();
        

        if (sp.getType().equals("DB")) {
            try {

                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                conn = DriverManager.getConnection(currentUrl);
                
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                System.err.println(ex.getMessage());
                this.setErrorCall(ex.getMessage());
                this.setSuccessfulCall(false);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.err.println(ex.getMessage());
                this.setErrorCall(ex.getMessage());
                this.setSuccessfulCall(false);
            } catch (InstantiationException ex) {
                ex.printStackTrace();
                this.setSuccessfulCall(false);
                System.err.println(ex.getMessage());
                this.setErrorCall(ex.getMessage());
                this.setSuccessfulCall(false);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
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


        } else {
            System.out.println("This is not Database Type in properties ");
            this.setSuccessfulCall(false);
        }
    }

    public static void main(String args[]) {

        DBService dbs = new DBService();
        dbs.run();

    }

}
