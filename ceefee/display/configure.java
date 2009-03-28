
package ceefee.display;


public class configure extends javax.swing.JFrame {
    
    private ceefee.main mainFrameRef;

    private boolean settingsUpdated;
    
    final int FORM_HEIGHT           =210;
    final int FORM_WIDTH            =390;
    
    final int HEADER_SIZE           =25;

    final String[] yn=new String[] {"Yes","No"};
    
    final private int GENERAL_PANEL                 =0;
    final private int NETWORK_PANEL                 =1;
    final private int LF_PANEL                      =2;
    
    
    /** Creates new form general_configure */
    public configure(final ceefee.main _mainFrameRef, final int parentX, final int parentY, final int parentWidth, final int parentHeight) {
        mainFrameRef=_mainFrameRef;
        _mainFrameRef.hide();
        initComponents();
        
        showPanel(GENERAL_PANEL);
                
        gHeader.setCursor(mainFrameRef.handCursor);
        ncHeader.setCursor(mainFrameRef.handCursor);
        lfHeader.setCursor(mainFrameRef.handCursor);
        
        jLabel3.setCursor(mainFrameRef.handCursor);
        jLabel7.setCursor(mainFrameRef.handCursor);
        jLabel8.setCursor(mainFrameRef.handCursor);
        jLabel9.setCursor(mainFrameRef.handCursor);
        jLabel10.setCursor(mainFrameRef.handCursor);
        jLabel11.setCursor(mainFrameRef.handCursor);
        jLabel12.setCursor(mainFrameRef.handCursor);
        jLabel14.setCursor(mainFrameRef.handCursor);

        scClicker.setForeground(mainFrameRef.default1);
        
        lfHeader.setBackground(mainFrameRef.default4);
        ncHeader.setBackground(mainFrameRef.default4);
        gHeader.setBackground(mainFrameRef.default4);

        cruxPanel.setBackground(mainFrameRef.default4);
        
        scClicker.setCursor(mainFrameRef.handCursor);
        
        setIconImage(mainFrameRef.getIconImage());
        
        setBounds(parentX+((parentWidth-FORM_WIDTH)/2),parentY+((parentHeight-FORM_HEIGHT)/2),FORM_WIDTH, FORM_HEIGHT);

        cruxPanel.setBounds(0, 0, FORM_WIDTH, FORM_HEIGHT-HEADER_SIZE);
       
    }
    
    private void showPanel( final int panel ) {
        
        if ( panel==GENERAL_PANEL ) {
            gHeader.setIcon(mainFrameRef.configureIcon);
            generalPanel.setVisible(true);
        } else {
            gHeader.setIcon(null);
            generalPanel.setVisible(false);
        }

        if ( panel==NETWORK_PANEL ) {
            ncHeader.setIcon(mainFrameRef.configureIcon);
            networkPanel.setVisible(true);
        } else {
            ncHeader.setIcon(null);
            networkPanel.setVisible(false);
        }

        if ( panel==LF_PANEL ) {
            lfHeader.setIcon(mainFrameRef.configureIcon);
            lfPanel.setVisible(true);
        } else {
            lfHeader.setIcon(null);
            lfPanel.setVisible(false);
        }
            
    }
    
