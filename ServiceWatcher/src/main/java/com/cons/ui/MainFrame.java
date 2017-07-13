
package com.cons.ui;

import com.cons.Configuration;
import com.cons.services.ServiceOrchestrator;
import com.cons.ui.CustomTableCellRenderer;
import com.cons.utils.SWConstants;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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
        statusMsg.setText((serviceOrchestrator.getStatus()).toString());
        
 
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
        btnLoad = new javax.swing.JButton();
        statusBar = new javax.swing.JPanel();
        statusBarSection1 = new javax.swing.JPanel();
        statusMsg = new javax.swing.JLabel();
        statusBarSection2 = new javax.swing.JPanel();
        statusRun = new javax.swing.JLabel();
        statusBarSection3 = new javax.swing.JPanel();

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

        btnLoad.setIcon(new ImageIcon(this.getClass().getResource("/src/images/add.png")));
        btnLoad.setText("Add file");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoadProperties(evt);
            }
        });

        statusBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        statusBarSection1.setAlignmentX((float) 0.8);

        javax.swing.GroupLayout statusBarSection1Layout = new javax.swing.GroupLayout(statusBarSection1);
        statusBarSection1.setLayout(statusBarSection1Layout);
        statusBarSection1Layout.setHorizontalGroup(
            statusBarSection1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusBarSection1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        statusBarSection1Layout.setVerticalGroup(
            statusBarSection1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusBarSection1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statusRun.setText(getServiceOrchestrator() !=null ? (serviceOrchestrator.isRunning()?"RUNNING":"IDLE") :"IDLE");

        javax.swing.GroupLayout statusBarSection2Layout = new javax.swing.GroupLayout(statusBarSection2);
        statusBarSection2.setLayout(statusBarSection2Layout);
        statusBarSection2Layout.setHorizontalGroup(
            statusBarSection2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 227, Short.MAX_VALUE)
            .addGroup(statusBarSection2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusBarSection2Layout.createSequentialGroup()
                    .addContainerGap(86, Short.MAX_VALUE)
                    .addComponent(statusRun, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(47, Short.MAX_VALUE)))
        );
        statusBarSection2Layout.setVerticalGroup(
            statusBarSection2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
            .addGroup(statusBarSection2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusBarSection2Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statusRun)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout statusBarSection3Layout = new javax.swing.GroupLayout(statusBarSection3);
        statusBarSection3.setLayout(statusBarSection3Layout);
        statusBarSection3Layout.setHorizontalGroup(
            statusBarSection3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 215, Short.MAX_VALUE)
        );
        statusBarSection3Layout.setVerticalGroup(
            statusBarSection3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout statusBarLayout = new javax.swing.GroupLayout(statusBar);
        statusBar.setLayout(statusBarLayout);
        statusBarLayout.setHorizontalGroup(
            statusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusBarLayout.createSequentialGroup()
                .addComponent(statusBarSection1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusBarSection3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(statusBarSection2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
        statusBarLayout.setVerticalGroup(
            statusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusBarLayout.createSequentialGroup()
                .addGroup(statusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(statusBarSection3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statusBarSection2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statusBarSection1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVersion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addComponent(jScrollPane1)
                    .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(lblVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }//GEN-END:initComponents

    private void ButtonExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonExit
      this.dispose();  //Remove JFrame 
      System.exit(0);
    }//GEN-LAST:event_ButtonExit

    private void buttonRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRefresh
       //TODO: Disable button, check if orchestrator is running, display a message if it is already running;
        serviceOrchestrator.start();
        
       
    }//GEN-LAST:event_buttonRefresh


    /* actions in order to add a new config file*/
    private void buttonLoadProperties(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoadProperties
        if(!serviceOrchestrator.isRunning()){
            JFileChooser fc = new JFileChooser();
            javax.swing.JFileChooser jFileChooser1 = new javax.swing.JFileChooser();
            int returnVal = fc.showOpenDialog(jFileChooser1);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                serviceOrchestrator.loadNewFile(file);  
            }
        }else{
            JOptionPane.showMessageDialog(null, "Services are currently running. Please wait to finish.");
            System.out.println("running!whait to finish!");//popup to be implemented
        }
        
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblVersion;
    private javax.swing.JTable servicesTable;
    private javax.swing.JPanel statusBar;
    private javax.swing.JPanel statusBarSection1;
    private javax.swing.JPanel statusBarSection2;
    private javax.swing.JPanel statusBarSection3;
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

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
