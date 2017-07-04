package com.cons.utils;

import java.net.ProtocolException;

public class SWConstants {
    
    public final static String LOG_FILE_NAME_CONFIQURATION = "configuration";
    public final static String LOG_FILE_NAME_SERVICE = "service";
    public final static String UNSUCCESSFUL_RESPONSE_MSG = "url with response message:";
    public final static String MALLFORMED_URL_EXCEPTION_MSG = "Malformed URL:";
    public final static String PROTOCOL_EXCEPTION_MSG = "Protocol exception:";
    public final static String GENERIC_EXCEPTION_MSG = "Generic Exception:";
    public final static String SEARCH_STRING_NOT_FOUND_MSG = "Search String not found in response";
    public final static String URL_RESPONSE_ERROR_MSG = "url with response message:";
    public final static String URL_HOST_ERROR_MSG = "The host was not found";
    public final static String SERVICE_DB_ERROR_ORACLE_CLASS_MSG = "Oracle Class driver Was not Found";
    public final static String SERVICE_DB_ERROR_ORACLE_SQLEXCEPTION_MSG = "SQL Connection Error";


        
    //Service thread status
    public final static String SERVICE_SUBMITTED = "SUBMITTED";
    public final static String SERVICE_SUCCESS = "SUCCESS";
    public final static String SERVICE_FAILED = "FAILED";
    public final static String SERVICE_RUNNING = "RUNNING";
    
    //JTable Column Indexes (zero based)

    public final static int TABLE_ID_INDEX = 0;    
    public final static int TABLE_URL_INDEX = 1;
    public final static int TABLE_DESCRIPTION_INDEX = 2;
    public final static int TABLE_TYPE_INDEX = 3;
    public final static int TABLE_GROUP_INDEX = 4;        
    public final static int TABLE_STATUS_INDEX = 5;
    
    //Number of columns in the table
    public final static int TABLE_NUMBER_OF_COLUMNS = 6;
    
    

}
