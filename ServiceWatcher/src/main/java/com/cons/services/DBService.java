package com.cons.services;

import com.cons.utils.SWConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DBService extends Service {
    static final Logger logger = Logger.getLogger(DBService.class);

    public DBService() {
    }

    public DBService(ServiceParameter sp) {
        super(sp);
    }


    @Override
    public void service() {

        Connection conn = null;
        this.setSuccessfulCall(true);
        Statement stm = null;
        ResultSet rs = null;

        try {
            logger.debug("Load driver.");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            logger.debug("Initialize Connection.");
            conn =
                DriverManager.getConnection(serviceParameter.getUrl(), serviceParameter.getUsername(),
                                            serviceParameter.getPassword());
            if (serviceParameter.getQuery() != null && serviceParameter.getQuery().length() != 0) {
                logger.debug("Execute query:" + serviceParameter.getQuery());
                stm = conn.createStatement();
                rs = stm.executeQuery(serviceParameter.getQuery());
                String msgSuccess = "";
                if (rs.next()) {
                    logger.debug("Query returned at least one row (" + rs.getString(1)  + ")");
                    msgSuccess = rs.getString(1);
                    this.setSuccessCall(msgSuccess);
                }

            }
        } catch (ClassNotFoundException ex) {
            logger.debug("ClassNotFoundException:" + ex.getMessage());
            this.setErrorCall(SWConstants.SERVICE_DB_ERROR_ORACLE_CLASS_MSG + ":" + ex.getMessage());
            this.setSuccessfulCall(false);
        } catch (SQLException ex) {
            logger.debug("SQLException:" + ex.getMessage());
            logger.debug("Query Parameter:" + serviceParameter.getQuery());
            this.setErrorCall(SWConstants.SERVICE_DB_ERROR_ORACLE_SQLEXCEPTION_MSG + ":" + ex.getMessage());
            this.setSuccessfulCall(false);
            getServiceParameter().setContext(getErrorCall());
        } catch (Exception ex) {
            logger.debug("Exception:" + ex.getMessage());
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
