package com.cons.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;


public class Class1 extends JFrame {
	private JLabel labelIV = new JLabel("Enter IV:");
	private JLabel labelKey = new JLabel("Enter Key:");
	
	private JPasswordField passwordField1 = new JPasswordField(15);
	private JPasswordField passwordField2 = new JPasswordField(15);
	
	private JButton buttonOK = new JButton("OK");
	
	public Class1() {
		super("Setting Key and IV");
		
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(labelIV, constraints);
		
		constraints.gridx = 1;
		add(passwordField1, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(labelKey, constraints);
		
		constraints.gridx = 1;
		add(passwordField2, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		add(buttonOK, constraints);

		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				buttonOKActionPerformed(event);
			}
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
	}
	
        public void CloseFrame(){
            super.dispose();
        }
        
        
	private void buttonOKActionPerformed(ActionEvent event) {
		char[] password1 = passwordField1.getPassword();
		char[] password2 = passwordField2.getPassword();

		if (password1.length==16 && password2.length==16) {
		    JOptionPane.showMessageDialog(Class1.this, 
		            "Setting Password Completed!");
		    CloseFrame();
		} else {
		    JOptionPane.showMessageDialog(Class1.this, 
		                    "Password Should be 16 Characters");
		    return;
		}
	}
}
    