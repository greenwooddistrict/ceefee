
package ceefee.display;


public class sb extends javax.swing.JPanel {

    private ceefee.main mainFrameRef;

    public int tabIndex;
    public int scourMethod;
    
    // General FLAGS
    private boolean resultsTableInitialColumnsWidthsSet;
    public boolean iffyConnection;
    boolean savePasswordLock;
    public boolean activityListLock=false;
    public boolean hostDescriptionLock=false;
    public boolean protocolLock;
    public boolean busy;
    public boolean searching;

    // General
    public ceefee.ftp.establishftpconnection establishFtpConnectionInstance;
    public Thread establishFtpConnectionInstanceThread;

    String ftpServer[][]={};
    String activityListHistoryTrail[]={};
    String searchListHistory[]={};
    String searchStartLocationHistory[]={};
    
    public String currentWD;

    public int sessionID;

    public String serverHeader;

    public java.net.Socket controlSocket;
    public Object dataSocket;

    public long reactionTime;


    // Counters
    public int reconnectAttempt;

    // Display
    public javax.swing.JScrollPane resultsTableScrollPane;
    public ceefee.gui.dnd.jtable resultsTable;
    
    java.util.Vector resultsTableColumnData=new java.util.Vector(0,1);

    public javax.swing.table.DefaultTableModel resultsTableDataModel;
    
    public java.util.Vector resultsTableAccentedCells=new java.util.Vector(0,1);
    
    private ceefee.display.resultstabledatacellrenderer resultsTableDataCellRendererInstance;

    // MISC
    final int imaginaryLine=250;
    
    //public ceefee.sockets.connect socketConnectInstance;
    public Thread socketConnectThread;

    private java.awt.event.ActionEvent activityListActionPerformedActionEvent;
    
