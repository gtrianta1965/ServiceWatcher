
package com.cons.ui;


import com.cons.Configuration;
import com.cons.services.ServiceOrchestrator;
import com.cons.utils.DateUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
    Configuration configuration;
    private int interval=200;
    private boolean send=false;
    private Timer generic_timer;
    private long diff;

    //The following variables control the fire times of the refresh timer.    
    private Date currentRefreshFireTime = null;
    private Date nextRefreshFireTime = null;
    
    /** Creates new form MainFrame */
    public MainFrame() {

    }
    public void initialization() {
        generic_timer = new Timer(interval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                checkAutoRefresh();
                updateStatusBar();
                checkSendMail();
            }
        });
        generic_timer.start();
    }
    public ImageIcon getImg(){
        if(serviceOrchestrator.isRunning()){
            return new ImageIcon(this.getClass().getResource("/src/images/active.gif"));
        }else{
            return new ImageIcon();
        }
    }
    
    public String[] getIntervalnumbers(){
        String[] intervals = this.serviceOrchestrator.getConfiguration().getAutoRefreshIntervals();
        
        return intervals;
    }
    
    /**
     * Updates status bar.
     */
    private void updateStatusBar(){
        statusMsg.setText((serviceOrchestrator.getStatus()).toHTML());
        if(statusRun.getIcon() == null && serviceOrchestrator.isRunning()){
            statusRun.setIcon(getImg());
        }else if(!serviceOrchestrator.isRunning()){
            statusRun.setIcon(null);
        }
        
        statusRun.setText(getServiceOrchestrator()!=null?(!serviceOrchestrator.isRunning()?"IDLE":""):"IDLE");
        if (serviceOrchestrator.isRunning()) {
            btnRefresh.setEnabled(false);
            btnLoad.setEnabled(false);
        } else {
            btnRefresh.setEnabled(true);
            btnLoad.setEnabled(true);
        }
    }
    
    /**
     * Checks if it should send emails for the current run.
     */
    private void checkSendMail(){
        if(this.send && 
           !this.serviceOrchestrator.isRunning() && 
           this.serviceOrchestrator.getConfiguration().getSendMailUpdates() && 
           (this.serviceOrchestrator.getConfiguration().getSmtpSendEmailOnSuccess() || 
            this.serviceOrchestrator.getOrchestratorStatus().getTotalSubmitted()!=this.serviceOrchestrator.getOrchestratorStatus().getTotalSuccess())){
            
            this.send = false;
            this.serviceOrchestrator.sendStatusLog();
            this.serviceOrchestrator.cleanLog();
        }
    }
    
    private void RefreshServices() {
        this.send = true;
        currentRefreshFireTime = nextRefreshFireTime;
        nextRefreshFireTime = DateUtils.addMinutesToDate(currentRefreshFireTime, 
                                                         Long.parseLong(cbAutoRefreshInterval.getSelectedItem().toString()));
        serviceOrchestrator.start();
    }
    
    /*
     * CheckForAutoRefresh
     */
    
    public void checkAutoRefresh(){
        if (checkAutoRefresh.isSelected()) {
            //Check if we are running, that is the current time is not null
            if (currentRefreshFireTime != null) {
                diff = DateUtils.getDateDiff(new Date(), nextRefreshFireTime, TimeUnit.SECONDS);
                //The diff should be positive (as the nextRefreshDate points in the future
                //In case the diff is negative then force a refresh. This might happen if 
                //we put the PC in a sleep mode. When it wakes up the diff is negative
                if (diff < 0) {
                    nextRefreshFireTime = DateUtils.addMinutesToDate(new Date(), Long.parseLong(cbAutoRefreshInterval.getSelectedItem().toString()));                    
                    diff = DateUtils.getDateDiff(new Date(), nextRefreshFireTime, TimeUnit.SECONDS);
                }
                next_refresh.setText("Next refresh in "+String.valueOf(diff)+" s");
                if (diff == 0) {
                    next_refresh.setText("Next refresh in "+String.valueOf(diff)+" s");
                    RefreshServices();
                }
            } else {
                this.send = true;
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
        float[] columnWidthPercentage = {2.0f, 30.0f, 20.0f, 3.0f, 10.0f, 35.0f, 35.0f};
        
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
        servicesTable.setColumnSelectionAllowed(true);
        servicesTable.setRowSelectionAllowed(true);
        
 
           if (servicesTable.getCellEditor() != null) {
             servicesTable.getCellEditor().stopCellEditing();
            }
 
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
        lblVersion = new javax.swing.JLabel();
        checkAutoRefresh = new javax.swing.JCheckBox();
        cbAutoRefreshInterval = new javax.swing.JComboBox<>();
        statusBar = new javax.swing.JPanel();
        statusBarSection2 = new javax.swing.JPanel();
        statusRun = new javax.swing.JLabel();
        statusBarSection1 = new javax.swing.JPanel();
        statusMsg = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        next_refresh = new javax.swing.JLabel();
        btnLoad = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        servicesTable = new javax.swing.JTable();
        servicesTable.setDefaultRenderer(String.class, new CustomTableCellRenderer());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Service Watcher");
        setMinimumSize(new java.awt.Dimension(1004, 439));

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

        lblVersion.setBackground(javax.swing.UIManager.getDefaults().getColor("CheckBox.focus"));
        lblVersion.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblVersion.setForeground(new java.awt.Color(102, 102, 255));
        lblVersion.setText("Service Watcher v1.0");

        checkAutoRefresh.setText("Auto-Refresh");
        checkAutoRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkAutoRefreshActionPerformed(evt);
            }
        });

        cbAutoRefreshInterval.setModel(new javax.swing.DefaultComboBoxModel<>(this.getIntervalnumbers()));
        cbAutoRefreshInterval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAutoRefreshIntervalActionPerformed(evt);
            }
        });

        statusBar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        statusBarSection2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        statusRun.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statusRun.setIcon(this.getImg());
        statusRun.setText(getServiceOrchestrator()!=null?(!serviceOrchestrator.isRunning()?"IDLE":""):"IDLE");
        statusRun.setAlignmentX((float) 0.5);

        javax.swing.GroupLayout statusBarSection2Layout = new javax.swing.GroupLayout(statusBarSection2);
        statusBarSection2.setLayout(statusBarSection2Layout);
        statusBarSection2Layout.setHorizontalGroup(
            statusBarSection2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusBarSection2Layout.createSequentialGroup()
                .addComponent(statusRun, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        statusBarSection2Layout.setVerticalGroup(
            statusBarSection2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusRun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        statusBarSection1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        statusMsg.setText("jLabel1");
        statusMsg.setMinimumSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout statusBarSection1Layout = new javax.swing.GroupLayout(statusBarSection1);
        statusBarSection1.setLayout(statusBarSection1Layout);
        statusBarSection1Layout.setHorizontalGroup(
            statusBarSection1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusMsg, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE)
        );
        statusBarSection1Layout.setVerticalGroup(
            statusBarSection1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(next_refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(next_refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statusBarLayout = new javax.swing.GroupLayout(statusBar);
        statusBar.setLayout(statusBarLayout);
        statusBarLayout.setHorizontalGroup(
            statusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusBarLayout.createSequentialGroup()
                .addComponent(statusBarSection1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(statusBarSection2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        statusBarLayout.setVerticalGroup(
            statusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusBarSection2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(statusBarSection1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btnLoad.setIcon(new ImageIcon(this.getClass().getResource("/src/images/add.png")));
        btnLoad.setText("Add File");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoadProperties(evt);
            }
        });

        jScrollPane1.setAutoscrolls(true);

        servicesTable.setModel(servicesTableModel);
        jScrollPane1.setViewportView(servicesTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVersion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(checkAutoRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbAutoRefreshInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 500, Short.MAX_VALUE)
                        .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(lblVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(checkAutoRefresh)
                        .addComponent(cbAutoRefreshInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnLoad.getAccessibleContext().setAccessibleName("");

        pack();
    }//GEN-END:initComponents

    private void ButtonExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonExit
      this.dispose();  //Remove JFrame 
      System.exit(0);
    }//GEN-LAST:event_ButtonExit

    private void buttonRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRefresh
       //TODO: Disable button, check if orchestrator is running, display a message if it is already running
       this.send = true;
       serviceOrchestrator.start();
       
    }//GEN-LAST:event_buttonRefresh

    private void checkAutoRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkAutoRefreshActionPerformed
        // TODO add your handling code here:
        if (checkAutoRefresh.isSelected()) {
            cbAutoRefreshInterval.setEnabled(false);
            btnRefresh.setEnabled(false);
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
        
    private void buttonLoadProperties(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoadProperties
        if (!serviceOrchestrator.isRunning()) {
            JFileChooser fc = new JFileChooser();
            javax.swing.JFileChooser jFileChooser1 = new javax.swing.JFileChooser();
            int returnVal = fc.showOpenDialog(jFileChooser1);
        
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                serviceOrchestrator.loadNewFile(file);
            }
        } /*else {
            JOptionPane.showMessageDialog(null, "Services are currently running. Please wait to finish.");
            System.out.println("running!whait to finish!"); //popup to be implemented
        }*/
    }//GEN-LAST:event_buttonLoadProperties

    
    
    
    
    
    
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
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> cbAutoRefreshInterval;
    private javax.swing.JCheckBox checkAutoRefresh;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblVersion;
    public javax.swing.JLabel next_refresh;
    private javax.swing.JTable servicesTable;
    private javax.swing.JPanel statusBar;
    private javax.swing.JPanel statusBarSection1;
    private javax.swing.JPanel statusBarSection2;
    private javax.swing.JLabel statusMsg;
    private javax.swing.JLabel statusRun;
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
