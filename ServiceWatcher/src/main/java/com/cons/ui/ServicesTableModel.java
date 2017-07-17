package com.cons.ui;

import com.cons.Configuration;
import com.cons.services.ServiceParameter;
import com.cons.utils.SWConstants;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ServicesTableModel extends AbstractTableModel {
    @SuppressWarnings("compatibility:2206726703879486991")
    private static final long serialVersionUID = 1L;
    private boolean DEBUG = false;

    private String[] columnNames = { "ID", "URL", "Description", "Type", "Group" , "Status","Password"};

    private Object[][] data;
    
    private ServiceParameter sp;
    
    
    /*
     * Row is zero indexed!!!!
     */
    public void setStatus(int row, String status) {
        data[row][SWConstants.TABLE_STATUS_INDEX] = status;
        fireTableDataChanged();
    }
    
    public void initFromConfiguration(Configuration configuration) {
        int rows = configuration.getServiceParameters().size();
        
        List<ServiceParameter> spl = configuration.getServiceParameters();
        data = new Object[rows][SWConstants.TABLE_NUMBER_OF_COLUMNS];
        
        
        
        for (int i=0 ; i< rows ; i++) {
            sp = (ServiceParameter)spl.get(i);
            data[i][SWConstants.TABLE_ID_INDEX] = sp.getId();
            data[i][SWConstants.TABLE_URL_INDEX] = sp.getUrl();
            data[i][SWConstants.TABLE_DESCRIPTION_INDEX] = sp.getDescription();
            data[i][SWConstants.TABLE_TYPE_INDEX] = sp.getType();
            data[i][SWConstants.TABLE_GROUP_INDEX] = sp.getGroup();
            data[i][SWConstants.TABLE_STATUS_INDEX] = new String();
            data[i][SWConstants.TABLE_PASSWORD_INDEX] = new String();
            setValueAt(data[i][SWConstants.TABLE_PASSWORD_INDEX], i, SWConstants.TABLE_PASSWORD_INDEX);
            sp.setPassword(data[i][SWConstants.TABLE_PASSWORD_INDEX].toString());
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
        return getValueAt(0, c).getClass();
    }
    
    /*
     * Don't need to implement this method unless your table's editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Nothing is editable
        if(col==SWConstants.TABLE_PASSWORD_INDEX){
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
        if(col == SWConstants.TABLE_PASSWORD_INDEX){
            sp.setPassword(value.toString());
            System.out.println(sp.getPassword());
        }
        for(char a:sp.getPassword().toCharArray()){
            data[row][col] += "*";
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