    public int lastValidServerIndex;
    
    
    public sb(final ceefee.main _mainFrameRef, final int _scourMethod, final int _tabIndex) {
        mainFrameRef=_mainFrameRef;
        
        scourMethod=_scourMethod;
        
        tabIndex=_tabIndex;

        initComponents();

        activityListActionPerformedActionEvent=new java.awt.event.ActionEvent(activityList, 1001, "comboBoxChanged");
        activityList.getEditor().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _sb_activitylistactionperformed();
            }
        });

        resultsTableDataCellRendererInstance=new ceefee.display.resultstabledatacellrenderer(mainFrameRef,this,mainFrameRef.default4, mainFrameRef.default3);
        
        createResultsTable();
        
        resultsTableScrollPane.setLocation(imaginaryLine+5, 20);
        
        for ( int securityLayerLooper=-1; ++securityLayerLooper<mainFrameRef.securityLayer.length; ) {
            protocol.addItem(mainFrameRef.securityLayer[securityLayerLooper]);
        }

        rch.setCursor(mainFrameRef.handCursor);
        rch.setForeground(mainFrameRef.default1);
        anh.setCursor(mainFrameRef.handCursor);
        anh.setForeground(mainFrameRef.default1);

        if ( scourMethod==mainFrameRef.SEARCH_METHOD ) {
            controlButton1.setText("Search");
            controlButton2.setText("Cancel");
            activityList.setVisible(false);
            upButton.setVisible(false);
           
        } else if ( scourMethod==mainFrameRef.BROWSE_METHOD ) {
            controlButton1.setText("Login");
            controlButton2.setText("Logout");
            searchList.setVisible(false);
            upButton.setVisible(true);
            
            upButton.setIcon(new javax.swing.ImageIcon(this.getClass().getClassLoader().getSystemResource("ceefee/_up16.png")));
            upButton.setBackground(mainFrameRef.default4);
        }
        goButton.setIcon(new javax.swing.ImageIcon(this.getClass().getClassLoader().getSystemResource("ceefee/_go16.png")));
        goButton.setBackground(mainFrameRef.default4);

        controlButton1.setBackground(mainFrameRef.default3);
        controlButton2.setBackground(mainFrameRef.default3);
        
        mainPanel.add(resultsTableScrollPane);
        mainPanel.setBackground(mainFrameRef.default4);

        advancedOptionsHider.setCursor(mainFrameRef.handCursor);
        
        mainSplitPane.setLocation(0,0);

        statusScrollPane.setLocation(0,0);
        status.setLocation(0,0);
        
        hideUnhide(false);
        sessionID=(tabIndex*10)+1;
    }

    public void killSockets() {
        try {
            if ( controlSocket!=null )//&& controlSocket.isConnected()==false )
                controlSocket.close();
        } catch ( java.io.IOException ioe ) {}
        
        try {
            if ( dataSocket!=null )//&& ((java.net.Socket)dataSocket).isConnected()==false )
                ((java.net.Socket)dataSocket).close();
        } catch ( java.io.IOException ioe ) {}

    }

    void _sb_activitylistactionperformed() {
        if ( !busy ) new Thread(new sb_activitylistactionperformed(mainFrameRef,this,sessionID,activityListActionPerformedActionEvent)).start();
    }
    public void enableFileMenuOptions( final int disable0enable1 ) {
        if ( disable0enable1==0 ) {
            deMenuItem.setVisible(false);
            downloadMenuItem.setVisible(false);
            deleteMenuItem.setVisible(false);
            rMenuItem.setVisible(false);
            menuSeparator1.setVisible(false);
            menuSeparator2.setVisible(false);
            menuSeparator3.setVisible(false);
            //menuSeparator4.setVisible(false);
        } else {
            deMenuItem.setVisible(true);
            downloadMenuItem.setVisible(true);
            deleteMenuItem.setVisible(true);
            rMenuItem.setVisible(true);
            menuSeparator1.setVisible(true);
            menuSeparator2.setVisible(true);
            menuSeparator3.setVisible(true);
            //menuSeparator4.setVisible(true);            
        }
        
    }

    private void initComponents() {//GEN-BEGIN:initComponents
        loginModeButtonGroup = new javax.swing.ButtonGroup();
        dataFormatButtonGroup = new javax.swing.ButtonGroup();
        connectionMethodButtonGroup = new javax.swing.ButtonGroup();
        ftpOptionsMenu = new javax.swing.JPopupMenu();
        deMenuItem = new javax.swing.JMenuItem();
        downloadMenuItem = new javax.swing.JMenuItem();
        menuSeparator1 = new javax.swing.JSeparator();
        rMenuItem = new javax.swing.JMenuItem();
        menuSeparator2 = new javax.swing.JSeparator();
        deleteMenuItem = new javax.swing.JMenuItem();
        menuSeparator3 = new javax.swing.JSeparator();
        saMenuItem = new javax.swing.JMenuItem();
        menuSeparator4 = new javax.swing.JSeparator();
        uploadMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        cndMenu = new javax.swing.JMenuItem();
        ecMenuItem = new javax.swing.JMenuItem();
        menuSeparator5 = new javax.swing.JSeparator();
        rrMenuItem = new javax.swing.JMenuItem();
        mainSplitPane = new javax.swing.JSplitPane();
        statusScrollPane = new javax.swing.JScrollPane();
        status = new javax.swing.JTextArea();
        mainPanel = new javax.swing.JPanel();
        anh = new javax.swing.JLabel();
        rch = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        protocol = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        hostAddress = new javax.swing.JTextField();
        hostPort = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        hostUsername = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        loginModePanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        normalLoginMode = new javax.swing.JRadioButton();
        anonymousLoginMode = new javax.swing.JRadioButton();
        hostPassword = new javax.swing.JPasswordField();
        controlButton1 = new javax.swing.JButton();
        controlButton2 = new javax.swing.JButton();
        advancedOptionsHider = new javax.swing.JLabel();
        hostDescription = new javax.swing.JComboBox();
        protocolLabel = new javax.swing.JLabel();
        savePassword = new javax.swing.JCheckBox();
        dataFormatLabel = new javax.swing.JLabel();
        binaryMode = new javax.swing.JRadioButton();
        asciiMode = new javax.swing.JRadioButton();
        connectionMethodLabel = new javax.swing.JLabel();
        pasvMode = new javax.swing.JRadioButton();
        portMode = new javax.swing.JRadioButton();
        browseHeaderPanel = new javax.swing.JPanel();
        goButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        activityList = new javax.swing.JComboBox();
        searchHeaderPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        searchList = new javax.swing.JComboBox();
        startLocation = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();

        deMenuItem.setText("Download & Execute");
        deMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deMenuItemActionPerformed(evt);
            }
        });

        ftpOptionsMenu.add(deMenuItem);

        downloadMenuItem.setText("Download");
        downloadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadMenuItemActionPerformed(evt);
            }
        });

        ftpOptionsMenu.add(downloadMenuItem);

        ftpOptionsMenu.add(menuSeparator1);

        rMenuItem.setText("Rename");
        rMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rMenuItemActionPerformed(evt);
            }
        });

        ftpOptionsMenu.add(rMenuItem);

        ftpOptionsMenu.add(menuSeparator2);

        deleteMenuItem.setText("Delete");
        deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMenuItemActionPerformed(evt);
            }
        });

        ftpOptionsMenu.add(deleteMenuItem);

        ftpOptionsMenu.add(menuSeparator3);

        saMenuItem.setText("Schedule Activity");
        saMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saMenuItemActionPerformed(evt);
            }
        });

        ftpOptionsMenu.add(saMenuItem);

        ftpOptionsMenu.add(menuSeparator4);

        uploadMenuItem.setText("Upload");
        uploadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadMenuItemActionPerformed(evt);
            }
        });

        ftpOptionsMenu.add(uploadMenuItem);

        ftpOptionsMenu.add(jSeparator1);

        cndMenu.setText("Create New Directory");
        cndMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cndMenuActionPerformed(evt);
            }
        });

        ftpOptionsMenu.add(cndMenu);

        ecMenuItem.setText("Enter Command");
        ecMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ecMenuItemActionPerformed(evt);
            }
        });

        ftpOptionsMenu.add(ecMenuItem);

        ftpOptionsMenu.add(menuSeparator5);

        rrMenuItem.setText("Refresh Results");
        rrMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rrMenuItemActionPerformed(evt);
            }
        });

        ftpOptionsMenu.add(rrMenuItem);

        setLayout(null);

        setDoubleBuffered(false);
        setFocusable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        mainSplitPane.setBorder(null);
        mainSplitPane.setDividerLocation(60);
        mainSplitPane.setDividerSize(4);
        mainSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setFocusable(false);
        mainSplitPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                mainSplitPanePropertyChange(evt);
            }
        });

        statusScrollPane.setBorder(null);
        status.setEditable(false);
        status.setFocusable(false);
        statusScrollPane.setViewportView(status);

        mainSplitPane.setRightComponent(statusScrollPane);

        mainPanel.setLayout(null);

        anh.setText("<HTML><U>Add New Host</U></HTML>");
        anh.setToolTipText("Allows you to add another FTP host.");
        anh.setFocusable(false);
        anh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                anhMouseClicked(evt);
            }
        });

        mainPanel.add(anh);
        anh.setBounds(15, 5, 80, 15);

        rch.setText("<HTML><U>Remove Current Host</U></HTML>");
        rch.setToolTipText("Allows you to remove this FTP host and all of its concomitant settings.");
        rch.setFocusable(false);
        rch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rchMouseClicked(evt);
            }
        });

        mainPanel.add(rch);
        rch.setBounds(110, 5, 130, 15);

        jLabel1.setText("Description");
        mainPanel.add(jLabel1);
        jLabel1.setBounds(20, 30, 110, 15);

        protocol.setEditable(true);
        protocol.setFont(new java.awt.Font("Dialog", 0, 12));
        protocol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                protocolActionPerformed(evt);
            }
        });

        mainPanel.add(protocol);
        protocol.setBounds(20, 230, 130, 22);

        jLabel2.setText("Address");
        mainPanel.add(jLabel2);
        jLabel2.setBounds(20, 70, 110, 15);

        mainPanel.add(hostAddress);
        hostAddress.setBounds(20, 85, 140, 22);

        mainPanel.add(hostPort);
        hostPort.setBounds(165, 85, 40, 22);

        jLabel3.setText("Port");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        mainPanel.add(jLabel3);
        jLabel3.setBounds(165, 70, 40, 15);

        hostUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                hostUsernameKeyReleased(evt);
            }
        });

        mainPanel.add(hostUsername);
        hostUsername.setBounds(20, 125, 110, 22);

        jLabel4.setText("Username");
        mainPanel.add(jLabel4);
        jLabel4.setBounds(20, 110, 110, 15);

        jLabel5.setText("Password");
        mainPanel.add(jLabel5);
        jLabel5.setBounds(20, 150, 110, 15);

        loginModePanel.setLayout(null);

        loginModePanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        loginModePanel.setOpaque(false);
        jLabel6.setText("Login Mode");
        jLabel6.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        loginModePanel.add(jLabel6);
        jLabel6.setBounds(7, 0, 70, 17);

        normalLoginMode.setFont(new java.awt.Font("Dialog", 0, 12));
        normalLoginMode.setText("Normal");
        loginModeButtonGroup.add(normalLoginMode);
        normalLoginMode.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        normalLoginMode.setFocusable(false);
        normalLoginMode.setOpaque(false);
        loginModePanel.add(normalLoginMode);
        normalLoginMode.setBounds(10, 15, 61, 19);

        anonymousLoginMode.setFont(new java.awt.Font("Dialog", 0, 12));
        anonymousLoginMode.setText("Anonymous");
        loginModeButtonGroup.add(anonymousLoginMode);
        anonymousLoginMode.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        anonymousLoginMode.setFocusable(false);
        anonymousLoginMode.setOpaque(false);
        anonymousLoginMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anonymousLoginModeActionPerformed(evt);
            }
        });

        loginModePanel.add(anonymousLoginMode);
        anonymousLoginMode.setBounds(10, 30, 90, 19);

        mainPanel.add(loginModePanel);
        loginModePanel.setBounds(130, 110, 100, 60);

        mainPanel.add(hostPassword);
        hostPassword.setBounds(20, 165, 110, 22);

        controlButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        controlButton1.setFocusable(false);
        controlButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                controlButton1ActionPerformed(evt);
            }
        });

        mainPanel.add(controlButton1);
        controlButton1.setBounds(15, 360, 60, 20);

        controlButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        controlButton2.setFocusable(false);
        controlButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                controlButton2ActionPerformed(evt);
            }
        });

        mainPanel.add(controlButton2);
        controlButton2.setBounds(85, 360, 60, 20);

        advancedOptionsHider.setText("<HTML><U>Advanced View</U></HTML>");
        advancedOptionsHider.setToolTipText("Hides/Un-hides the advanced settings.");
        advancedOptionsHider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                advancedOptionsHiderMouseClicked(evt);
            }
        });

        mainPanel.add(advancedOptionsHider);
        advancedOptionsHider.setBounds(10, 190, 110, 20);

        hostDescription.setEditable(true);
        hostDescription.setFont(new java.awt.Font("Dialog", 0, 12));
        hostDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostDescriptionActionPerformed(evt);
            }
        });

        mainPanel.add(hostDescription);
        hostDescription.setBounds(20, 45, 200, 22);

        protocolLabel.setText("Protocol");
        mainPanel.add(protocolLabel);
        protocolLabel.setBounds(20, 215, 50, 15);

        savePassword.setFont(new java.awt.Font("Dialog", 0, 10));
        savePassword.setText("Remember It!");
        savePassword.setFocusable(false);
        savePassword.setOpaque(false);
        mainPanel.add(savePassword);
        savePassword.setBounds(135, 165, 89, 22);

        dataFormatLabel.setText("Data Format");
        mainPanel.add(dataFormatLabel);
        dataFormatLabel.setBounds(20, 260, 70, 15);

        binaryMode.setText("Binary");
        dataFormatButtonGroup.add(binaryMode);
        binaryMode.setFocusable(false);
        binaryMode.setOpaque(false);
        mainPanel.add(binaryMode);
        binaryMode.setBounds(25, 280, 70, 15);

        asciiMode.setText("ASCII");
        dataFormatButtonGroup.add(asciiMode);
        asciiMode.setFocusable(false);
        asciiMode.setOpaque(false);
        mainPanel.add(asciiMode);
        asciiMode.setBounds(90, 280, 60, 15);

        connectionMethodLabel.setText("Connection Method");
        mainPanel.add(connectionMethodLabel);
        connectionMethodLabel.setBounds(20, 300, 110, 15);

        pasvMode.setText("PASV");
        connectionMethodButtonGroup.add(pasvMode);
        pasvMode.setFocusable(false);
        pasvMode.setOpaque(false);
        mainPanel.add(pasvMode);
        pasvMode.setBounds(25, 320, 70, 15);

        portMode.setText("PORT");
        connectionMethodButtonGroup.add(portMode);
        portMode.setFocusable(false);
        portMode.setOpaque(false);
        mainPanel.add(portMode);
        portMode.setBounds(90, 320, 60, 15);

        browseHeaderPanel.setLayout(null);

        browseHeaderPanel.setOpaque(false);
        goButton.setIcon(new javax.swing.ImageIcon(""));
        goButton.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        goButton.setFocusable(false);
        goButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goButtonActionPerformed(evt);
            }
        });

        browseHeaderPanel.add(goButton);
        goButton.setBounds(180, 0, 20, 20);

        upButton.setIcon(new javax.swing.ImageIcon(""));
        upButton.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        upButton.setFocusable(false);
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        browseHeaderPanel.add(upButton);
        upButton.setBounds(0, 0, 20, 20);

        activityList.setEditable(true);
        activityList.setFont(new java.awt.Font("Dialog", 0, 12));
        activityList.setVerifyInputWhenFocusTarget(false);
        browseHeaderPanel.add(activityList);
        activityList.setBounds(22, 0, 150, 22);

        mainPanel.add(browseHeaderPanel);
        browseHeaderPanel.setBounds(260, 0, 260, 22);

        searchHeaderPanel.setLayout(null);

        searchHeaderPanel.setOpaque(false);
        jLabel7.setText("File containing the word(s):");
        searchHeaderPanel.add(jLabel7);
        jLabel7.setBounds(0, 0, 160, 20);

        searchList.setEditable(true);
        searchList.setFont(new java.awt.Font("Dialog", 0, 12));
        searchHeaderPanel.add(searchList);
        searchList.setBounds(160, 0, 180, 22);

        searchHeaderPanel.add(startLocation);
        startLocation.setBounds(100, 24, 220, 22);

        jLabel8.setText("Within directory:");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        searchHeaderPanel.add(jLabel8);
        jLabel8.setBounds(0, 20, 100, 20);

        mainPanel.add(searchHeaderPanel);
        searchHeaderPanel.setBounds(260, 0, 260, 44);

        mainSplitPane.setLeftComponent(mainPanel);

        add(mainSplitPane);
        mainSplitPane.setBounds(10, 10, 430, 270);

    }//GEN-END:initComponents

    private void uploadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadMenuItemActionPerformed

        // Upload File OR Folder
        final javax.swing.JFileChooser jFileChooser=new javax.swing.JFileChooser();
        java.io.File selectedFile;
        
        jFileChooser.setApproveButtonText("Upload");
        jFileChooser.setDialogTitle("Browse for file OR folder...");
        jFileChooser.setFileSelectionMode(jFileChooser.FILES_AND_DIRECTORIES);
        if ( jFileChooser.showOpenDialog(this)==jFileChooser.APPROVE_OPTION ) {
            if ( (selectedFile=new java.io.File(jFileChooser.getSelectedFile().getPath())).exists() ) {
                mainFrameRef.ftpClassInstance.startFtpUpload(this, sessionID, null, controlSocket, dataSocket, selectedFile.getPath(), selectedFile.isDirectory(), currentWD, "", hostAddress.getText(), hostPort.getText(), hostUsername.getText(), new String(hostPassword.getPassword()), String.valueOf(getDataFormat()), String.valueOf(getConnectionMethod()), false, false);
                if ( selectedFile.isDirectory()==false )
                    mainFrameRef.showPanel(2);
            }
        }

    }//GEN-LAST:event_uploadMenuItemActionPerformed

    private void saMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saMenuItemActionPerformed
        if ( resultsTable.getSelectedRow()==-1 ) return;
        String selectedItem=resultsTableDataModel.getValueAt(resultsTable.getSelectedRow(),0).toString();
        if ( selectedItem.substring(0,2).equals("./") )
            selectedItem=selectedItem.substring(2);

        mainFrameRef.scheduleTab.launchScheduleDataEntry(hostDescription.getEditor().getItem().toString(),hostAddress.getText(),hostPort.getText(),hostUsername.getText(),new String(hostPassword.getPassword()),getProtocol(), String.valueOf(getDataFormat()), String.valueOf(getConnectionMethod()), currentWD+selectedItem, mainFrameRef.downloadDir,"",0,"");
        
    }//GEN-LAST:event_saMenuItemActionPerformed

    private void downloadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadMenuItemActionPerformed
        
        // Download Only
        _startFtpDownload(resultsTableDataModel.getValueAt(resultsTable.getSelectedRow(),0).toString(),resultsTableDataModel.getValueAt(resultsTable.getSelectedRow(),3).toString(),false,false);
    
    }//GEN-LAST:event_downloadMenuItemActionPerformed

    private void deleteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMenuItemActionPerformed
        
        // Delete Object
        mainFrameRef.ftpClassInstance.deleteRemoteObject(this, sessionID, controlSocket, (java.net.Socket)dataSocket, resultsTableDataModel.getValueAt(resultsTable.getSelectedRow(), 0).toString(), "", serverHeader, hostAddress.getText(), hostPort.getText(), hostUsername.getText(), new String(hostPassword.getPassword()),getDataFormat(), getConnectionMethod(), currentWD, true);
        
    }//GEN-LAST:event_deleteMenuItemActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
    }//GEN-LAST:event_jLabel3MouseClicked

    private void ecMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ecMenuItemActionPerformed
        // Enter Command
        final String inputCommand="";
        Object[] retData=new Object[2];
        String input=javax.swing.JOptionPane.showInputDialog(mainFrameRef, "", "Manual Command Entry", javax.swing.JOptionPane.DEFAULT_OPTION);

        retData=mainFrameRef.ftpClassInstance.ensureFtpControlSocketConnection(this,sessionID,controlSocket,hostAddress.getText(),hostPort.getText(),hostUsername.getText(),new String(hostPassword.getPassword()),getDataFormat(), false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            enableDisable(-1, 0);
            return;
        }
        controlSocket=(java.net.Socket)retData[0];

        if ( input!=null && input.equals("")==false ) {
            retData=mainFrameRef.ftpClassInstance.ftpSendCommand(this, sessionID, controlSocket, String.valueOf(inputCommand).toUpperCase()+input, true, 2);
            if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                enableDisable(-1, 0);
                return;
            }
            
        }
        
    }//GEN-LAST:event_ecMenuItemActionPerformed

    private void rrMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rrMenuItemActionPerformed

        // Refresh Results
        mainFrameRef.ftpClassInstance.displaydir(this, sessionID, controlSocket, dataSocket, false, true, false);
        
    }//GEN-LAST:event_rrMenuItemActionPerformed

    private void cndMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cndMenuActionPerformed
        // Create Directory
        final String inputCommand="MKD ";
        Object[] retData=new Object[2];
        String input=javax.swing.JOptionPane.showInputDialog(mainFrameRef, "Desired name:", "Create New Directory", javax.swing.JOptionPane.DEFAULT_OPTION);

        retData=mainFrameRef.ftpClassInstance.ensureFtpControlSocketConnection(this,sessionID,controlSocket,hostAddress.getText(),hostPort.getText(),hostUsername.getText(),new String(hostPassword.getPassword()),getDataFormat(), false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            enableDisable(-1, 0);
            return;
        }
        controlSocket=(java.net.Socket)retData[0];

        if ( input!=null && input.equals("")==false ) {
            retData=mainFrameRef.ftpClassInstance.ftpSendCommand(this, sessionID, controlSocket, String.valueOf(inputCommand).toUpperCase()+input, true, 2);
            if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                enableDisable(-1, 0);
                return;
            }
            
            mainFrameRef.ftpClassInstance.displaydir(this, sessionID, controlSocket, dataSocket, false, true, false);
        }

    }//GEN-LAST:event_cndMenuActionPerformed

    private void rMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rMenuItemActionPerformed

        // Rename Object
        new Thread(new sb_renameremoteobject(mainFrameRef,this,sessionID)).start();

    }//GEN-LAST:event_rMenuItemActionPerformed

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        
    }//GEN-LAST:event_jLabel8MouseClicked

    private void deMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deMenuItemActionPerformed

        // Download and Execute
        _startFtpDownload(resultsTableDataModel.getValueAt(resultsTable.getSelectedRow(),0).toString(),resultsTableDataModel.getValueAt(resultsTable.getSelectedRow(),3).toString(),false,true);
        
    }//GEN-LAST:event_deMenuItemActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        new Thread(new ceefee.display.sb_upbuttonactionperformed(mainFrameRef,this,sessionID)).start();
    }//GEN-LAST:event_upButtonActionPerformed

    private void mainSplitPanePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_mainSplitPanePropertyChange
        organize();
    }//GEN-LAST:event_mainSplitPanePropertyChange

    private void advancedOptionsHiderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_advancedOptionsHiderMouseClicked
        if ( advancedOptionsHider.isEnabled() )
            hideUnhide(false);
        
    }//GEN-LAST:event_advancedOptionsHiderMouseClicked

    private void controlButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_controlButton2ActionPerformed
        endSession(false);

    }//GEN-LAST:event_controlButton2ActionPerformed

    private void hostUsernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hostUsernameKeyReleased

        if ( hostUsername.getText().toLowerCase().equals("anonymous") ) {
            hostPassword.setText("anon@");
            anonymousLoginMode.setSelected(true);
        } else {
            if ( normalLoginMode.isSelected()==false ) {
                hostPassword.setText("");
                normalLoginMode.setSelected(true);
            }
        }
        
    }//GEN-LAST:event_hostUsernameKeyReleased

    private void anonymousLoginModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anonymousLoginModeActionPerformed
        if ( anonymousLoginMode.isSelected() ) {
            hostUsername.setText("anonymous");
            hostPassword.setText("anon@");
        }
        
    }//GEN-LAST:event_anonymousLoginModeActionPerformed

    private void rchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rchMouseClicked
        if ( hostDescription.getItemCount()==0 || controlButton1.isEnabled()==false ) return ;

        int index;
        Object[] retData=new Object[2];
        

        mainFrameRef.fileioClassInstance.removeDataFromFile("_hosts.dat","ftpserver",String.valueOf(hostDescription.getSelectedIndex()),true);
        if ( scourMethod==mainFrameRef.SEARCH_METHOD ) {
            mainFrameRef.fileioClassInstance.removeDataFromFile("_hosts.dat","searchlisthistory",String.valueOf(hostDescription.getSelectedIndex()),true);
            mainFrameRef.fileioClassInstance.removeDataFromFile("_hosts.dat","searchstartlocationhistory",String.valueOf(hostDescription.getSelectedIndex()),true);
        } else if ( scourMethod==mainFrameRef.BROWSE_METHOD ) {
            mainFrameRef.fileioClassInstance.removeDataFromFile("_hosts.dat","activitylisthistorytrail",String.valueOf(hostDescription.getSelectedIndex()),true);
        }
        index=(Integer.parseInt(mainFrameRef.fileioClassInstance.getDataFromFile("_hosts.dat", "lastserver", false)[0].toString())-1);
        if ( index==-1 ) index=0;
        mainFrameRef.fileioClassInstance.storeDataToFile("_hosts.dat", "lastserver", "", String.valueOf(index), 1, 1, "", false);
            
        retData[0]=String.valueOf(mainFrameRef.utilitiesClassInstance.findItemInList("", hostDescription));
        if ( (retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1")==false) )
            hostDescription.removeItem("");
        
        hostDescription.removeItem(hostDescription.getSelectedItem());
        
        if ( hostDescription.getItemCount()==0 ) {
            clearFtpFields();
        } else {
            refreshFtpSettings();
        }

        hostDescription.requestFocusInWindow();

    }//GEN-LAST:event_rchMouseClicked

    private void anhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_anhMouseClicked
        if ( controlButton1.isEnabled()==false ) return;
        
        
        Object[] retData=new Object[2];
        retData[0]=String.valueOf(mainFrameRef.utilitiesClassInstance.findItemInList("", hostDescription));
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            hostDescription.addItem("");
            hostDescription.requestFocusInWindow();
            lastValidServerIndex=hostDescription.getItemCount()-1;
        } else {
            try {
                lastValidServerIndex=Integer.parseInt(retData[0].toString());
            } catch ( java.lang.NumberFormatException nfe ) {}
        }
        clearPreviousSessionVestige();
        clearFtpFields();

    }//GEN-LAST:event_anhMouseClicked

    private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goButtonActionPerformed
        _sb_activitylistactionperformed();
    }//GEN-LAST:event_goButtonActionPerformed

    private void hostDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostDescriptionActionPerformed
        if ( hostDescription.getSelectedIndex()==-1 ) return;
        if ( hostDescriptionLock==true ) return;


        if ( hostDescription.getSelectedItem().toString().equals("") ) {
            hostAddress.setText("");
            if ( tabIndex==0 ) {
                    lastValidServerIndex=hostDescription.getItemCount()-1;
            }
        } else {
            loadFtpBrowseSettings(hostDescription.getSelectedIndex());
            if ( tabIndex==0 ) {
                    lastValidServerIndex=hostDescription.getSelectedIndex();
            }
        }

    }//GEN-LAST:event_hostDescriptionActionPerformed

    private void protocolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_protocolActionPerformed
        if ( (scourMethod==mainFrameRef.SEARCH_METHOD && (mainFrameRef.searchTabPane.getSelectedIndex()!=-1) && (mainFrameRef.searchTabPane.getSelectedIndex()!=tabIndex)) || (scourMethod==mainFrameRef.BROWSE_METHOD && (mainFrameRef.browseTabPane.getSelectedIndex()!=-1) && (mainFrameRef.browseTabPane.getSelectedIndex()!=tabIndex)) ) return;
        if ( protocolLock ) return;

        
        mainFrameRef.securityLayerLabel.setText(protocol.getSelectedItem().toString());
        for ( int securityLayerLooper=-1; ++securityLayerLooper<mainFrameRef.securityLayer.length; ) {
            if ( mainFrameRef.securityLayerLabel.getText().equals(mainFrameRef.securityLayer[securityLayerLooper]) ) {
                hostPort.setText(mainFrameRef.securityLayerDefaultPort[securityLayerLooper]);
                if ( mainFrameRef.securityLayer[securityLayerLooper].equals("Standard FTP")==false )
                    mainFrameRef.securityLayerLabel.setIcon(mainFrameRef.lockIcon);
            } else if ( securityLayerLooper==(mainFrameRef.securityLayer.length-1) ) {
                hostPort.setText(mainFrameRef.securityLayerDefaultPort[0]); // Standard FTP
                mainFrameRef.securityLayerLabel.setText("");
                mainFrameRef.securityLayerLabel.setIcon(null);
            }
        }
        
        mainFrameRef.securityLayerLabel.setSize((mainFrameRef.securityLayerLabel.getText().length()*8)+(mainFrameRef.sideMargin*2),mainFrameRef.securityLayerLabel.getHeight());
        mainFrameRef.organize(scourMethod,tabIndex,false);

    }//GEN-LAST:event_protocolActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        organize();
    }//GEN-LAST:event_formComponentResized

    private void controlButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_controlButton1ActionPerformed

        if ( controlButton1.isEnabled()==false || ((scourMethod==mainFrameRef.SEARCH_METHOD && tabIndex!=mainFrameRef.searchTabPane.getSelectedIndex()) || (scourMethod==mainFrameRef.BROWSE_METHOD && tabIndex!=mainFrameRef.browseTabPane.getSelectedIndex())) )
            return;
        
        if ( scourMethod==mainFrameRef.SEARCH_METHOD )
            login(false,false,false);
        else if ( scourMethod==mainFrameRef.BROWSE_METHOD )
            login(false,true,true);
        
    }//GEN-LAST:event_controlButton1ActionPerformed

    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
    }//GEN-LAST:event_exitForm

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JComboBox activityList;
    private javax.swing.JLabel advancedOptionsHider;
    private javax.swing.JLabel anh;
    private javax.swing.JRadioButton anonymousLoginMode;
    private javax.swing.JRadioButton asciiMode;
    private javax.swing.JRadioButton binaryMode;
    private javax.swing.JPanel browseHeaderPanel;
    private javax.swing.JMenuItem cndMenu;
    private javax.swing.ButtonGroup connectionMethodButtonGroup;
    private javax.swing.JLabel connectionMethodLabel;
    public javax.swing.JButton controlButton1;
    public javax.swing.JButton controlButton2;
    private javax.swing.ButtonGroup dataFormatButtonGroup;
    private javax.swing.JLabel dataFormatLabel;
    private javax.swing.JMenuItem deMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenuItem downloadMenuItem;
    private javax.swing.JMenuItem ecMenuItem;
    public javax.swing.JPopupMenu ftpOptionsMenu;
    public javax.swing.JButton goButton;
    public javax.swing.JTextField hostAddress;
    public javax.swing.JComboBox hostDescription;
    public javax.swing.JPasswordField hostPassword;
    public javax.swing.JTextField hostPort;
    public javax.swing.JTextField hostUsername;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.ButtonGroup loginModeButtonGroup;
    private javax.swing.JPanel loginModePanel;
    private javax.swing.JPanel mainPanel;
    public javax.swing.JSplitPane mainSplitPane;
    private javax.swing.JSeparator menuSeparator1;
    private javax.swing.JSeparator menuSeparator2;
    private javax.swing.JSeparator menuSeparator3;
    private javax.swing.JSeparator menuSeparator4;
    private javax.swing.JSeparator menuSeparator5;
    private javax.swing.JRadioButton normalLoginMode;
    public javax.swing.JRadioButton pasvMode;
    public javax.swing.JRadioButton portMode;
    public javax.swing.JComboBox protocol;
    private javax.swing.JLabel protocolLabel;
    private javax.swing.JMenuItem rMenuItem;
    private javax.swing.JLabel rch;
    private javax.swing.JMenuItem rrMenuItem;
    private javax.swing.JMenuItem saMenuItem;
    private javax.swing.JCheckBox savePassword;
    private javax.swing.JPanel searchHeaderPanel;
    public javax.swing.JComboBox searchList;
    public javax.swing.JTextField startLocation;
    private javax.swing.JTextArea status;
    private javax.swing.JScrollPane statusScrollPane;
    public javax.swing.JButton upButton;
    private javax.swing.JMenuItem uploadMenuItem;
    // End of variables declaration//GEN-END:variables

    void loadFtpBrowseSettings(final int refVal ) {

        if ( refVal>hostDescription.getItemCount()-1 ) return ;

        java.util.StringTokenizer strTok=null;
        String tBuffer;
        Object[] retData=new Object[2];


        protocol.setSelectedItem(ftpServer[refVal][1]);

        hostUsername.setText(ftpServer[refVal][4]);
        if ( hostUsername.getText().toLowerCase().equals("anonymous") ) {
            anonymousLoginMode.setSelected(true);
            hostPassword.setText("anon@");
        } else {
            normalLoginMode.setSelected(true);
            hostPassword.setText(ftpServer[refVal][5]);
        }

        savePasswordLock=true;
        if ( String.valueOf(hostPassword.getPassword()).equals("") ) {
            savePassword.setSelected(false);
        } else {
            savePassword.setSelected(true);
        }
        savePasswordLock=false;

        hostAddress.setText(ftpServer[refVal][2]);

        hostPort.setText(ftpServer[refVal][3]);

        if ( ftpServer[refVal][6].equals("0") )
            binaryMode.setSelected(true);
        else
            asciiMode.setSelected(true);

        if ( ftpServer[refVal][7].equals("0") )
            pasvMode.setSelected(true);
        else
            portMode.setSelected(true);
        
        if ( scourMethod==mainFrameRef.SEARCH_METHOD ) {
            strTok=new java.util.StringTokenizer(searchListHistory[refVal], "|");
            
            searchList.removeAllItems();

            try {
                do {            
                    tBuffer=strTok.nextToken();
                      
                    retData[0]=String.valueOf(mainFrameRef.utilitiesClassInstance.findItemInList(tBuffer, searchList));
                    if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                        searchList.addItem(tBuffer);
                    }
                } while ( strTok.countTokens()!=0 );
            } catch ( java.util.NoSuchElementException nsee ) {}

            searchList.setSelectedIndex(searchList.getItemCount()-1);

            startLocation.setText(searchStartLocationHistory[refVal]);
            
        } else if ( scourMethod==mainFrameRef.BROWSE_METHOD ) {
            strTok=new java.util.StringTokenizer(activityListHistoryTrail[refVal], "|");
            activityListLock=true;
            activityList.removeAllItems();
            activityList.addItem(".");
            activityList.addItem("..");
            
            try {
                do {
                    tBuffer=strTok.nextToken();
                      
                    retData[0]=String.valueOf(mainFrameRef.utilitiesClassInstance.findItemInList(tBuffer, activityList));
                    if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                        activityList.addItem(tBuffer);
                    }
                } while ( strTok.countTokens()!=0 );
            } catch ( java.util.NoSuchElementException nsee ) {}

            if ( activityList.getItemCount()>2 ) {
                activityList.setSelectedIndex(activityList.getItemCount()-1);
            } else {
                activityList.setSelectedItem("");
            }

            activityListLock=false;
            
        }

    }
    
    final void _startFtpDownload( final String remoteFileName, final String remoteFilePath, final boolean overrideOverwriteConfirmation, final boolean exec ) {
        final boolean isFolder=mainFrameRef.utilitiesClassInstance.isFolderFormatting(remoteFileName);
        
        mainFrameRef.ftpClassInstance.startFtpDownload(this, sessionID, null, controlSocket, dataSocket, remoteFileName, remoteFilePath, isFolder, "", mainFrameRef.downloadDir, resultsTableDataModel.getValueAt(resultsTable.getSelectedRow(),4).toString(), serverHeader, hostAddress.getText(), hostPort.getText(), hostUsername.getText(), new String(hostPassword.getPassword()), String.valueOf(getDataFormat()), String.valueOf(getConnectionMethod()), overrideOverwriteConfirmation, exec);
        if ( isFolder==false )
            mainFrameRef.showPanel(2);
        
    }
    
    void createResultsTable() {
        ceefee.gui.TableSorter dataSorter;

        resultsTableColumnData.add("Resource");
        resultsTableColumnData.add("Size");
        resultsTableColumnData.add("Date Modified");
        resultsTableColumnData.add("");
        resultsTableColumnData.add("");
        resultsTableDataModel=new javax.swing.table.DefaultTableModel(null, resultsTableColumnData) {
            public boolean isCellEditable(int row,int col) {
                return false;
            }
        };
       
        dataSorter=new ceefee.gui.TableSorter(resultsTableDataModel,mainFrameRef.displayClassInstance);
        resultsTable=new ceefee.gui.dnd.jtable(mainFrameRef,dataSorter);
        resultsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resultsTableMouseClicked(evt);
            }
        });
        resultsTable.setRowHeight(resultsTable.getRowHeight()+1);
        resultsTable.setDragEnabled(true);
        resultsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        dataSorter.addMouseListenerToHeaderInTable(resultsTable);
        resultsTable.setShowGrid(false);
        resultsTable.setIntercellSpacing(mainFrameRef.zeroDimension);
        resultsTable.setBackground(mainFrameRef.default4);
        resultsTable.setAutoscrolls(true);
        resultsTable.setAutoResizeMode(resultsTable.AUTO_RESIZE_OFF);
        resultsTable.getColumnModel().getColumn(0).setCellRenderer(resultsTableDataCellRendererInstance);
        resultsTable.removeColumn(resultsTable.getColumnModel().getColumn(4));
        resultsTable.removeColumn(resultsTable.getColumnModel().getColumn(3));
        
        resultsTableScrollPane=new ceefee.gui.dnd.jscrollpane(mainFrameRef);
        resultsTableScrollPane.getViewport().setBackground(mainFrameRef.default4);
        resultsTableScrollPane.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        resultsTableScrollPane.setOpaque(false);
        resultsTableScrollPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resultsTableMouseClicked(evt);
            }
        });
        resultsTableScrollPane.setViewportView(resultsTable);

    }
    void setResultsTableInitialColumnsWidths() {
        if ( resultsTableScrollPane.getWidth()<1 ) return;
        
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth((int)(resultsTableScrollPane.getWidth()*mainFrameRef.firstColumnPercentage));
        resultsTable.getColumnModel().getColumn(1).setPreferredWidth((int)(resultsTableScrollPane.getWidth()*mainFrameRef.secondColumnPercentage));
        resultsTable.getColumnModel().getColumn(2).setPreferredWidth((int)(resultsTableScrollPane.getWidth()*mainFrameRef.thirdColumnPercentage));
                
        resultsTable.setAutoResizeMode(resultsTable.AUTO_RESIZE_ALL_COLUMNS);
        
        resultsTableInitialColumnsWidthsSet=true;
    }
    
    public void enableDisable( final int option, final int setBusy ) {

        if ( option==0 ) {
            anh.setEnabled(true);
            rch.setEnabled(true);
            hostDescription.setEnabled(true);
            protocol.setEnabled(true);
            hostAddress.setEnabled(true);
            binaryMode.setEnabled(true);
            asciiMode.setEnabled(true);
            pasvMode.setEnabled(true);
            portMode.setEnabled(true);
            hostPort.setEnabled(true);
            hostUsername.setEnabled(true);
            hostPassword.setEnabled(true);
            advancedOptionsHider.setEnabled(true);
            savePasswordLock=true;
            savePassword.setEnabled(true);
            savePasswordLock=false;
            normalLoginMode.setEnabled(true);
            anonymousLoginMode.setEnabled(true);
            controlButton1.setEnabled(true);

            controlButton2.setEnabled(false);
            resultsTable.setEnabled(false);
            resultsTable.setForeground(mainFrameRef.darkGray3);
            resultsTableScrollPane.setEnabled(false);
            status.setEnabled(false);
            if ( scourMethod==mainFrameRef.SEARCH_METHOD ) {
                searchList.setEnabled(true);
                startLocation.setEnabled(true);
            }
            goButton.setEnabled(false);
            upButton.setEnabled(false);
        } else if ( option==1 ) {
            anh.setEnabled(false);
            rch.setEnabled(false);
            hostDescription.setEnabled(false);
            protocol.setEnabled(false);
            hostAddress.setEnabled(false);
            binaryMode.setEnabled(false);
            asciiMode.setEnabled(false);
            portMode.setEnabled(false);
            pasvMode.setEnabled(false);
            hostPort.setEnabled(false);
            hostUsername.setEnabled(false);
            savePasswordLock=true;
            savePassword.setEnabled(false);
            savePasswordLock=false;
            normalLoginMode.setEnabled(false);
            anonymousLoginMode.setEnabled(false);
            hostPassword.setEnabled(false);
            advancedOptionsHider.setEnabled(false);
            controlButton1.setEnabled(false);
            controlButton2.setEnabled(true);
            if ( scourMethod==mainFrameRef.SEARCH_METHOD ) {
                searchList.setEnabled(false);
                startLocation.setEnabled(false);
            }
            
        } else if ( option==2 ) {
            controlButton2.setEnabled(true);
            resultsTable.setEnabled(true);
            resultsTable.setForeground(java.awt.Color.black);
            resultsTableScrollPane.setEnabled(true);
            status.setEnabled(true);
            goButton.setEnabled(true);
            upButton.setEnabled(true);
        }
            
        if ( setBusy==1 ) {

            setCursor(mainFrameRef.waitCursor);
            busy=true;
        } else if ( setBusy==0 ) {
                
            setCursor(mainFrameRef.defaultCursor);
            busy=false;
        }

    }
        
    public void saveSiteData(final String protocol, final String siteDescription, final String siteAddress, final String sitePort, final String siteUsername, final String sitePassword, final String siteDataFormat, final String siteConnectionMethod, final String groupNumber) {
        if ( siteDescription.equals("") ) return ;

        mainFrameRef.fileioClassInstance.storeDataToFile("_hosts.dat", "ftpserver", "", siteDescription, 1, 9, groupNumber, false);
        mainFrameRef.fileioClassInstance.storeDataToFile("_hosts.dat", "ftpserver", "", protocol, 2, 9, groupNumber, false);
        mainFrameRef.fileioClassInstance.storeDataToFile("_hosts.dat", "ftpserver", "", siteAddress, 3, 9, groupNumber, false);
        mainFrameRef.fileioClassInstance.storeDataToFile("_hosts.dat", "ftpserver", "", sitePort, 4, 9, groupNumber, false);
        mainFrameRef.fileioClassInstance.storeDataToFile("_hosts.dat", "ftpserver", "", siteUsername, 5, 9, groupNumber, false);
        if ( savePassword.isSelected() ) {
            mainFrameRef.fileioClassInstance.storeDataToFile("_hosts.dat", "ftpserver", "", sitePassword, 6, 9, groupNumber, false);
        } else {
            mainFrameRef.fileioClassInstance.storeDataToFile("_hosts.dat", "ftpserver", "", "", 6, 9, groupNumber, false);
        }
    
        mainFrameRef.fileioClassInstance.storeDataToFile("_hosts.dat", "ftpserver", "", siteDataFormat, 7, 9, groupNumber, false);
        mainFrameRef.fileioClassInstance.storeDataToFile("_hosts.dat", "ftpserver", "", siteConnectionMethod, 8, 9, groupNumber, false);

        mainFrameRef.fileioClassInstance.storeDataToFile("_hosts.dat", "lastserver", "", String.valueOf(groupNumber), 1, 1, "", false);
        
    }
    
    public int getConnectionMethod() {
        if ( pasvMode.isSelected() )
            return 0; //PASV";
        else
            return 1; //"PORT";
    }
    
    public int getDataFormat() {
        if ( binaryMode.isSelected() )
            return 0; //"Binary";
        else
            return 1; //"ASCII";    
    }

    public void organize() {
        
        if ( isEnabled()==false )
            return;
        else
            setEnabled(false);
        
        mainSplitPane.setSize(getWidth(), getHeight());

        if ( scourMethod==mainFrameRef.SEARCH_METHOD ) { //search
            searchHeaderPanel.setVisible(true);
            searchHeaderPanel.setSize(getWidth()-searchHeaderPanel.getX(), searchHeaderPanel.getHeight());
            browseHeaderPanel.setVisible(false);
            resultsTableScrollPane.setLocation(resultsTableScrollPane.getX(), searchHeaderPanel.getHeight());
            searchList.setSize(searchHeaderPanel.getWidth()-searchList.getX(), 22);
            startLocation.setSize(searchHeaderPanel.getWidth()-startLocation.getX(), 20);
        } else if ( scourMethod==mainFrameRef.BROWSE_METHOD ) {
            searchHeaderPanel.setVisible(false);
            browseHeaderPanel.setSize(getWidth()-browseHeaderPanel.getX(), browseHeaderPanel.getHeight());
            browseHeaderPanel.setVisible(true);
            resultsTableScrollPane.setLocation(resultsTableScrollPane.getX(), browseHeaderPanel.getHeight());
            activityList.setSize(browseHeaderPanel.getWidth()-activityList.getX()-2-2-goButton.getWidth(), 22);
            goButton.setLocation(activityList.getX()+activityList.getWidth()+2, 0);
        }
        
        resultsTableScrollPane.setSize(getWidth()-resultsTableScrollPane.getX(), mainSplitPane.getTopComponent().getHeight()-resultsTableScrollPane.getY());
        if ( resultsTableInitialColumnsWidthsSet==false )
            setResultsTableInitialColumnsWidths();
        
        setEnabled(true);
        validate();
        
    }

    public String getProtocol() {
        return protocol.getSelectedItem().toString();
    }

    public void refreshFtpSettings() {
        java.util.StringTokenizer strtok;
        String ftpData;
        java.util.Vector ftpServerBuffer1=new java.util.Vector(0,1);
        String ftpServerBuffer2[][]={};
        String ftpServerHistoryTrailBuffer1[]={};
        String ftpServerSearchStartLocationBuffer[]={};

        int ftpServerListLoop;
        int ftpServerHistoryTrailLoop;



        lastValidServerIndex=(Integer.parseInt(mainFrameRef.fileioClassInstance.getDataFromFile("_hosts.dat", "lastserver", false)[0].toString()));

        hostDescriptionLock=true;
        hostDescription.setEnabled(false);
        hostDescription.removeAllItems();

        ftpServerListLoop=-1;
        while ( (ftpData=mainFrameRef.fileioClassInstance.getDataFromFile("_hosts.dat", "ftpserver" + (++ftpServerListLoop), false)[0].toString()).equals("")==false ) {
            ftpServerBuffer1.add(ftpData);
        }

        // Search
        ftpServerBuffer2=new String[ftpServerBuffer1.size()][9];
        ftpServerHistoryTrailBuffer1=new String[ftpServerBuffer1.size()];
        ftpServerSearchStartLocationBuffer=new String[ftpServerBuffer1.size()];
        
        for ( ftpServerListLoop=0;ftpServerListLoop<ftpServerBuffer1.size();ftpServerListLoop++ ) {
            strtok=new java.util.StringTokenizer(String.valueOf(ftpServerBuffer1.get(ftpServerListLoop)),"|");
            for ( int ftpServerDataItemLooper=-1; ++ftpServerDataItemLooper<9; ) {
                try {
                    ftpServerBuffer2[ftpServerListLoop][ftpServerDataItemLooper]=strtok.nextToken();
                } catch ( java.util.NoSuchElementException nsee ) {
                    ftpServerBuffer2[ftpServerListLoop][ftpServerDataItemLooper]="";
                }
            }
            hostDescription.addItem(ftpServerBuffer2[ftpServerListLoop][0]);
            if ( (ftpServerListLoop==lastValidServerIndex) && (scourMethod==mainFrameRef.SEARCH_METHOD) )
                hostDescription.setSelectedIndex(lastValidServerIndex);
            else if ( (ftpServerListLoop==lastValidServerIndex) && (scourMethod==mainFrameRef.BROWSE_METHOD) )
                hostDescription.setSelectedIndex(lastValidServerIndex);
            
            ftpServerHistoryTrailBuffer1[ftpServerListLoop]=mainFrameRef.fileioClassInstance.getDataFromFile("_hosts.dat", "searchlisthistory"+ftpServerListLoop, false)[0].toString();
            ftpServerSearchStartLocationBuffer[ftpServerListLoop]=mainFrameRef.fileioClassInstance.getDataFromFile("_hosts.dat", "searchstartlocationhistory"+ftpServerListLoop, false)[0].toString();
        }
        hostDescription.setEnabled(true);
        hostDescriptionLock=false;
        ftpServer=ftpServerBuffer2;
        searchListHistory=ftpServerHistoryTrailBuffer1;
        searchStartLocationHistory=ftpServerSearchStartLocationBuffer;

        // Browse
        ftpServerBuffer2=new String[ftpServerBuffer1.size()][9];
        ftpServerHistoryTrailBuffer1=new String[ftpServerBuffer1.size()];

        for ( ftpServerListLoop=0;ftpServerListLoop<ftpServerBuffer1.size();ftpServerListLoop++ ) {
            //strtok=new java.util.StringTokenizer(ftpServerBuffer1[ftpServerListLoop],"|");
            //for ( int ftpServerDataItemLooper=-1; ++ftpServerDataItemLooper<9; ) {
            //    try {
            //        ftpServerBuffer2[ftpServerListLoop][ftpServerDataItemLooper]=strtok.nextToken();
            //    } catch ( java.util.NoSuchElementException nsee ) {
            //        ftpServerBuffer2[ftpServerListLoop][ftpServerDataItemLooper]="";
            //    }
            //}
            //hostDescription.addItem(ftpServerBuffer2[ftpServerListLoop][0]);
            ftpServerHistoryTrailBuffer1[ftpServerListLoop]=mainFrameRef.fileioClassInstance.getDataFromFile("_hosts.dat", "activitylisthistorytrail"+ftpServerListLoop, false)[0].toString();
        }
        //ftpServer=ftpServerBuffer2;
        activityListHistoryTrail=ftpServerHistoryTrailBuffer1;

//        if ( hostDescription.getItemCount()!=0 ) {
//            if ( hostDescription.isEnabled() ) {
//                if ( (scourMethod==mainFrameRef.SEARCH_METHOD && lastValidSearchServerIndex!=-1) || (scourMethod==mainFrameRef.BROWSE_METHOD && lastValidBrowseServerIndex!=-1) ) {
//                    hostDescription.setSelectedIndex(hostDescription.getItemCount()-1);
//                }
//            }
//        }

        //if ( browseTabIndex==0 )
        if ( scourMethod==mainFrameRef.SEARCH_METHOD )
            loadFtpBrowseSettings(lastValidServerIndex);
        else if ( scourMethod==mainFrameRef.BROWSE_METHOD )
            loadFtpBrowseSettings(lastValidServerIndex);
        //else
        //    sbDefaultTabInstance[browseTabIndex].loadFtpBrowseSettings(0, browseTabIndex);
    }

    void resetactivityList() {
        activityListLock=true;
        activityList.removeAllItems();
        activityList.addItem(".");
        activityList.addItem("..");
        activityListLock=false;
    }

    private void clearFtpFields() {
        hostDescriptionLock=true;
        hostDescription.setSelectedItem("");
        hostDescriptionLock=false;
        hostAddress.setText("");
        resetactivityList();
    }
    
    private void clearPreviousSessionVestige() {
        status.setText("");
        resultsTableDataModel.setRowCount(0);
    }
    
    public String isBrowseAdvancedViewShown() {
        return String.valueOf((boolean)(advancedOptionsHider.getIcon()==mainFrameRef.rightArrow));
    }
            
    public void hideUnhide( final boolean forceHide ) {

        if ( forceHide==false && advancedOptionsHider.getIcon()==mainFrameRef.downArrow ) {
            advancedOptionsHider.setIcon(mainFrameRef.rightArrow);
            protocolLabel.setSize(60,16);
            protocol.setSize(protocol.getWidth(), 20);
            protocol.validate();
            dataFormatLabel.setSize(70, 16);
            binaryMode.setSize(70, 15);
            asciiMode.setSize(60, 15);
            connectionMethodLabel.setSize(110, 16);
            pasvMode.setSize(70, 15);
            portMode.setSize(60, 15);
            
            controlButton1.setLocation(controlButton1.getX(), 360);
            controlButton2.setLocation(controlButton2.getX(), 360);
        } else {
            advancedOptionsHider.setIcon(mainFrameRef.downArrow);
            protocolLabel.setSize(60, 0);
            protocol.setSize(protocol.getWidth(), 0);
            dataFormatLabel.setSize(70, 0);
            binaryMode.setSize(70, 0);
            asciiMode.setSize(60, 0);
            connectionMethodLabel.setSize(110, 0);
            pasvMode.setSize(70, 0);
            portMode.setSize(60, 0);
            
            controlButton1.setLocation(controlButton1.getX(), 280);
            controlButton2.setLocation(controlButton2.getX(), 280);
        }
        organize();
        
    }
    
    public void addTextToStatListing( final int _sessionID, String textData, final int command0reply1, final int numOfLines, final boolean replaceLastLine ) {
        if ( _sessionID!=sessionID ) return;
        if ( numOfLines==0 ) return;
        
        final int MAX_NUM_OF_LINES                       =0xf00; // gives a fair amount of data, uses fair amount of resources
        int startOfLastLine=0;
        int lineLooper;
        String separator;
        
        
        if ( textData.indexOf("\r\n")!=-1 )
            separator="\r\n";
        else
            separator="\n";
        
        if ( textData.length()>separator.length() && textData.substring(textData.length()-separator.length()).equals(separator) )
            textData=textData.substring(0,textData.length()-separator.length());

        if ( textData.indexOf(separator)!=-1 ) {
            addTextToStatListing(_sessionID,textData.substring(0,textData.indexOf(separator)),command0reply1,numOfLines,false);
            addTextToStatListing(_sessionID,textData.substring(textData.indexOf(separator)+separator.length()),command0reply1,numOfLines,false);
            return;
        }
        
        if ( replaceLastLine ) {
            status.setText(status.getText().substring(0,status.getText().lastIndexOf(System.getProperty("line.separator"))));
        }

        if ( command0reply1==0 ) {
            status.setText(status.getText() + System.getProperty("line.separator") + "   " + "           " + "           " + textData);
        } else if ( command0reply1==1 ) {
            status.setText(status.getText() + System.getProperty("line.separator") + "  -        " + textData);
        }

        for ( lineLooper=0; ++lineLooper<numOfLines; ) {
            status.setText(status.getText() + System.getProperty("line.separator"));// + "           " + "           ");
        }

        for ( lineLooper=0; status.getText().length()>MAX_NUM_OF_LINES; ) {
            status.setText(status.getText().substring(status.getText().indexOf(System.getProperty("line.separator"))+System.getProperty("line.separator").length()));
        }
        
        try {
            startOfLastLine=status.getLineStartOffset(status.getLineCount()-1);
        } catch ( javax.swing.text.BadLocationException ble ) {}
        status.select(startOfLastLine,startOfLastLine);
 
    }
    
    public void login( final boolean wait, final boolean establishWorkingDirectory, final boolean displayDirectory ) {
        enableDisable(1,1);

        hostDescription.removeItem(""); // if user decided to change his/her mind about adding a new host

        clearPreviousSessionVestige();
        
        establishFtpConnectionInstance=new ceefee.ftp.establishftpconnection(mainFrameRef,this,sessionID,establishWorkingDirectory,displayDirectory);
        establishFtpConnectionInstanceThread=new Thread(establishFtpConnectionInstance);
        establishFtpConnectionInstanceThread.start();
        if ( wait ) {
            try {
                establishFtpConnectionInstanceThread.join();
            } catch ( java.lang.InterruptedException ie) {}
        }
        
    }

    void resultsTableMouseClicked(final java.awt.event.MouseEvent evt) {
        if ( busy || resultsTable.isEnabled()==false ) return;

        new Thread(new ceefee.display.sb_resultstablemouseclicked(mainFrameRef,this,evt)).start();
        
    }
    

    public void endSession( final boolean wait ) {

        ceefee.display.sb_cancelactivity cancelActivityInstance;
        Thread cancelActivityInstanceThread;

        if ( controlButton2.isEnabled()==false )
            return;

        sessionID=sessionID+1;
        if ( sessionID==Integer.MAX_VALUE )
            sessionID=(tabIndex*10)+1;

        if ( controlSocket!=null && controlSocket.isConnected() ) {
            mainFrameRef.ftpClassInstance.ftpSendCommand(this,sessionID, controlSocket, "QUIT", true, 0);
            try { controlSocket.close(); } catch ( java.io.IOException ioe ) {};
        }
        if ( dataSocket!=null ) {
            dataSocket=null;
        }

        resultsTableAccentedCells=null;
        iffyConnection=false;

        cancelActivityInstance=new ceefee.display.sb_cancelactivity(mainFrameRef,this);
        cancelActivityInstanceThread=new Thread(cancelActivityInstance);
        cancelActivityInstanceThread.start();
        if ( wait ) {
            try {
                cancelActivityInstanceThread.join();
            } catch ( java.lang.InterruptedException ie ) {}
        }

    }
}