package com.cons.services;

import com.cons.utils.SWConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBService extends Service {

    public DBService() {
    }

    public DBService(ServiceParameter sp) {
        super(sp);
    }


    @Override
    public void service() {

        Connection conn = null;
        this.setSuccessfulCall(true);

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(serviceParameter.getUrl(), serviceParameter.getUsername(), serviceParameter.getPassword());

        } catch (ClassNotFoundException ex) {
            this.setErrorCall(SWConstants.SERVICE_DB_ERROR_ORACLE_CLASS_MSG + ex.getMessage());
            this.setSuccessfulCall(false);
        } catch (SQLException ex) {
            this.setErrorCall(SWConstants.SERVICE_DB_ERROR_ORACLE_SQLEXCEPTION_MSG + ex.getMessage());
            this.setSuccessfulCall(false);
        } catch (Exception ex) {
            this.setErrorCall(SWConstants.GENERIC_EXCEPTION_MSG + ex.getMessage());
            this.setSuccessfulCall(false);
        }

        if (conn != null) {
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