    private void initComponents() {//GEN-BEGIN:initComponents
        cruxPanel = new javax.swing.JPanel();
        gHeader = new javax.swing.JLabel();
        ncHeader = new javax.swing.JLabel();
        lfHeader = new javax.swing.JLabel();
        generalPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        networkPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lfPanel = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        scClicker = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CeeFee - Configuration Console");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        cruxPanel.setLayout(null);

        gHeader.setText("  General");
        gHeader.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gHeaderMouseClicked(evt);
            }
        });

        cruxPanel.add(gHeader);
        gHeader.setBounds(20, 20, 100, 19);

        ncHeader.setText("  Network Control");
        ncHeader.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ncHeaderMouseClicked(evt);
            }
        });

        cruxPanel.add(ncHeader);
        ncHeader.setBounds(20, 120, 130, 19);

        lfHeader.setText("  Look & Feel");
        lfHeader.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lfHeaderMouseClicked(evt);
            }
        });

        cruxPanel.add(lfHeader);
        lfHeader.setBounds(20, 70, 100, 19);

        generalPanel.setLayout(null);

        generalPanel.setOpaque(false);
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setText("<HTML><U>Download Directory.</U></HTML>");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
        });

        generalPanel.add(jLabel3);
        jLabel3.setBounds(30, 10, 170, 20);

        cruxPanel.add(generalPanel);
        generalPanel.setBounds(170, 10, 210, 120);

        networkPanel.setLayout(null);

        networkPanel.setOpaque(false);
        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel7.setText("<HTML><U>Set Default MTU.</U></HTML>");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel7MouseExited(evt);
            }
        });

        networkPanel.add(jLabel7);
        jLabel7.setBounds(30, 70, 130, 20);

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setText("<HTML><U>Connection Attempts.</U></HTML>");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel8MouseExited(evt);
            }
        });

        networkPanel.add(jLabel8);
        jLabel8.setBounds(30, 40, 290, 20);

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel9.setText("<HTML><U>Set Connection Time-out.</U></HTML>");
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel9MouseExited(evt);
            }
        });

        networkPanel.add(jLabel9);
        jLabel9.setBounds(30, 10, 180, 20);

        cruxPanel.add(networkPanel);
        networkPanel.setBounds(170, 10, 210, 120);

        lfPanel.setLayout(null);

        lfPanel.setOpaque(false);
        jLabel14.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel14.setText("<HTML><U>Graphics Mode.</U></HTML>");
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel14MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel14MouseExited(evt);
            }
        });

        lfPanel.add(jLabel14);
        jLabel14.setBounds(30, 100, 150, 20);

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel11.setText("<HTML><U>History Items.</U></HTML>");
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel11MouseExited(evt);
            }
        });

        lfPanel.add(jLabel11);
        jLabel11.setBounds(30, 70, 160, 20);

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel10.setText("<HTML><U>Overwrite Confirmations.</U></HTML>");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel10MouseExited(evt);
            }
        });

        lfPanel.add(jLabel10);
        jLabel10.setBounds(30, 40, 180, 20);

        jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel12.setText("<HTML><U>Delete Confirmations.</U></HTML>");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel12MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel12MouseExited(evt);
            }
        });

        lfPanel.add(jLabel12);
        jLabel12.setBounds(30, 10, 160, 20);

        cruxPanel.add(lfPanel);
        lfPanel.setBounds(170, 10, 210, 120);

        scClicker.setFont(new java.awt.Font("Dialog", 1, 14));
        scClicker.setText("<HTML><U>Exit</U></HTML>");
        scClicker.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scClickerMouseClicked(evt);
            }
        });

        cruxPanel.add(scClicker);
        scClicker.setBounds(320, 150, 30, 20);

        getContentPane().add(cruxPanel);
        cruxPanel.setBounds(0, 0, 390, 210);

        pack();
    }//GEN-END:initComponents

    private void gHeaderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gHeaderMouseClicked
        showPanel(GENERAL_PANEL);
    }//GEN-LAST:event_gHeaderMouseClicked

    private void lfHeaderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lfHeaderMouseClicked
        showPanel(LF_PANEL);
    }//GEN-LAST:event_lfHeaderMouseClicked

    private void ncHeaderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ncHeaderMouseClicked
        showPanel(NETWORK_PANEL);
    }//GEN-LAST:event_ncHeaderMouseClicked

    private void scClickerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scClickerMouseClicked
        close();
    }//GEN-LAST:event_scClickerMouseClicked

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        String currentSetting;
        if ( mainFrameRef.graphicsMode==0 )
            currentSetting="Spartan";
        else
            currentSetting="Standard";
        Object[] retData=new Object[2];
        retData[0]=javax.swing.JOptionPane.showInputDialog(this,"Choose your desired graphics mode:", "", javax.swing.JOptionPane.PLAIN_MESSAGE, null, new String[] {"Spartan", "Standard"}, currentSetting);
        if ( retData[0]==null || retData[0].toString().toString().toLowerCase().equals("spartan") ) {
            mainFrameRef.fileioClassInstance.storeDataToFile("_user.dat", "graphicsmode", "", "0", 1, 1, "", false);
            mainFrameRef.graphicsMode=0;
        } else {
            mainFrameRef.fileioClassInstance.storeDataToFile("_user.dat", "graphicsmode", "", "1", 1, 1, "", false);
            mainFrameRef.graphicsMode=1;
        }
        jLabel14.setFont(mainFrameRef.boldAndItalicFont);
        settingsUpdated=true;
        return;
        
    }//GEN-LAST:event_jLabel14MouseClicked

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        String currentSetting;
        if ( mainFrameRef.displayOverwriteConfirmations )
            currentSetting="Yes";
        else
            currentSetting="No";
        Object[] retData=new Object[2];
        retData[0]=javax.swing.JOptionPane.showInputDialog(this,"Display overwrite confirmations?", "", javax.swing.JOptionPane.PLAIN_MESSAGE, null, yn, currentSetting);
        if ( retData[0]==null || retData[0].toString().toString().toLowerCase().equals("yes") ) {
            mainFrameRef.fileioClassInstance.storeDataToFile("_user.dat", "displayoverwriteconfirmations", "", "true", 1, 1, "", false);
            mainFrameRef.displayOverwriteConfirmations=true;
        } else {
            mainFrameRef.fileioClassInstance.storeDataToFile("_user.dat", "displayoverwriteconfirmations", "", "false", 1, 1, "", false);
            mainFrameRef.displayOverwriteConfirmations=false;
        }
        jLabel10.setFont(mainFrameRef.boldAndItalicFont);
        settingsUpdated=true;
        return;

    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        String currentSetting;
        if ( mainFrameRef.displayDeleteConfirmations )
            currentSetting="Yes";
        else
            currentSetting="No";
        Object[] retData=new Object[2];
        retData[0]=javax.swing.JOptionPane.showInputDialog(this,"Display delete confirmations?", "", javax.swing.JOptionPane.PLAIN_MESSAGE, null, yn, currentSetting);
        if ( retData[0]==null || retData[0].toString().toString().toLowerCase().equals("yes") ) {
            mainFrameRef.fileioClassInstance.storeDataToFile("_user.dat", "displaydeleteconfirmations", "", "true", 1, 1, "", false);
            mainFrameRef.displayDeleteConfirmations=true;
        } else {
            mainFrameRef.fileioClassInstance.storeDataToFile("_user.dat", "displaydeleteconfirmations", "", "false", 1, 1, "", false);
            mainFrameRef.displayDeleteConfirmations=false;
        }
        jLabel12.setFont(mainFrameRef.boldAndItalicFont);
        settingsUpdated=true;
        return;

    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        final int MAX_VALUE             =18;
        Object[] retData=new Object[2];
        retData[0]=javax.swing.JOptionPane.showInputDialog(this,"Please enter the desired number of history items to store.\n ( A value of '0' will disable the history feature. )",String.valueOf(mainFrameRef.historyTrailSize));
        if ( retData[0]==null || retData[0].toString().equals("")==false ) {
            try {
                if ( Integer.parseInt(retData[0].toString())<=MAX_VALUE ) {
                    mainFrameRef.fileioClassInstance.storeDataToFile("_user.dat", "historytrailsize", "", String.valueOf(Integer.parseInt(retData[0].toString())), 1, 1, "", false);
                    mainFrameRef.historyTrailSize=Integer.parseInt(retData[0].toString());
                    jLabel11.setFont(mainFrameRef.boldAndItalicFont);
                    settingsUpdated=true;
                    return;
                }
                                
            } catch ( java.lang.NumberFormatException nfe ) {}
            javax.swing.JOptionPane.showMessageDialog(this,"Unable to update configuration.\n This field requires an integer value in the range: { 1 - "+MAX_VALUE+" }","Syntax Error",javax.swing.JOptionPane.WARNING_MESSAGE);
        }
        
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked

        final int MAX_VALUE             =360000;
        Object[] retData=new Object[2];
        retData[0]=javax.swing.JOptionPane.showInputDialog(this,"Please enter the desired default Maximum Transmission Unit size.\n ( Caution: This setting is for Advanced users only! )",String.valueOf(mainFrameRef.connectionMTU));
        if ( retData[0]==null || retData[0].toString().equals("")==false ) {
            try {
                if ( Integer.parseInt(retData[0].toString())<=MAX_VALUE ) {
                    mainFrameRef.fileioClassInstance.storeDataToFile("_user.dat", "connectiondefaultmtu", "", String.valueOf(Integer.parseInt(retData[0].toString())), 1, 1, "", false);
                    mainFrameRef.connectionMTU=Integer.parseInt(retData[0].toString());
                    jLabel7.setFont(mainFrameRef.boldAndItalicFont);
                    settingsUpdated=true;
                    return;
                }
            } catch ( java.lang.NumberFormatException nfe ) {}
            javax.swing.JOptionPane.showMessageDialog(this,"Unable to update configuration.\n This field requires an integer value in the range: { 1 - "+MAX_VALUE+" }","Syntax Error",javax.swing.JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        final int MAX_VALUE         =32766;
        Object[] retData=new Object[2];

        retData[0]=javax.swing.JOptionPane.showInputDialog(this,"Please enter the desired maximum number of connection attempts.\n ( A value of '3' is usually nominal. )",String.valueOf(mainFrameRef.connectionAttempts));
        if ( retData[0]==null || retData[0].toString().equals("")==false ) {
            try {
                if ( Integer.parseInt(retData[0].toString())<=MAX_VALUE ) {
                    mainFrameRef.fileioClassInstance.storeDataToFile("_user.dat", "connectionattempts", "", String.valueOf((int)Integer.parseInt(retData[0].toString())), 1, 1, "", false);
                    mainFrameRef.connectionAttempts=Integer.parseInt(retData[0].toString());
                    jLabel8.setFont(mainFrameRef.boldAndItalicFont);
                    settingsUpdated=true;
                    return;
                }
            } catch ( java.lang.NumberFormatException nfe ) {}
            javax.swing.JOptionPane.showMessageDialog(this,"Unable to update configuration.\n This field requires an integer value in the range: { 1 - "+MAX_VALUE+" }","Syntax Error",javax.swing.JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_jLabel8MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        final int MAX_VALUE             =360000;
        Object[] retData=new Object[2];
        retData[0]=javax.swing.JOptionPane.showInputDialog(this,"Please enter the desired socket time-out value.\n ( e.g. '30' for a 1-half minute time-out  )",String.valueOf(mainFrameRef.connectionTimeout/1000));
        if ( retData[0]==null || retData[0].toString().equals("")==false ) {
            try {
                if ( (Integer.parseInt(retData[0].toString())*1E3)<=MAX_VALUE ) {
                    mainFrameRef.fileioClassInstance.storeDataToFile("_user.dat", "connectiontimeout", "", String.valueOf((int)(Integer.parseInt(retData[0].toString())*1E3)), 1, 1, "", false);
                    mainFrameRef.connectionTimeout=(int)(Integer.parseInt(retData[0].toString())*1E3);
                    jLabel9.setFont(mainFrameRef.boldAndItalicFont);
                    settingsUpdated=true;
                    return;
                }
            } catch ( java.lang.NumberFormatException nfe ) {}
            javax.swing.JOptionPane.showMessageDialog(this,"Unable to update configuration.\n This field requires an integer value in the range: { 1 - "+MAX_VALUE+" }","Syntax Error",javax.swing.JOptionPane.WARNING_MESSAGE);
        }
                
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        final javax.swing.JFileChooser jFileChooser=new javax.swing.JFileChooser(mainFrameRef.downloadDir);

        jFileChooser.setApproveButtonText("Select");
        jFileChooser.setDialogTitle("Browse for folder...");
        jFileChooser.setFileSelectionMode(jFileChooser.DIRECTORIES_ONLY);
        jFileChooser.setAcceptAllFileFilterUsed(false);
        jFileChooser.setFileFilter(mainFrameRef.folderFilterInstance);
        if ( jFileChooser.showOpenDialog(this)==jFileChooser.APPROVE_OPTION ) {
            if ( new java.io.File(jFileChooser.getSelectedFile().getPath()).exists() ) {
                mainFrameRef.fileioClassInstance.storeDataToFile("_user.dat", "downloaddir", "", jFileChooser.getSelectedFile().getPath(), 1, 1, "", false);
                mainFrameRef.downloadDir=jFileChooser.getSelectedFile().getPath();
                jLabel3.setFont(mainFrameRef.boldAndItalicFont);
                settingsUpdated=true;
            }
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel14MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseExited
        jLabel14.setIcon(null);
    }//GEN-LAST:event_jLabel14MouseExited

    private void jLabel14MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseEntered
        if ( mainFrameRef.graphicsMode==1 ) jLabel14.setIcon(mainFrameRef.configureIcon);
    }//GEN-LAST:event_jLabel14MouseEntered

    private void jLabel7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseExited
        jLabel7.setIcon(null);
    }//GEN-LAST:event_jLabel7MouseExited

    private void jLabel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseEntered
        if ( mainFrameRef.graphicsMode==1 ) jLabel7.setIcon(mainFrameRef.configureIcon);
    }//GEN-LAST:event_jLabel7MouseEntered

    private void jLabel8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseExited
        jLabel8.setIcon(null);
    }//GEN-LAST:event_jLabel8MouseExited

    private void jLabel8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseEntered
        if ( mainFrameRef.graphicsMode==1 ) jLabel8.setIcon(mainFrameRef.configureIcon);
    }//GEN-LAST:event_jLabel8MouseEntered

    private void jLabel9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseExited
        jLabel9.setIcon(null);
    }//GEN-LAST:event_jLabel9MouseExited

    private void jLabel9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseEntered
        if ( mainFrameRef.graphicsMode==1 ) jLabel9.setIcon(mainFrameRef.configureIcon);
    }//GEN-LAST:event_jLabel9MouseEntered

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        jLabel3.setIcon(null);
    }//GEN-LAST:event_jLabel3MouseExited

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        if ( mainFrameRef.graphicsMode==1 ) jLabel3.setIcon(mainFrameRef.configureIcon);
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseExited
        jLabel11.setIcon(null);
    }//GEN-LAST:event_jLabel11MouseExited

    private void jLabel11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseEntered
        if ( mainFrameRef.graphicsMode==1 ) jLabel11.setIcon(mainFrameRef.configureIcon);
    }//GEN-LAST:event_jLabel11MouseEntered

    private void jLabel10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseExited
        jLabel10.setIcon(null);
    }//GEN-LAST:event_jLabel10MouseExited

    private void jLabel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseEntered
        if ( mainFrameRef.graphicsMode==1 ) jLabel10.setIcon(mainFrameRef.configureIcon);
    }//GEN-LAST:event_jLabel10MouseEntered

    private void jLabel12MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseExited
        jLabel12.setIcon(null);
    }//GEN-LAST:event_jLabel12MouseExited

    private void jLabel12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseEntered
        if ( mainFrameRef.graphicsMode==1 ) jLabel12.setIcon(mainFrameRef.configureIcon);
    }//GEN-LAST:event_jLabel12MouseEntered
    
    void close() {
        
        if ( settingsUpdated ) {
            if ( javax.swing.JOptionPane.showConfirmDialog(mainFrameRef, "Some settings may not take effect until after you restart the application.\nWould you like to restart the application at this time?","",javax.swing.JOptionPane.YES_NO_OPTION)==javax.swing.JOptionPane.YES_OPTION ) {
                mainFrameRef.reload();
                return;
            } else
                javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Settings will take effect next time you re-start the application.","Configuration Update Complete.",javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
        
        mainFrameRef.setVisible(true);
        dispose();

    }
    
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        close();
    }//GEN-LAST:event_exitForm

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cruxPanel;
    private javax.swing.JLabel gHeader;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lfHeader;
    private javax.swing.JPanel lfPanel;
    private javax.swing.JLabel ncHeader;
    private javax.swing.JPanel networkPanel;
    private javax.swing.JLabel scClicker;
    // End of variables declaration//GEN-END:variables
    
}
