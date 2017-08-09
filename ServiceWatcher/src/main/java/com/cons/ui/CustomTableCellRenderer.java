package com.cons.ui;

import com.cons.utils.SWConstants;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    public CustomTableCellRenderer() {
        super();
    }    

      public Component getTableCellRendererComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     boolean hasFocus,
                                                     int row,
                                                     int column) {
        Component c = 
          super.getTableCellRendererComponent(table, value,
                                              isSelected, hasFocus,
                                              row, column);

        // Only for specific cell
        c.setBackground(Color.white);
        setHorizontalAlignment(SwingConstants.LEFT);
        if (column == SWConstants.TABLE_STATUS_INDEX) {
            if (value.equals(SWConstants.SERVICE_RUNNING)) {                 
                c.setBackground(Color.yellow);
            } else if (value.toString().startsWith(SWConstants.SERVICE_SUCCESS)) {
                c.setBackground(Color.green);
            } else if (value.toString().startsWith(SWConstants.SERVICE_FAILED)) {
                c.setBackground(Color.red);
            }

        }
        if (column == SWConstants.TABLE_RETRIES_INDEX) {
            setHorizontalAlignment(SwingConstants.CENTER);
            if (!value.toString().startsWith("0/")) {                
                c.setBackground(Color.orange);
            }
        }
        
        if (column == SWConstants.TABLE_PASSWORD_INDEX) {
            if (table.isCellEditable(row, column) == false) {
                c.setBackground(Color.lightGray);
            }
        }


        return c;
    }

}
