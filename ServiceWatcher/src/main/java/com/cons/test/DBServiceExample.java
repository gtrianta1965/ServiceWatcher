package com.cons.test;

import com.cons.services.ServiceParameter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBServiceExample {
    private static String ErrorCall = "";

    public DBServiceExample() {
        super();
    }

    public void service() {
        // TODO Implement this method

        String currentUrl1 =
            "jdbc:oracle:thin:@(DESCRIPTION=(LOAD_BALANCE=yes)(USER=epresbkp2)(PASSWORD=Manager1)(ADDRESS=(PROTOCOL=TCP)(HOST=shststdb1-vip.idika.gr)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=shststdb2-vip.idika.gr)(PORT=1521))(CONNECT_DATA=(SERVER=SHARED)(SERVICE_NAME=tstdb_taf)))";
        String currentUrl2 = "jdbc:oracle:thin:epresbkp2/Manager1@shstst-scan.idika.gr:1521/tstdb_taf";
        String currentUrl4 = "jdbc:oracle:thin:hr/oracle@192.168.6.67:1521/orcl";

        String currentUrl3="jdbc:oracle:thin:hr/oracle@192.168.6.67:1521/orcl";
        String db_driver = "com.mysql.jdbc.Driver";
        String db_username = "epresbkp2";
        String db_password = "Manager1";

        try {
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            
            //String url = "jdbc:mysql://HOST/DATABASE";
            Connection conn = DriverManager.getConnection(currentUrl1);
            //doTests();
            //conn.close();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();

            System.err.println(ex.getMessage());
            
            this.ErrorCall="Error";
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
            this.ErrorCall="Error";
        } catch (InstantiationException ex) {
            ex.printStackTrace();

            System.err.println(ex.getMessage());
            ex.printStackTrace();

            this.ErrorCall="Error";
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();

            this.ErrorCall="Error";
        }
        
        if ((ErrorCall.isEmpty())|| ErrorCall.equalsIgnoreCase("")) {
            System.out.println("Connected to database");
        }


    } //end procedure

    public static void main(String[] args) {
        DBServiceExample dBServiceTest = new DBServiceExample();
        dBServiceTest.service();
    }


}
