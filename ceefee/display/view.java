package ceefee.display;


public class view extends javax.swing.JPanel {
    
    private ceefee.main mainFrameRef;

    // Table Display
    private final String[] transferTableColumnData={"Resource", "Status", "Velocity"};
    
    private javax.swing.table.DefaultTableModel uploadsTableDataModel;
    
    private javax.swing.table.DefaultTableModel downloadsTableDataModel;    
           

    
    public view( final ceefee.main _mainFrameRef, final int horizontalSplitPaneDividerLocation ) {
        mainFrameRef=_mainFrameRef;

        initComponents();

        horizontalSplitPane.setDividerLocation(horizontalSplitPaneDividerLocation);

        downloadsPanelFooterPanel.setBackground(mainFrameRef.default4);
        downloadsPanelFooterLabel.setLocation(0, 0);
        downloadsPanelFooterLabel.setCursor(mainFrameRef.handCursor);
        downloadsPanelFooterLabel.setForeground(mainFrameRef.default1);
        downloadsPanel.setLocation(-1,0);
        downloadsTableScrollPane.getViewport().setBackground(java.awt.Color.white);
        
        downloadsHeader.setLocation(0,0);
        downloadsHeader.setBackground(mainFrameRef.default4);
        downloadsHeader.setIcon(mainFrameRef.downloadingIcon);
        
        uploadsPanel.setLocation(-1,0);
        uploadsTableScrollPane.getViewport().setBackground(java.awt.Color.white);
        uploadsTableScrollPane.setSize(uploadsPanel.getWidth(), uploadsPanel.getHeight()-uploadsTableScrollPane.getY());
        
        uploadsHeader.setBackground(mainFrameRef.default4);
        uploadsHeader.setIcon(mainFrameRef.uploadingIcon);
        
        createUploadsTable();
        createDownloadsTable();
        
        repaint();
    }
    
