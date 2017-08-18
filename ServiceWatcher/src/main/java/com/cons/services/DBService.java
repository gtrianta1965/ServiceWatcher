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
        ResultSet rs = null;

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =
                DriverManager.getConnection(serviceParameter.getUrl(), serviceParameter.getUsername(),
                                            serviceParameter.getPassword());
            if (serviceParameter.getQuery() != null && serviceParameter.getQuery().length() != 0) {
                stm = conn.createStatement();
                rs = stm.executeQuery(serviceParameter.getQuery());
                //ResultSetMetaData metaData = rs.getMetaData();
                String msgSuccess = "";
                if (rs.next()) {
                    msgSuccess = rs.getString(1);
                    this.setSuccessCall(msgSuccess);
                }

            }
        } catch (ClassNotFoundException ex) {
            this.setErrorCall(SWConstants.SERVICE_DB_ERROR_ORACLE_CLASS_MSG + ":" + ex.getMessage());
            this.setSuccessfulCall(false);
        } catch (SQLException ex) {
            this.setErrorCall(SWConstants.SERVICE_DB_ERROR_ORACLE_SQLEXCEPTION_MSG + ":" + ex.getMessage());
            this.setSuccessfulCall(false);
            getServiceParameter().setContext("Wrong Query=" + serviceParameter.getQuery()+" - "+getErrorCall());
            System.out.println("Wrong Query= " + serviceParameter.getQuery());
        } catch (Exception ex) {
            ex.printStackTrace();
            this.setErrorCall(SWConstants.GENERIC_EXCEPTION_MSG + ":" + ex.getMessage());
            this.setSuccessfulCall(false);
        } finally {
                if (rs != null) try {rs.close();} catch (Exception e) {};
                if (stm != null) try {stm.close();} catch (Exception e) {};
                if (conn != null) try {conn.close();} catch (Exception e) {};
        }
    }

    public static void main(String args[]) {

        DBService dbs = new DBService();
        dbs.run();

    }

}
