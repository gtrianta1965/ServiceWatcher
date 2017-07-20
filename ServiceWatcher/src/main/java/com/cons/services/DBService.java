package com.cons.services;

import com.cons.utils.SWConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        //String value = null;
        Statement stm = null;

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =
                DriverManager.getConnection(serviceParameter.getUrl(), serviceParameter.getUsername(),
                                            serviceParameter.getPassword());
            if (serviceParameter.getQuery().toString() != null) {
                stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(serviceParameter.getQuery());
                //ResultSetMetaData metaData = rs.getMetaData();
                String msgSuccess = "";
                if (rs.next()) {
                    msgSuccess = rs.getString(1);
                    //                    int columnSize = metaData.getColumnCount();
                    //                    for (int i = 1; i <= columnSize; i++) {
                    //                        msgSuccess=msgSuccess+ metaData.getColumnName(i) + ":" + rs.getString(i);
                    //                        System.out.println("Returned Query:" + metaData.getColumnName(i) + ":" + rs.getString(i));
                    //                    }
                    this.setSuccessCall(msgSuccess);
                }

            }
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
        if (stm != null) {
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