    public void resumeDownloads() {
        // Resume Downloads
        
        String buffer;
        int itemCount;
        try {
            itemCount=Integer.parseInt(mainFrameRef.fileioClassInstance.getDataFromFile("_downloads.dat", "itemcount", false)[0].toString());
        } catch ( java.lang.NumberFormatException nfe ) { return; }
        int itemLooper=-1;
        
        while ( itemLooper++<=itemCount ) {
            buffer=mainFrameRef.fileioClassInstance.getDataFromFile("_downloads.dat", "item"+itemLooper, false)[0].toString();
            if ( buffer.equals("") ) {
                // removed item
                continue;
            }
            
            mainFrameRef.fileioClassInstance.removeDataFromFile("_downloads.dat", "item", String.valueOf(itemLooper), false);
            
            mainFrameRef.ftpClassInstance.startFtpDownload(null,-1,null,null,null,String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",0)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",1)), mainFrameRef.utilitiesClassInstance.isFolderFormatting(String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",0))), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",2)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",3)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",4)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",5)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",6)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",7)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",8)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",9)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",10)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",11)), true, false);
        }
        updateTransfersQueueFileItemCount(0);
    }

    public void resumeUploads() {
        // Resume Uploads
    
        String buffer;
        final int itemCount;
        try {
            itemCount=Integer.parseInt(mainFrameRef.fileioClassInstance.getDataFromFile("_uploads.dat", "itemcount", false)[0].toString());
        } catch ( java.lang.NumberFormatException nfe ) { return; }
        int itemLooper=-1;
        
        while ( itemLooper++<=itemCount ) {
            buffer=mainFrameRef.fileioClassInstance.getDataFromFile("_uploads.dat", "item"+itemLooper, false)[0].toString();
            if ( buffer.equals("") ) // removed item
                continue;

            mainFrameRef.fileioClassInstance.removeDataFromFile("_uploads.dat", "item", String.valueOf(itemLooper), false);
            
            try {
                mainFrameRef.ftpClassInstance.startFtpUpload(null, -1, null, null, null, String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",0)), mainFrameRef.utilitiesClassInstance.isFolderFormatting(String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",0))), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",1)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",2)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",3)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",4)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",5)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",6)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",7)), String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",8)), false, false);
            } catch ( java.lang.NumberFormatException nfe ) {
                // corrupt file - object will be purged
            }
        }
        updateTransfersQueueFileItemCount(1);
    }

    void createUploadsTable() {
        ceefee.gui.TableSorter dataSorter;
        
        
        uploadsTableDataModel=new javax.swing.table.DefaultTableModel(new String[][] {}, transferTableColumnData) {
            public boolean isCellEditable(int row,int col) {
                return false;
            }
        };
        
        dataSorter=new ceefee.gui.TableSorter(uploadsTableDataModel, mainFrameRef.displayClassInstance);
        uploadsTable=new javax.swing.JTable(dataSorter); //droppableTable(dataSorter,this); //javax.swing.JTable
        uploadsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ( evt.getButton()==evt.BUTTON3 ) {
                    if ( uploadsTable.getSelectedColumn()==-1 || uploadsTable.getSelectedRow()==-1 ) {
                        uploadsTable.setRowSelectionInterval(0,0);
                        uploadsTable.setColumnSelectionInterval(0,0);
                    }
                    uploadsTableMenu.show(uploadsTable,evt.getX(),evt.getY());
                    uploadsTableMenu.repaint();
                }
            }
        });
        
        uploadsTable.setRowHeight(uploadsTable.getRowHeight()+1);
        uploadsTable.getColumnModel().getColumn(0).setCellRenderer(mainFrameRef.defaultDataCellRendererInstance);
        uploadsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        dataSorter.addMouseListenerToHeaderInTable(uploadsTable);

        uploadsTable.setShowGrid(false);
        uploadsTable.setIntercellSpacing(mainFrameRef.zeroDimension);
        if ( mainFrameRef.graphicsMode==1 ) uploadsTable.setBackground(java.awt.Color.white);
        uploadsTable.setAutoscrolls(true);
        
        if ( mainFrameRef.graphicsMode==1 ) uploadsTableScrollPane.getViewport().setBackground(java.awt.Color.white);

        uploadsTableScrollPane.add(uploadsTable);
        uploadsTableScrollPane.setViewportView(uploadsTable);

        downloadsPanel.add(downloadsTableScrollPane);
        
    }
    public void addToUploadsTable( final String remoteFile ) {
        String[][] _tempModel=new String[uploadsTable.getRowCount()][uploadsTable.getColumnCount()];
        

        for ( int uploadsTableDataModelRowLooper=uploadsTable.getRowCount(); --uploadsTableDataModelRowLooper>(-1); ) {
            for ( int uploadsTableDataModelColumnLooper=-1; ++uploadsTableDataModelColumnLooper<uploadsTable.getColumnCount(); ) {
                _tempModel[uploadsTableDataModelRowLooper][uploadsTableDataModelColumnLooper]=uploadsTable.getValueAt(uploadsTableDataModelRowLooper, uploadsTableDataModelColumnLooper).toString();
            }
            uploadsTableDataModel.removeRow(uploadsTableDataModelRowLooper);
        }
        
        for ( int uploadsTableDataModelRowLooper=-1; ++uploadsTableDataModelRowLooper<_tempModel.length; ) {
            uploadsTableDataModel.addRow(new String[] {_tempModel[uploadsTableDataModelRowLooper][0],_tempModel[uploadsTableDataModelRowLooper][1],_tempModel[uploadsTableDataModelRowLooper][2]});
        }
        uploadsTableDataModel.addRow(new String[] { remoteFile, "", "" });
                                
    }
        
    void createDownloadsTable() {
        ceefee.gui.TableSorter dataSorter;
        
        
        downloadsTableDataModel=new javax.swing.table.DefaultTableModel(new String[][] {}, transferTableColumnData) {
            public boolean isCellEditable(int row,int col) {
                return false;
            }
        };
        
        dataSorter=new ceefee.gui.TableSorter(downloadsTableDataModel, mainFrameRef.displayClassInstance);
        downloadsTable=new javax.swing.JTable(dataSorter);
        downloadsTable.setDragEnabled(true);
        downloadsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ( evt.getButton()==evt.BUTTON3 ) {
                    if ( downloadsTable.getSelectedColumn()==-1 || downloadsTable.getSelectedRow()==-1 ) {
                        downloadsTable.setRowSelectionInterval(0,0);
                        downloadsTable.setColumnSelectionInterval(0,0);
                    }
                    if ( downloadsTable.getValueAt(downloadsTable.getSelectedRow(),1).equals("(100%)  Done.") ) {
                        openDownloadMenuItem.setEnabled(true);
                    } else {
                        openDownloadMenuItem.setEnabled(false);
                    }
                    downloadsTableMenu.show(downloadsTable,evt.getX(),evt.getY());
                    downloadsTableMenu.repaint();
                }
            }
        });

        downloadsTable.setRowHeight(downloadsTable.getRowHeight()+1);
        downloadsTable.getColumnModel().getColumn(0).setCellRenderer(mainFrameRef.defaultDataCellRendererInstance);
        downloadsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        dataSorter.addMouseListenerToHeaderInTable(downloadsTable);

        downloadsTable.setShowGrid(false);
        downloadsTable.setIntercellSpacing(mainFrameRef.zeroDimension);
        if ( mainFrameRef.graphicsMode==1 ) downloadsTable.setBackground(java.awt.Color.white);
        downloadsTable.setAutoscrolls(true);

        downloadsTableScrollPane.setViewportView(downloadsTable);

    }
    public void addToDownloadsTable( final String remoteFile ) {
        String[][] _tempModel=new String[downloadsTable.getRowCount()][downloadsTable.getColumnCount()];
        

        for ( int downloadsTableDataModelRowLooper=downloadsTable.getRowCount(); --downloadsTableDataModelRowLooper>(-1); ) {
            for ( int downloadsTableDataModelColumnLooper=-1; ++downloadsTableDataModelColumnLooper<downloadsTable.getColumnCount(); ) {
                _tempModel[downloadsTableDataModelRowLooper][downloadsTableDataModelColumnLooper]=downloadsTable.getValueAt(downloadsTableDataModelRowLooper, downloadsTableDataModelColumnLooper).toString();
            }
            downloadsTableDataModel.removeRow(downloadsTableDataModelRowLooper);
        }
        
        for ( int downloadsTableDataModelRowLooper=-1; ++downloadsTableDataModelRowLooper<_tempModel.length; ) {
            downloadsTableDataModel.addRow(new String[] {_tempModel[downloadsTableDataModelRowLooper][0],_tempModel[downloadsTableDataModelRowLooper][1],_tempModel[downloadsTableDataModelRowLooper][2]});
        }
        downloadsTableDataModel.addRow(new String[] { remoteFile, "", "" });
                                
    }
    
    public void organize() {
        if ( isEnabled()==false ) return;
        
        horizontalSplitPane.setSize(getWidth(), getHeight()-horizontalSplitPane.getY());
        horizontalSplitPane.validate();
        
        downloadsHeader.setSize(horizontalSplitPane.getDividerLocation(), downloadsHeader.getHeight());
        
        downloadsPanel.setSize(horizontalSplitPane.getLeftComponent().getWidth(), horizontalSplitPane.getLeftComponent().getHeight());
        downloadsTableScrollPane.setSize(downloadsPanel.getWidth(), downloadsPanel.getHeight()-downloadsPanelFooterPanel.getHeight());
        downloadsTableScrollPane.validate();
        downloadsTable.setSize(downloadsTableScrollPane.getWidth(), downloadsTableScrollPane.getHeight());
        downloadsPanelFooterPanel.setBounds(0, downloadsTableScrollPane.getY()+downloadsTableScrollPane.getHeight(), downloadsPanel.getWidth(), downloadsPanelFooterPanel.getHeight());
        downloadsPanelFooterLabel.setLocation(((downloadsPanelFooterPanel.getWidth()-downloadsPanelFooterLabel.getWidth())/2), ((downloadsPanelFooterPanel.getHeight()-downloadsPanelFooterLabel.getHeight())/2));

        uploadsHeader.setBounds(downloadsHeader.getWidth(), 0, horizontalSplitPane.getWidth()-uploadsHeader.getX(), uploadsHeader.getHeight());
        
        uploadsPanel.setSize(horizontalSplitPane.getRightComponent().getWidth(), horizontalSplitPane.getRightComponent().getHeight());
        uploadsTableScrollPane.setSize(uploadsPanel.getWidth(), uploadsPanel.getHeight()-uploadsTableScrollPane.getY());
        uploadsTableScrollPane.validate();
        uploadsTable.setSize(uploadsTableScrollPane.getWidth(), uploadsTableScrollPane.getHeight());

        repaint();
        
    }
    
    public void updateTransfersQueueFileItemCount( final int download0upload1 ) {
        int itemCount=0;

        
        if ( download0upload1==0 )
            itemCount=downloadsTable.getRowCount();
        else if ( download0upload1==1 )
            itemCount=uploadsTable.getRowCount();
        
        if ( download0upload1==0 )
            mainFrameRef.fileioClassInstance.storeDataToFile("_downloads.dat", "itemcount", "", String.valueOf(itemCount), 1, 1, "", false);
        else
            mainFrameRef.fileioClassInstance.storeDataToFile("_uploads.dat", "itemcount", "", String.valueOf(itemCount), 1, 1, "", false);
        
    }

    public void removeItemsResumeAbility( final int download0upload1, final int itemIndex ) {
        String itemsFile;
        if ( download0upload1==0 )
            itemsFile="_downloads.dat";
        else
            itemsFile="_uploads.dat";
        
        mainFrameRef.fileioClassInstance.removeDataFromFile(itemsFile, "item", String.valueOf(itemIndex), false);
        
        updateTransfersQueueFileItemCount(download0upload1);
    }
    
    private void initComponents() {//GEN-BEGIN:initComponents
        uploadsTableMenu = new javax.swing.JPopupMenu();
        cancelUploadMenuItem = new javax.swing.JMenuItem();
        downloadsTableMenu = new javax.swing.JPopupMenu();
        openDownloadMenuItem = new javax.swing.JMenuItem();
        downloadsTableMenuSeparator1 = new javax.swing.JSeparator();
        cancelDownloadMenuItem = new javax.swing.JMenuItem();
        downloadsHeader = new javax.swing.JLabel();
        uploadsHeader = new javax.swing.JLabel();
        horizontalSplitPane = new javax.swing.JSplitPane();
        downloadsPanel = new javax.swing.JPanel();
        downloadsTableScrollPane = new javax.swing.JScrollPane();
        downloadsTable = new javax.swing.JTable();
        downloadsPanelFooterPanel = new javax.swing.JPanel();
        downloadsPanelFooterLabel = new javax.swing.JLabel();
        uploadsPanel = new javax.swing.JPanel();
        uploadsTableScrollPane = new javax.swing.JScrollPane();
        uploadsTable = new javax.swing.JTable();

        cancelUploadMenuItem.setText("Cancel Upload");
        cancelUploadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelUploadMenuItemActionPerformed(evt);
            }
        });

        uploadsTableMenu.add(cancelUploadMenuItem);

        openDownloadMenuItem.setText("Open");
        openDownloadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDownloadMenuItemActionPerformed(evt);
            }
        });

        downloadsTableMenu.add(openDownloadMenuItem);

        downloadsTableMenu.add(downloadsTableMenuSeparator1);

        cancelDownloadMenuItem.setText("Cancel Download");
        cancelDownloadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelDownloadMenuItemActionPerformed(evt);
            }
        });

        downloadsTableMenu.add(cancelDownloadMenuItem);

        setLayout(null);

        downloadsHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        downloadsHeader.setText("In");
        downloadsHeader.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        downloadsHeader.setOpaque(true);
        add(downloadsHeader);
        downloadsHeader.setBounds(0, 0, 120, 23);

        uploadsHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        uploadsHeader.setText("Out");
        uploadsHeader.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        uploadsHeader.setOpaque(true);
        add(uploadsHeader);
        uploadsHeader.setBounds(270, 0, 180, 23);

        horizontalSplitPane.setBorder(null);
        horizontalSplitPane.setDividerLocation(150);
        horizontalSplitPane.setDividerSize(4);
        horizontalSplitPane.setContinuousLayout(true);
        horizontalSplitPane.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                horizontalSplitPaneAncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                horizontalSplitPaneAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                horizontalSplitPaneAncestorRemoved(evt);
            }
        });
        horizontalSplitPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                horizontalSplitPaneComponentHidden(evt);
            }
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                horizontalSplitPaneComponentMoved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                horizontalSplitPaneComponentResized(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                horizontalSplitPaneComponentShown(evt);
            }
        });
        horizontalSplitPane.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                horizontalSplitPaneComponentAdded(evt);
            }
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                horizontalSplitPaneComponentRemoved(evt);
            }
        });
        horizontalSplitPane.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                horizontalSplitPaneFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                horizontalSplitPaneFocusLost(evt);
            }
        });
        horizontalSplitPane.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                horizontalSplitPaneHierarchyChanged(evt);
            }
        });
        horizontalSplitPane.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
                horizontalSplitPaneAncestorMoved1(evt);
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                horizontalSplitPaneAncestorResized(evt);
            }
        });
        horizontalSplitPane.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                horizontalSplitPaneCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                horizontalSplitPaneInputMethodTextChanged(evt);
            }
        });
        horizontalSplitPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                horizontalSplitPaneKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                horizontalSplitPaneKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                horizontalSplitPaneKeyTyped(evt);
            }
        });
        horizontalSplitPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                horizontalSplitPaneMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                horizontalSplitPaneMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                horizontalSplitPaneMouseReleased(evt);
            }
        });
        horizontalSplitPane.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                horizontalSplitPaneMouseDragged(evt);
            }
        });
        horizontalSplitPane.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                horizontalSplitPaneMouseWheelMoved(evt);
            }
        });
        horizontalSplitPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                horizontalSplitPanePropertyChange(evt);
            }
        });
        horizontalSplitPane.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                horizontalSplitPaneVetoableChange(evt);
            }
        });

        downloadsPanel.setLayout(null);

        downloadsTableScrollPane.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        downloadsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        downloadsTableScrollPane.setViewportView(downloadsTable);

        downloadsPanel.add(downloadsTableScrollPane);
        downloadsTableScrollPane.setBounds(0, 0, 448, 49);

        downloadsPanelFooterPanel.setLayout(null);

        downloadsPanelFooterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        downloadsPanelFooterLabel.setText("<HTML><U>Click here to open the 'Download' folder.</U></HTML>");
        downloadsPanelFooterLabel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        downloadsPanelFooterLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                downloadsPanelFooterLabelMouseClicked(evt);
            }
        });

        downloadsPanelFooterPanel.add(downloadsPanelFooterLabel);
        downloadsPanelFooterLabel.setBounds(0, 0, 230, 23);

        downloadsPanel.add(downloadsPanelFooterPanel);
        downloadsPanelFooterPanel.setBounds(0, 110, 300, 23);

        horizontalSplitPane.setLeftComponent(downloadsPanel);

        uploadsPanel.setLayout(null);

        uploadsPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        uploadsTableScrollPane.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        uploadsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        uploadsTableScrollPane.setViewportView(uploadsTable);

        uploadsPanel.add(uploadsTableScrollPane);
        uploadsTableScrollPane.setBounds(0, 0, 448, 275);

        horizontalSplitPane.setRightComponent(uploadsPanel);

        add(horizontalSplitPane);
        horizontalSplitPane.setBounds(0, 23, 500, 500);

    }//GEN-END:initComponents

    private void openDownloadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDownloadMenuItemActionPerformed
        mainFrameRef.utilitiesClassInstance.system(mainFrameRef.downloadDir+downloadsTable.getValueAt(downloadsTable.getSelectedRow(),0));
    }//GEN-LAST:event_openDownloadMenuItemActionPerformed

    private void horizontalSplitPanePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_horizontalSplitPanePropertyChange
        
        organize();
    }//GEN-LAST:event_horizontalSplitPanePropertyChange

    private void horizontalSplitPaneMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_horizontalSplitPaneMouseWheelMoved
    }//GEN-LAST:event_horizontalSplitPaneMouseWheelMoved

    private void horizontalSplitPaneMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_horizontalSplitPaneMousePressed
    }//GEN-LAST:event_horizontalSplitPaneMousePressed

    private void horizontalSplitPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_horizontalSplitPaneMouseClicked
    }//GEN-LAST:event_horizontalSplitPaneMouseClicked

    private void horizontalSplitPaneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_horizontalSplitPaneKeyTyped
    }//GEN-LAST:event_horizontalSplitPaneKeyTyped

    private void horizontalSplitPaneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_horizontalSplitPaneKeyReleased
    }//GEN-LAST:event_horizontalSplitPaneKeyReleased

    private void horizontalSplitPaneInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_horizontalSplitPaneInputMethodTextChanged
    }//GEN-LAST:event_horizontalSplitPaneInputMethodTextChanged

    private void horizontalSplitPaneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_horizontalSplitPaneKeyPressed
    }//GEN-LAST:event_horizontalSplitPaneKeyPressed

    private void horizontalSplitPaneHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_horizontalSplitPaneHierarchyChanged
    }//GEN-LAST:event_horizontalSplitPaneHierarchyChanged

    private void horizontalSplitPaneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_horizontalSplitPaneFocusLost
    }//GEN-LAST:event_horizontalSplitPaneFocusLost

    private void horizontalSplitPaneFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_horizontalSplitPaneFocusGained
    }//GEN-LAST:event_horizontalSplitPaneFocusGained

    private void horizontalSplitPaneComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_horizontalSplitPaneComponentShown
    }//GEN-LAST:event_horizontalSplitPaneComponentShown

    private void horizontalSplitPaneComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_horizontalSplitPaneComponentRemoved
    }//GEN-LAST:event_horizontalSplitPaneComponentRemoved

    private void horizontalSplitPaneComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_horizontalSplitPaneComponentMoved
    }//GEN-LAST:event_horizontalSplitPaneComponentMoved

    private void horizontalSplitPaneComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_horizontalSplitPaneComponentHidden
    }//GEN-LAST:event_horizontalSplitPaneComponentHidden

    private void horizontalSplitPaneComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_horizontalSplitPaneComponentAdded
    }//GEN-LAST:event_horizontalSplitPaneComponentAdded

    private void horizontalSplitPaneCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_horizontalSplitPaneCaretPositionChanged
    }//GEN-LAST:event_horizontalSplitPaneCaretPositionChanged

    private void horizontalSplitPaneAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_horizontalSplitPaneAncestorResized
    }//GEN-LAST:event_horizontalSplitPaneAncestorResized

    private void horizontalSplitPaneAncestorRemoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_horizontalSplitPaneAncestorRemoved
    }//GEN-LAST:event_horizontalSplitPaneAncestorRemoved

    private void horizontalSplitPaneAncestorMoved1(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_horizontalSplitPaneAncestorMoved1
    }//GEN-LAST:event_horizontalSplitPaneAncestorMoved1

    private void horizontalSplitPaneAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_horizontalSplitPaneAncestorMoved
    }//GEN-LAST:event_horizontalSplitPaneAncestorMoved

    private void horizontalSplitPaneAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_horizontalSplitPaneAncestorAdded
    }//GEN-LAST:event_horizontalSplitPaneAncestorAdded

    private void horizontalSplitPaneVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_horizontalSplitPaneVetoableChange
    }//GEN-LAST:event_horizontalSplitPaneVetoableChange

    private void horizontalSplitPaneMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_horizontalSplitPaneMouseDragged
    }//GEN-LAST:event_horizontalSplitPaneMouseDragged

    private void horizontalSplitPaneComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_horizontalSplitPaneComponentResized
        
        organize();
    }//GEN-LAST:event_horizontalSplitPaneComponentResized

    private void horizontalSplitPaneMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_horizontalSplitPaneMouseReleased
        
        organize();
        
    }//GEN-LAST:event_horizontalSplitPaneMouseReleased

    private void cancelUploadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelUploadMenuItemActionPerformed
        // nullify; cancelling upload
        uploadsTableDataModel.setValueAt("Annulled.",uploadsTable.getSelectedRow(),1);
        uploadsTableDataModel.setValueAt("",uploadsTable.getSelectedRow(),2);
        
        removeItemsResumeAbility(1,uploadsTable.getSelectedRow());
        
    }//GEN-LAST:event_cancelUploadMenuItemActionPerformed

    private void cancelDownloadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDownloadMenuItemActionPerformed
        // nullify; cancelling download
        downloadsTableDataModel.setValueAt("Annulled.",downloadsTable.getSelectedRow(),1);
        downloadsTableDataModel.setValueAt("",downloadsTable.getSelectedRow(),2);
        
        removeItemsResumeAbility(0,downloadsTable.getSelectedRow());
        
    }//GEN-LAST:event_cancelDownloadMenuItemActionPerformed

    private void downloadsPanelFooterLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_downloadsPanelFooterLabelMouseClicked
        mainFrameRef.utilitiesClassInstance.system(mainFrameRef.downloadDir);
        
    }//GEN-LAST:event_downloadsPanelFooterLabelMouseClicked

    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
    }//GEN-LAST:event_exitForm
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem cancelDownloadMenuItem;
    private javax.swing.JMenuItem cancelUploadMenuItem;
    private javax.swing.JLabel downloadsHeader;
    private javax.swing.JPanel downloadsPanel;
    private javax.swing.JLabel downloadsPanelFooterLabel;
    private javax.swing.JPanel downloadsPanelFooterPanel;
    public javax.swing.JTable downloadsTable;
    private javax.swing.JPopupMenu downloadsTableMenu;
    private javax.swing.JSeparator downloadsTableMenuSeparator1;
    private javax.swing.JScrollPane downloadsTableScrollPane;
    public javax.swing.JSplitPane horizontalSplitPane;
    private javax.swing.JMenuItem openDownloadMenuItem;
    private javax.swing.JLabel uploadsHeader;
    private javax.swing.JPanel uploadsPanel;
    public javax.swing.JTable uploadsTable;
    private javax.swing.JPopupMenu uploadsTableMenu;
    private javax.swing.JScrollPane uploadsTableScrollPane;
    // End of variables declaration//GEN-END:variables
    
}
