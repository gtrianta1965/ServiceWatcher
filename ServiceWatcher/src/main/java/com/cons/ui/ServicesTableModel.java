package com.cons.ui;

import com.cons.Configuration;

import com.cons.ServiceParameter;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ServicesTableModel extends AbstractTableModel {
    @SuppressWarnings("compatibility:3592099106572457242")
    private boolean DEBUG = false;
    private static final long serialVersionUID = 1L;
    private String[] columnNames = { "ID", "URL", "Description", "Type", "Group" , "Status"};

    private Object[][] data;
    
    
    
    public void changeModel() {
        for (int i=0 ; i< getRowCount() ; i++) {
            data[i][5] = "TATA";
        }
        
        //Send an event to the table to refresh the data
        fireTableDataChanged();
              
    }
    
    public void initFromConfiguration(Configuration configuration) {
        int rows = configuration.getServiceParameters().size();
        ServiceParameter sp;
        List<ServiceParameter> spl = configuration.getServiceParameters();
        data = new Object[rows][6];
        
        for (int i=0 ; i< rows ; i++) {
            sp = (ServiceParameter)spl.get(i);
            data[i][0] = sp.getId();
            data[i][1] = sp.getUrl();
            data[i][2] = sp.getDescription();
            data[i][3] = sp.getType();
            data[i][4] = sp.getGroup();
            data[i][5] = new String();
        }    
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

        data[row][col] = value;
        fireTableCellUpdated(row, col);

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

