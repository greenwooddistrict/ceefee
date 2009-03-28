package ceefee.display;


public class schedule extends javax.swing.JPanel {
    
    private ceefee.main mainFrameRef;
    
    private java.util.Vector scheduleTableColumnData=new java.util.Vector(0,1);
    public javax.swing.table.DefaultTableModel scheduleTableDataModel;
    public javax.swing.JTable scheduleTable;
    
    private boolean scheduleTableInitialColumnWidthsSet;
    
    private javax.swing.JScrollPane scheduleTableScrollPane;

    final double firstColumnPercentage                     =.25;
    final double secondColumnPercentage                    =.5;
    final double thirdColumnPercentage                     =.25;
    
    public java.util.Vector scheduledItems=new java.util.Vector(0,1);
    
    java.util.Vector timers=new java.util.Vector(0,1);
    
    
    public schedule( final ceefee.main _mainFrameRef ) {
        mainFrameRef=_mainFrameRef;
        
        initComponents();
        
        createScheduleTable();
        add(scheduleTableScrollPane);
        
        aiLabel.setCursor(mainFrameRef.handCursor);
        aiLabel.setForeground(mainFrameRef.default1);
        riLabel.setCursor(mainFrameRef.handCursor);
        riLabel.setForeground(mainFrameRef.default1);
        eiLabel.setCursor(mainFrameRef.handCursor);
        eiLabel.setForeground(mainFrameRef.default1);
        
        setBackground(mainFrameRef.default4);
        
        scheduleSummaryLabel.setIcon(mainFrameRef.clockIcon);
        
        scheduleTableScrollPane.setBounds(scheduleSummaryLabel.getX()+5, scheduleSummaryLabel.getY()+scheduleSummaryLabel.getHeight()+5, 380, 260);
        
        scheduleTable.setBackground(mainFrameRef.default4);
     
        updateVignette();
    }

