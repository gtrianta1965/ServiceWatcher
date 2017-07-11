package com.cons.ui;

import java.awt.Component;

import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

class Add_password extends JPasswordField
implements TableCellRenderer {

    public PasswordCellRenderer() {
        super();
        // This displays astericks in fields since it is a password.
        // It does not affect the actual value of the cell.
        this.setText("filler123");
    }

    public Component getTableCellRendererComponent(
    JTable arg0,
    Object arg1,
    boolean arg2,
    boolean arg3,
    int arg4,
    int arg5) {
        return this;
    }
}