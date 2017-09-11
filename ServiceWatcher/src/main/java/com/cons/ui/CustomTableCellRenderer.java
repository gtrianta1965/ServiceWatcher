package com.cons.ui;

import com.cons.utils.SWConstants;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
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
        

        if (column == SWConstants.TABLE_ID_INDEX){
            setHorizontalAlignment(SwingConstants.CENTER);            
        }
        
        if (column == SWConstants.TABLE_RETRIES_INDEX) {
            setHorizontalAlignment(SwingConstants.CENTER);
            if (!value.toString().startsWith("0/")) {                
                c.setBackground(Color.orange);
            }
        }
        
        if (column == SWConstants.TABLE_PASSWORD_INDEX) {
            JPasswordField pass = new JPasswordField();
            pass.setText(String.valueOf(value));
            if (table.isCellEditable(row, column) == false) {
                pass.setBackground(Color.lightGray);
            }
            c=pass;
        }
        
        if(column==SWConstants.TABLE_CONTEXT_INDEX){
            JLabel lbl =new JLabel();
            lbl.setText(String.valueOf(value));
            lbl.setToolTipText(String.valueOf(value));
            lbl.setFont(new Font("plain", Font.PLAIN, 12));
            c = lbl;
        }
        return c;
    }

}