    void createScheduleTable() {
        ceefee.gui.TableSorter dataSorter;

        scheduleTableColumnData.add("Object");
        scheduleTableColumnData.add("Description");
        scheduleTableColumnData.add("Scheduled Date");
        scheduleTableColumnData.add("");
        scheduleTableColumnData.add("");
        scheduleTableDataModel=new javax.swing.table.DefaultTableModel(null, scheduleTableColumnData) {
            public boolean isCellEditable(int row,int col) {
                return false;
            }
        };
       
        dataSorter=new ceefee.gui.TableSorter(scheduleTableDataModel,mainFrameRef.displayClassInstance);
        scheduleTable=new ceefee.gui.dnd.jtable(mainFrameRef,dataSorter);
        scheduleTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scheduleTableMouseClicked(evt);
            }
        });
        scheduleTable.setRowHeight(scheduleTable.getRowHeight()+1);
        scheduleTable.setDragEnabled(true);
        scheduleTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        dataSorter.addMouseListenerToHeaderInTable(scheduleTable);
        scheduleTable.setShowGrid(false);
        scheduleTable.setIntercellSpacing(mainFrameRef.zeroDimension);
        scheduleTable.setBackground(mainFrameRef.default4);
        scheduleTable.setAutoscrolls(true);
        scheduleTable.setAutoResizeMode(scheduleTable.AUTO_RESIZE_OFF);
        scheduleTable.getColumnModel().getColumn(0).setCellRenderer(mainFrameRef.defaultDataCellRendererInstance);
        scheduleTable.removeColumn(scheduleTable.getColumnModel().getColumn(4));
        scheduleTable.removeColumn(scheduleTable.getColumnModel().getColumn(3));
        
        scheduleTableScrollPane=new ceefee.gui.dnd.jscrollpane(mainFrameRef);
        scheduleTableScrollPane.getViewport().setBackground(mainFrameRef.default4);
        scheduleTableScrollPane.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        scheduleTableScrollPane.setOpaque(false);
        scheduleTableScrollPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scheduleTableMouseClicked(evt);
            }
        });
        scheduleTableScrollPane.setViewportView(scheduleTable);

    }
    void setScheduleTableInitialColumnWidths() {
        if ( scheduleTableScrollPane.getWidth()<1 ) return;
        
        scheduleTable.getColumnModel().getColumn(0).setPreferredWidth((int)(scheduleTableScrollPane.getWidth()*firstColumnPercentage));
        scheduleTable.getColumnModel().getColumn(1).setPreferredWidth((int)(scheduleTableScrollPane.getWidth()*secondColumnPercentage));
        scheduleTable.getColumnModel().getColumn(2).setPreferredWidth((int)(scheduleTableScrollPane.getWidth()*thirdColumnPercentage));
                
        scheduleTable.setAutoResizeMode(scheduleTable.AUTO_RESIZE_ALL_COLUMNS);
        
        scheduleTableInitialColumnWidthsSet=true;
    }

    public void resumeTasks() {
        // Resume Tasks
        java.util.Vector itemData=new java.util.Vector(0,1);
        String buffer;
        int itemCount;
        try {
            itemCount=Integer.parseInt(mainFrameRef.fileioClassInstance.getDataFromFile("_tasks.dat", "itemcount", false)[0].toString());
        } catch ( java.lang.NumberFormatException nfe ) {
            return;
        }
        int itemLooper=-1;
        
        while ( itemLooper++<=itemCount ) {
            buffer=mainFrameRef.fileioClassInstance.getDataFromFile("_tasks.dat", "item"+itemLooper, false)[0].toString();
            if ( buffer.equals("") ) {
                // removed item
                continue;
            }
            
            mainFrameRef.fileioClassInstance.removeDataFromFile("_tasks.dat", "item", String.valueOf(itemLooper), false);

            for ( int itemDataLooper=-1; ++itemDataLooper<13; ) {
                if ( itemDataLooper==11 )
                    itemData.add(String.valueOf(Long.parseLong(String.valueOf(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",itemDataLooper)))));
                else
                    itemData.add(mainFrameRef.utilitiesClassInstance.getToken(buffer,"|",itemDataLooper));
            }
            addNewTask(itemData.toArray());
        }
        mainFrameRef.fileioClassInstance.storeDataToFile("_tasks.dat", "itemcount", "", String.valueOf(scheduleTable.getRowCount()), 1, 1, "", false);

    }
    
    private void executeTask( final int itemIndex ) {
        final ceefee.display.sb sbTabInstance=mainFrameRef.getSelectedSbTab();

        Object[] itemData=(Object[])scheduledItems.get(itemIndex);

        final String hostDescription=String.valueOf(itemData[0]);
        final String hostAddress=String.valueOf(itemData[1]);
        final String hostPort=String.valueOf(itemData[2]);
        final String hostUsername=String.valueOf(itemData[3]);
        final String hostPassword=String.valueOf(itemData[4]);
        final String hostProtocol=String.valueOf(itemData[5]);
        final String dataFormat=String.valueOf(itemData[6]);
        final String connectionMethod=String.valueOf(itemData[7]);

        final java.util.Calendar ci=java.util.Calendar.getInstance();
        
        String remoteFilePath=String.valueOf(itemData[8]);
        String remoteFileName="";
        remoteFilePath=mainFrameRef.utilitiesClassInstance.getMotherPathFromPath(String.valueOf(itemData[8]),"/");
        remoteFileName=mainFrameRef.utilitiesClassInstance.getObjectFromPath(String.valueOf(itemData[8]),"/");

        String localFileName="";
        String localFilePath=String.valueOf(itemData[9]);
        localFilePath=mainFrameRef.utilitiesClassInstance.getMotherPathFromPath(String.valueOf(itemData[9]),"\\");
        localFileName=mainFrameRef.utilitiesClassInstance.getObjectFromPath(String.valueOf(itemData[9]),"\\");


        if ( remoteFilePath.length()>1 && remoteFilePath.substring(remoteFilePath.length()-2,remoteFilePath.length()).equals("./") ) {
            remoteFilePath=remoteFilePath.substring(0,remoteFilePath.length()-2);
        }

        if ( itemData[10].equals("Download and Execute") ) {
            mainFrameRef.ftpClassInstance.startFtpDownload(sbTabInstance, sbTabInstance.sessionID, mainFrameRef.currentstateInstance, null, null, remoteFileName, remoteFilePath, true, "", localFilePath+localFileName, "-1", "", hostAddress, hostPort, hostUsername, hostPassword, dataFormat, connectionMethod, true, true );
        } else if ( itemData[10].equals("Download") ) {
            mainFrameRef.ftpClassInstance.startFtpDownload(sbTabInstance, sbTabInstance.sessionID, mainFrameRef.currentstateInstance, null, null, remoteFileName, remoteFilePath, true, "", localFilePath+localFileName, "-1", "", hostAddress, hostPort, hostUsername, hostPassword, dataFormat, connectionMethod, true, false );
        } else if ( itemData[10].equals("Upload") ) {
            mainFrameRef.ftpClassInstance.startFtpUpload(sbTabInstance, sbTabInstance.sessionID, mainFrameRef.currentstateInstance, null, null, localFilePath+localFileName, true, remoteFilePath+remoteFileName, "", hostAddress, hostPort, hostUsername, hostPassword, dataFormat, connectionMethod, false, false);
        }

        if ( itemData[12].equals("Once")==false ) {
            ci.setTimeInMillis(Long.parseLong(String.valueOf(itemData[11])));
            
            if ( itemData[12].equals("Daily") )
                ci.add(java.util.Calendar.DAY_OF_YEAR, 1);
            else if ( itemData[12].equals("Weekly") )
                ci.add(java.util.Calendar.WEEK_OF_YEAR, 1);
            else if ( itemData[12].equals("Monthly") )
                ci.add(java.util.Calendar.MONTH, 1);

            itemData[11]=String.valueOf(ci.getTimeInMillis());
            addNewTask(itemData);
        }
        removeScheduledTask(itemIndex,false);
        
    }
    
    private class timerTask extends java.util.TimerTask {
        int itemIndex;
        timerTask( final int _itemIndex ) {
            itemIndex=_itemIndex;
        }
        public void run() {
            executeTask(itemIndex);
        }
    };

    public void addNewTask( final Object[] scheduleData ) {
        java.util.Timer taskTimer;
        int itemIndex;
        timerTask tt=new timerTask(scheduledItems.size());
        java.util.Calendar ci=java.util.Calendar.getInstance();
        ci.setTimeInMillis(Long.parseLong(String.valueOf(scheduleData[11])));

        
        scheduledItems.add(scheduleData);

        scheduleTableDataModel.addRow(new String[] {mainFrameRef.utilitiesClassInstance.getObjectFromPath(scheduleData[8].toString(),"/"), scheduleData[0].toString(), String.valueOf(ci.getTime())});
        itemIndex=scheduleTable.getRowCount();
        
        mainFrameRef.fileioClassInstance.storeDataToFile("_tasks.dat", "itemcount", "", String.valueOf(itemIndex), 1, 1, "", false);
        for ( int dataLooper=-1; ++dataLooper<scheduleData.length; ) {
            mainFrameRef.fileioClassInstance.storeDataToFile("_tasks.dat", "item", "", scheduleData[dataLooper].toString(), dataLooper+1, scheduleData.length, String.valueOf(itemIndex), false);
        }
        
        updateVignette();
        
        taskTimer=new java.util.Timer();
        timers.add(taskTimer);
        
        taskTimer.schedule(tt, ci.getTime());
        
    }
    
    private void editScheduledTask( int itemIndex ) {
        if ( scheduleTable.getRowCount()==0 ) return;
        if ( itemIndex==-1 ) itemIndex=0;
        
        final ceefee.display.schedule_dataentry scheduleDataEntryInstance=new ceefee.display.schedule_dataentry(mainFrameRef,this,itemIndex,String.valueOf(((Object[])scheduledItems.get(itemIndex))[0]),String.valueOf(((Object[])scheduledItems.get(itemIndex))[1]),String.valueOf(((Object[])scheduledItems.get(itemIndex))[2]),String.valueOf(((Object[])scheduledItems.get(itemIndex))[3]),String.valueOf(((Object[])scheduledItems.get(itemIndex))[4]),String.valueOf(((Object[])scheduledItems.get(itemIndex))[5]),String.valueOf(((Object[])scheduledItems.get(itemIndex))[6]),String.valueOf(((Object[])scheduledItems.get(itemIndex))[7]),String.valueOf(((Object[])scheduledItems.get(itemIndex))[8]),String.valueOf(((Object[])scheduledItems.get(itemIndex))[9]),String.valueOf(((Object[])scheduledItems.get(itemIndex))[10]),Long.parseLong(String.valueOf(((Object[])scheduledItems.get(itemIndex))[11])),String.valueOf(((Object[])scheduledItems.get(itemIndex))[12]));
        mainFrameRef.setVisible(false);
        scheduleDataEntryInstance.setVisible(true);

    }
    
    private void removeScheduledTask( int itemIndex, final boolean killTimer ) {
        if ( scheduleTable.getRowCount()==0 ) return;
        if ( itemIndex==-1 ) itemIndex=0;

        if ( killTimer ) ((java.util.Timer)timers.get(itemIndex)).cancel();
        timers.remove(itemIndex);
        scheduledItems.remove(itemIndex);
        scheduleTableDataModel.removeRow(itemIndex);
        mainFrameRef.fileioClassInstance.removeDataFromFile("_tasks.dat", "item", String.valueOf(itemIndex+1), false);
        mainFrameRef.fileioClassInstance.storeDataToFile("_tasks.dat", "itemcount", "", String.valueOf(scheduleTable.getRowCount()), 1, 1, "", false);

        updateVignette();
    }
    
    void updateVignette() {
        if ( scheduleTable.getRowCount()==0 ) {
            scheduleSummaryLabel.setText("  No tasks are currently scheduled.");
        } else if ( scheduleTable.getRowCount()==1 ) {
            scheduleSummaryLabel.setText("  1 task is currently scheduled.");
        } else {
            scheduleSummaryLabel.setText(scheduleTable.getRowCount()+" tasks are currently scheduled.");
        }
    }
    
    public void organize() {
        
        setEnabled(false);
        
        scheduleSummaryLabel.setSize(getWidth()-scheduleSummaryLabel.getX(), scheduleSummaryLabel.getHeight());
        
        scheduleTableScrollPane.setSize(getWidth()-scheduleTableScrollPane.getX()-30, getHeight()-scheduleTableScrollPane.getY()-60);

        if ( scheduleTableInitialColumnWidthsSet==false )
            setScheduleTableInitialColumnWidths();

        setEnabled(true);
        repaint();

    }

    void scheduleTableMouseClicked( final java.awt.event.MouseEvent evt ) {
        if ( evt.getButton()==evt.BUTTON3 ) {
            if ( scheduleTable.getSelectedColumn()==-1 || scheduleTable.getSelectedRow()==-1 ) {
                if ( scheduleTable.getRowCount()>0 ) {
                    scheduleTable.setRowSelectionInterval(0,0);
                    scheduleTable.setColumnSelectionInterval(0,0);

                    editScheduleItemMenuItem.setEnabled(true);
                    removeScheduleItemMenuItem.setEnabled(true);

                } else {
                    editScheduleItemMenuItem.setEnabled(false);
                    removeScheduleItemMenuItem.setEnabled(false);
                }
            } else {
                editScheduleItemMenuItem.setEnabled(true);
                removeScheduleItemMenuItem.setEnabled(true);
            }

            scheduleMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            scheduleMenu.repaint();
        }
    }
    
    public void launchScheduleDataEntry(final String _taskDescription, final String _hostAddress, final String _hostPort, final String _hostUsername, final String _hostPassword, final String _protocol, final String _dataFormat, final String _connectionMethod, final String _remoteObjectPath, final String _localObjectPath, final String _operation, final long _date, final String _frequency) {
        final ceefee.display.schedule_dataentry scheduleDataEntryInstance=new ceefee.display.schedule_dataentry(mainFrameRef,this,-1,_taskDescription,_hostAddress,_hostPort,_hostUsername,_hostPassword,_protocol,_dataFormat,_connectionMethod,_remoteObjectPath,_localObjectPath,_operation,_date,_frequency);
        mainFrameRef.setVisible(false);
        scheduleDataEntryInstance.setVisible(true);
    }

    private void initComponents() {//GEN-BEGIN:initComponents
        scheduleMenu = new javax.swing.JPopupMenu();
        addScheduleItemMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        editScheduleItemMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        removeScheduleItemMenuItem = new javax.swing.JMenuItem();
        scheduleSummaryLabel = new javax.swing.JLabel();
        aiLabel = new javax.swing.JLabel();
        riLabel = new javax.swing.JLabel();
        eiLabel = new javax.swing.JLabel();

        addScheduleItemMenuItem.setText("Add Item");
        addScheduleItemMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addScheduleItemMenuItemActionPerformed(evt);
            }
        });

        scheduleMenu.add(addScheduleItemMenuItem);

        scheduleMenu.add(jSeparator1);

        editScheduleItemMenuItem.setText("Edit Item");
        editScheduleItemMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editScheduleItemMenuItemActionPerformed(evt);
            }
        });

        scheduleMenu.add(editScheduleItemMenuItem);

        scheduleMenu.add(jSeparator2);

        removeScheduleItemMenuItem.setText("Remove Item");
        removeScheduleItemMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeScheduleItemMenuItemActionPerformed(evt);
            }
        });

        scheduleMenu.add(removeScheduleItemMenuItem);

        setLayout(null);

        scheduleSummaryLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        scheduleSummaryLabel.setIcon(new javax.swing.ImageIcon(""));
        add(scheduleSummaryLabel);
        scheduleSummaryLabel.setBounds(140, 10, 340, 23);

        aiLabel.setText("<HTML><U>Add Item</U></HTML>");
        aiLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aiLabelMouseClicked(evt);
            }
        });

        add(aiLabel);
        aiLabel.setBounds(20, 40, 55, 15);

        riLabel.setText("<HTML><U>Remove Item</U></HTML>");
        riLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                riLabelMouseClicked(evt);
            }
        });

        add(riLabel);
        riLabel.setBounds(20, 120, 75, 15);

        eiLabel.setText("<HTML><U>Edit Item</U></HTML>");
        eiLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eiLabelMouseClicked(evt);
            }
        });

        add(eiLabel);
        eiLabel.setBounds(20, 80, 55, 15);

    }//GEN-END:initComponents

    private void editScheduleItemMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editScheduleItemMenuItemActionPerformed
        editScheduledTask(scheduleTable.getSelectedRow());
    }//GEN-LAST:event_editScheduleItemMenuItemActionPerformed

    private void eiLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eiLabelMouseClicked
        editScheduledTask(scheduleTable.getSelectedRow());
    }//GEN-LAST:event_eiLabelMouseClicked

    private void removeScheduleItemMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeScheduleItemMenuItemActionPerformed
        removeScheduledTask(scheduleTable.getSelectedRow(),true);
    }//GEN-LAST:event_removeScheduleItemMenuItemActionPerformed

    private void riLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_riLabelMouseClicked
        removeScheduledTask(scheduleTable.getSelectedRow(),true);
    }//GEN-LAST:event_riLabelMouseClicked

    private void aiLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aiLabelMouseClicked
        launchScheduleDataEntry("","","","","","","","","","","",0,"");
    }//GEN-LAST:event_aiLabelMouseClicked

    private void addScheduleItemMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addScheduleItemMenuItemActionPerformed
        launchScheduleDataEntry("","","","","","","","","","","",0,"");
    }//GEN-LAST:event_addScheduleItemMenuItemActionPerformed
    
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
    }//GEN-LAST:event_exitForm
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addScheduleItemMenuItem;
    private javax.swing.JLabel aiLabel;
    private javax.swing.JMenuItem editScheduleItemMenuItem;
    private javax.swing.JLabel eiLabel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JMenuItem removeScheduleItemMenuItem;
    private javax.swing.JLabel riLabel;
    private javax.swing.JPopupMenu scheduleMenu;
    private javax.swing.JLabel scheduleSummaryLabel;
    // End of variables declaration//GEN-END:variables
    
}