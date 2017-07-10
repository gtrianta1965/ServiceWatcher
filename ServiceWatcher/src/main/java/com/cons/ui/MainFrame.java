
package com.cons.ui;

import com.cons.Configuration;
import com.cons.services.ServiceOrchestrator;
import com.cons.ui.CustomTableCellRenderer;
import com.cons.utils.SWConstants;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

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
    private int interval;
    private Timer countdown_timer;
    private Timer timer;
    private int counter=0;
    /** Creates new form MainFrame */
    public MainFrame() {

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
        jCheckBox1 = new javax.swing.JCheckBox();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();

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

        jCheckBox1.setText("Auto-Refresh");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1","2","3"}));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
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
                            .addComponent(jCheckBox1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 463, Short.MAX_VALUE)
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
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox1)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
      
        
        if (jCheckBox1.isSelected()) {
            jComboBox1.setEnabled(false);
            String time_value= String.valueOf(jComboBox1.getSelectedItem());
            interval=Integer.parseInt(time_value)*3600000;
            System.out.println(time_value);
            btnRefresh.setEnabled(false);            
            serviceOrchestrator.start();
            jLabel1.setText("Minits left:" + String.valueOf(interval/60000));
            countdown_timer=new Timer(60000,new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if(serviceOrchestrator.getExecutor().isTerminated()){
                        counter=0;    
                    }else{
                        ++counter;                        
                    }
                    jLabel1.setText("Minits left:"+ String.valueOf((interval/60000)-counter));                    
                }
            });
                
            timer=new Timer(interval,new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent arg0){
                    serviceOrchestrator.start();
                }
            });
            timer.setRepeats(true);
            timer.start();
            countdown_timer.setRepeats(true);
            countdown_timer.start();
        } else {
            jLabel1.setText("Finished");

            countdown_timer.stop();
            timer.stop();
            jComboBox1.setEnabled(true);
            btnRefresh.setEnabled(true);
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jComboBox1ActionPerformed
        
        

    
    
    
    
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
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblVersion;
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
