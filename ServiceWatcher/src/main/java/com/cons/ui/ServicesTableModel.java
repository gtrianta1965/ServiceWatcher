package com.cons.ui;

import com.cons.Configuration;
import com.cons.services.ServiceParameter;
import com.cons.utils.GenericUtils;
import com.cons.utils.SWConstants;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ServicesTableModel extends AbstractTableModel {
    @SuppressWarnings("compatibility:2206726703879486991")
    private static final long serialVersionUID = 1L;
    private boolean DEBUG = false;

    private String[] columnNames = { "ID", "URL", "Description", "Type", "Group" , "Status","Context", "R","Password"};

    private Object[][] data;
    
    List<ServiceParameter> serviceParameters;
    private ServiceParameter sp;
    
    
    /*
     * Row is zero indexed!!!!
     */
    public void setStatus(int row, String status) {
        data[row][SWConstants.TABLE_STATUS_INDEX] = status;
        data[row][SWConstants.TABLE_RETRIES_INDEX] = serviceParameters.get(row).getActualRetries() + "/" +
                                                     serviceParameters.get(row).getRetries();
        if(serviceParameters.get(row).getContext()!=null){
            data[row][SWConstants.tABLE_CONTEXT_INDEX]=serviceParameters.get(row).getContext();   
        }  
        fireTableDataChanged();
    }
    
    public void initFromConfiguration(Configuration configuration) {
        int rows = configuration.getServiceParameters().size();
        
        serviceParameters = configuration.getServiceParameters();
        data = new Object[rows][SWConstants.TABLE_NUMBER_OF_COLUMNS];
        
        
        for (int i=0 ; i< rows ; i++) {
            sp = (ServiceParameter)serviceParameters.get(i);
            data[i][SWConstants.TABLE_ID_INDEX] = sp.getId();
            data[i][SWConstants.TABLE_URL_INDEX] = sp.getUrl();
            data[i][SWConstants.TABLE_DESCRIPTION_INDEX] = sp.getDescription();
            data[i][SWConstants.TABLE_TYPE_INDEX] = sp.getType();
            data[i][SWConstants.TABLE_GROUP_INDEX] = sp.getGroup();
            data[i][SWConstants.TABLE_STATUS_INDEX] = new String();
            data[i][SWConstants.tABLE_CONTEXT_INDEX] = sp.getContext()== null?"":sp.getContext(); 
            data[i][SWConstants.TABLE_RETRIES_INDEX] = sp.getActualRetries() + "/" + sp.getRetries();
            
            String password_ast = "";
            for (int j = 0; j < GenericUtils.nvl(sp.getPassword(), "")
                                            .toString()
                                            .length(); j++) {
                password_ast += "*";
            }
            data[i][SWConstants.TABLE_PASSWORD_INDEX] = password_ast;
        }    
        
        fireTableDataChanged();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/ editor for
     * each cell. If we didn't implement this method, then the last column
     * would contain text ("true"/"false"), rather than a check box.
     */
    
    public Class getColumnClass(int c) {
        if (c != SWConstants.TABLE_RETRIES_INDEX && c!= SWConstants.TABLE_ID_INDEX) {
           return getValueAt(0, c).getClass();
        } else {
            //This is important because the class cannot be determined automatically (the value is primitive type int)
            return String.class;
        }
        
    }
    
    /*
     * Don't need to implement this method unless your table's editable.
     */
    public boolean isCellEditable(int row,
                                  int col) {
        //Nothing is editable
        if (col == SWConstants.TABLE_PASSWORD_INDEX &&
            ((data[row][SWConstants.TABLE_TYPE_INDEX].equals("DB")) ||
             (data[row][SWConstants.TABLE_TYPE_INDEX].equals("LDAP")))) {
            return true;            
            }
        return false;
    }

    /*
     * Don't need to implement this method unless your table's data can
     * change.
     */
    public void setValueAt(Object value, int row, int col) {
        if (DEBUG) {
            System.out.println("Setting value at " + row + "," + col + " to " + value + " (an instance of " +
                               value.getClass() + ")");
        }

        fireTableCellUpdated(row, col);
        if (col == SWConstants.TABLE_PASSWORD_INDEX) {
            sp.setPassword(value.toString());

            data[row][col] = "";
            for (char a : sp.getPassword().toCharArray()) {
                data[row][col] += "*";
            }
        }
        if (DEBUG) {
            System.out.println("New value of data:");
            printDebugData();
        }
    }

        

    private void printDebugData() {
        int numRows = getRowCount();
        int numCols = getColumnCount();

        for (int i = 0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j = 0; j < numCols; j++) {
                System.out.print("  " + data[i][j]);
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }
}

