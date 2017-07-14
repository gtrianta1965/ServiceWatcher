
package com.cons.ui;


import com.cons.services.ServiceOrchestrator;
import com.cons.utils.DateUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author gtrianta
 */
@SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")

public class MainFrame extends javax.swing.JFrame {
    ServicesTableModel servicesTableModel;
    ServiceOrchestrator serviceOrchestrator;
    private int interval=200;
    private Timer generic_timer;
    private long diff;

    //The following variables control the fire times of the refresh timer.    
    private Date currentRefreshFireTime = null;
    private Date nextRefreshFireTime = null;
    
    /** Creates new form MainFrame */
    public MainFrame() {

    }
    public void initialization(){
            generic_timer=new Timer( interval ,new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    checkAutoRefresh();
                }
            });
            generic_timer.start();          
        }
    private void RefreshServices() {
        currentRefreshFireTime = nextRefreshFireTime;
        nextRefreshFireTime = DateUtils.addMinutesToDate(currentRefreshFireTime, Long.parseLong(cbAutoRefreshInterval.getSelectedItem().toString()));
        System.out.println("yolo");
        serviceOrchestrator.start();
    }
    
    /*
     * CheckForAutoRefresh
     */
    
    public void checkAutoRefresh(){
        if (checkAutoRefresh.isSelected()) {
            //Check if we are running, that is the current time is not null
            if (currentRefreshFireTime != null) {
                diff = Math.abs(DateUtils.getDateDiff(nextRefreshFireTime, new Date(), TimeUnit.SECONDS));
                next_refresh.setText("Next refresh in "+String.valueOf(diff)+" s");
                if (diff == 0) {
                    next_refresh.setText("Next refresh in "+String.valueOf(diff)+" s");
                    RefreshServices();
                }
            } else {
                //The refresh never executed (execute it now)
                currentRefreshFireTime = new Date(); //Set the current to NOW!!!
                //Set the next fire time according to interval specified
                nextRefreshFireTime =
                    DateUtils.addMinutesToDate(currentRefreshFireTime,
                                               Long.parseLong(cbAutoRefreshInterval.getSelectedItem().toString()));
                serviceOrchestrator.start();
            }
        } else {
            //Autorefresh is disabled. Nullify current and next fire times
            currentRefreshFireTime = null;
            nextRefreshFireTime = null;
        }
    }

    private void setColumnsWidth() {
        //Size of each column espressed in percentage
        float[] columnWidthPercentage = {2.0f, 30.0f, 20.0f, 3.0f, 10.0f, 35.0f};
        
        int tW = servicesTable.getWidth();
            TableColumn column;
            TableColumnModel jTableColumnModel = servicesTable.getColumnModel();
            int cantCols = jTableColumnModel.getColumnCount();
            for (int i = 0; i < cantCols; i++) {
                column = jTableColumnModel.getColumn(i);
                int pWidth = Math.round(columnWidthPercentage[i] * tW);
                column.setPreferredWidth(pWidth);
            }
    }

    public void initModel(ServicesTableModel stm) {
        servicesTableModel = stm;       
        
        //ImageIcon icon = new ImageIcon(this.getClass().getResource("/src/images/refresh.png"));
        initComponents();  
        setColumnsWidth();
        servicesTable.getTableHeader().setReorderingAllowed(false);
 
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        btnRefresh = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        servicesTable = new javax.swing.JTable();
        servicesTable.setDefaultRenderer(String.class, new CustomTableCellRenderer());
        lblVersion = new javax.swing.JLabel();
        checkAutoRefresh = new javax.swing.JCheckBox();
        cbAutoRefreshInterval = new javax.swing.JComboBox<>();
        next_refresh = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Service Watcher");

        btnRefresh.setIcon(new ImageIcon(this.getClass().getResource("/src/images/refresh.png")));
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRefresh(evt);
            }
        });

        btnExit.setIcon(new ImageIcon(this.getClass().getResource("/src/images/exit.png")));
        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonExit(evt);
            }
        });

        servicesTable.setModel(servicesTableModel);
        jScrollPane1.setViewportView(servicesTable);

        lblVersion.setBackground(javax.swing.UIManager.getDefaults().getColor("CheckBox.focus"));
        lblVersion.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblVersion.setText("Service Watcher v1.0");

        checkAutoRefresh.setText("Auto-Refresh");
        checkAutoRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkAutoRefreshActionPerformed(evt);
            }
        });

        cbAutoRefreshInterval.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1","2","3"}));
        cbAutoRefreshInterval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAutoRefreshIntervalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVersion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 938, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkAutoRefresh)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cbAutoRefreshInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(next_refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 425, Short.MAX_VALUE)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(lblVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(checkAutoRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbAutoRefreshInterval)
                            .addComponent(next_refresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30))))
        );

        pack();
    }//GEN-END:initComponents

    private void ButtonExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonExit
      this.dispose();  //Remove JFrame 
      System.exit(0);
    }//GEN-LAST:event_ButtonExit

    private void buttonRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRefresh
       //TODO: Disable button, check if orchestrator is running, display a message if it is already running
       serviceOrchestrator.start();
       
    }//GEN-LAST:event_buttonRefresh

    private void checkAutoRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkAutoRefreshActionPerformed
        // TODO add your handling code here:
      
        
        if (checkAutoRefresh.isSelected()) {
            cbAutoRefreshInterval.setEnabled(false);
            String time_value= String.valueOf(cbAutoRefreshInterval.getSelectedItem());           
            //serviceOrchestrator.start();
        } else {
            cbAutoRefreshInterval.setEnabled(true);
            btnRefresh.setEnabled(true);
        }
    }//GEN-LAST:event_checkAutoRefreshActionPerformed

    private void cbAutoRefreshIntervalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAutoRefreshIntervalActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cbAutoRefreshIntervalActionPerformed
        
        

    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing
                                                                   .UIManager
                                                                   .getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing
                         .UIManager
                         .setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util
                .logging
                .Logger
                .getLogger(MainFrame.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util
                .logging
                .Logger
                .getLogger(MainFrame.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util
                .logging
                .Logger
                .getLogger(MainFrame.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util
                .logging
                .Logger
                .getLogger(MainFrame.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt
            .EventQueue
            .invokeLater(new Runnable() {
                public void run() {
                    new MainFrame().setVisible(true);
                }
            });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> cbAutoRefreshInterval;
    private javax.swing.JCheckBox checkAutoRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblVersion;
    public javax.swing.JLabel next_refresh;
    private javax.swing.JTable servicesTable;
    // End of variables declaration//GEN-END:variables

    public void setServicesTableModel(ServicesTableModel servicesTableModel) {
        this.servicesTableModel = servicesTableModel;
    }

    public ServicesTableModel getServicesTableModel() {
        return servicesTableModel;
    }

    public void setServiceOrchestrator(ServiceOrchestrator serviceOrchestrator) {
        this.serviceOrchestrator = serviceOrchestrator;
    }

    public ServiceOrchestrator getServiceOrchestrator() {
        return serviceOrchestrator;
    }
}
