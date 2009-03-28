package ceefee.ftp;



public class displaydirectory implements Runnable {
    
    private ceefee.main mainFrameRef;
    private ceefee.display.sb sbTabInstance;
    private int sessionID;
    private boolean evince;
    private boolean corrected;
    private boolean killSockets;
    
    
    /** Creates a new instance of general_ftp_displaydirectory */
    public displaydirectory(final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final int _sessionID, final java.net.Socket _controlSocket, final Object _dataSocket, final boolean _killSockets, final boolean _evince, final boolean _corrected) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
        killSockets=_killSockets;
        sessionID=_sessionID;
        evince=_evince;
        corrected=_corrected;
    }
    
    public void run() {
        final int connectionMethod=sbTabInstance.getConnectionMethod();
        
        final javax.swing.table.TableColumnModel resultsTableColumnModelBuffer=sbTabInstance.resultsTable.getColumnModel();
        
        javax.swing.table.DefaultTableModel resultsTableDataModelBuffer=null;

        String dataTransferHeader;
        Object[] retData=new Object[2];

        int marker;

        String tdwdp;

        
        retData=mainFrameRef.ftpClassInstance.ensureFtpControlSocketConnection(sbTabInstance,sessionID,sbTabInstance.controlSocket,sbTabInstance.hostAddress.getText(),sbTabInstance.hostPort.getText(),sbTabInstance.hostUsername.getText(),new String(sbTabInstance.hostPassword.getPassword()),sbTabInstance.getDataFormat(), evince);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            sbTabInstance.enableDisable(2,0);
            if ( retData[0]!=null ) try { ((java.net.Socket)retData[0]).close(); } catch ( java.io.IOException ioe ) {}
            return;
        }
        sbTabInstance.controlSocket=(java.net.Socket)retData[0];

        sbTabInstance.resultsTableAccentedCells=new java.util.Vector(0,1);

        //if junk gets left in socket ( like info from a slow host ) discard
        try {
            if ( (marker=((java.net.Socket)sbTabInstance.controlSocket).getInputStream().available())!=0 )
                ((java.net.Socket)sbTabInstance.controlSocket).getInputStream().skip(marker);
        } catch ( java.io.IOException ioe ) {
            if ( evince ) sbTabInstance.addTextToStatListing(sessionID,"( " + ioe.getMessage() + " )", 1,2,false);
            if ( sbTabInstance.controlSocket!=null ) try { sbTabInstance.controlSocket.close(); } catch ( java.io.IOException ioe1 ) {}
            return;
        }    

        if ( sbTabInstance.iffyConnection || sbTabInstance.activityList.getItemCount()<3 ) {
            sbTabInstance.currentWD=mainFrameRef.ftpClassInstance.getWorkingDirectory(sbTabInstance,sessionID,sbTabInstance.controlSocket,evince);
            if ( sbTabInstance.currentWD==null || (sbTabInstance.currentWD.length()>1 && sbTabInstance.currentWD.substring(0,2).equals("-1")) ) {
                if ( sbTabInstance.currentWD.substring(2).equals("") ) sbTabInstance.currentWD="-1Socket error. Connection closing.";
                sbTabInstance.addTextToStatListing(sessionID,"( " + sbTabInstance.currentWD.substring(2) + " )", 1, 2, false);
                sbTabInstance.currentWD="";
                sbTabInstance.enableDisable(2,0);
                return;
            }
        }

        if ( connectionMethod==1 ) {
            retData=mainFrameRef.ftpClassInstance.performPortOperation(sbTabInstance,sessionID,sbTabInstance.controlSocket,sbTabInstance.dataSocket,sbTabInstance.hostAddress.getText(),sbTabInstance.hostPort.getText(),sbTabInstance.hostUsername.getText(),new String(sbTabInstance.hostPassword.getPassword()),"LIST","( Retrieving directory listing. Please Wait... )",evince);
        } else {
            retData=mainFrameRef.ftpClassInstance.performPasvOperation(sbTabInstance,sessionID,sbTabInstance.controlSocket,sbTabInstance.dataSocket,sbTabInstance.hostAddress.getText(),sbTabInstance.hostPort.getText(),sbTabInstance.hostUsername.getText(),new String(sbTabInstance.hostPassword.getPassword()),"LIST","( Retrieving directory listing. Please Wait... )",evince);
        }

        // change mode if current mode does not work
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            if ( corrected ) {
                sbTabInstance.enableDisable(2,0);
                return;
            } else {
                if ( retData[0].toString().indexOf("Permission denied.")!=-1 ) {
                    if ( evince ) sbTabInstance.addTextToStatListing(sessionID,"( " + retData[0].toString().substring(2) + " )",1,2,false);
                    sbTabInstance.enableDisable(2,0);
                    return;
                } else {
                    if ( evince ) {
                        if ( retData[0].toString().length()>2 ) {
                            sbTabInstance.addTextToStatListing(sessionID,"( " + retData[0].toString().substring(2) + " )",1,2,false);
                        }
                    }
                    
                    if ( sbTabInstance.pasvMode.isSelected() ) {
                        sbTabInstance.portMode.setSelected(true);
                    } else {
                        sbTabInstance.pasvMode.setSelected(true);
                    }

                    try {
                        ((java.net.Socket)sbTabInstance.controlSocket).close();
                        sbTabInstance.controlSocket=null;
                    } catch ( java.io.IOException ioe ) {}
                    retData=mainFrameRef.ftpClassInstance.ensureFtpControlSocketConnection(sbTabInstance, sessionID, sbTabInstance.controlSocket, sbTabInstance.hostAddress.getText(), sbTabInstance.hostPort.getText(), sbTabInstance.hostUsername.getText(), new String(sbTabInstance.hostPassword.getPassword()),sbTabInstance.getConnectionMethod(), false);
                    System.out.println(System.getProperty("line.separator")+"ret: "+retData[0]);
                    if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                        sbTabInstance.enableDisable(2,0);
                        return;
                    }
                    sbTabInstance.controlSocket=(java.net.Socket)retData[0];
                    
                    mainFrameRef.ftpClassInstance.displaydir(sbTabInstance,sessionID,sbTabInstance.controlSocket,sbTabInstance.dataSocket,killSockets,evince,true);
                }
            }
        }

        if ( sbTabInstance.sessionID!=sessionID ) {
            sbTabInstance.enableDisable(-1,0);
            return;
        } else if ( sbTabInstance.dataSocket==null ) {
            sbTabInstance.enableDisable(-1,0);
            return;
        }

        dataTransferHeader=retData[0].toString();
        
        System.out.println("1) "+(sbTabInstance==null)+System.getProperty("line.separator")+"2) "+(sessionID==sbTabInstance.sessionID));
        retData=mainFrameRef.ftpClassInstance.ftpGetResponse(sbTabInstance, sessionID, sbTabInstance.dataSocket, mainFrameRef.connectionTimeout,(evince?1:0));
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            sbTabInstance.enableDisable(2,0);
            return;
        }
        
        try {
            if ( sbTabInstance.dataSocket!=null ) {
                if ( sbTabInstance.dataSocket.getClass().getName().equals("java.net.Socket") )
                    ((java.net.Socket)sbTabInstance.dataSocket).close();
                else if ( sbTabInstance.dataSocket.getClass().getName().equals("java.net.ServerSocket") )
                    ((java.net.ServerSocket)sbTabInstance.dataSocket).close();
            }
        } catch ( java.io.IOException ioe ) {}

        if ( sbTabInstance.sessionID!=sessionID ) {
            sbTabInstance.enableDisable(2,0);
            return;
        }

        sbTabInstance.resultsTableDataModel.setRowCount(0);
        sbTabInstance.resultsTable.setEnabled(false);
        sbTabInstance.resultsTable.setVisible(false);
        
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            if ( evince ) sbTabInstance.addTextToStatListing(sessionID,"( " + retData[0].toString().substring(2) + " )",0,1,false);
        } else {
            resultsTableDataModelBuffer=mainFrameRef.ftpClassInstance.getDirectoryContents(sbTabInstance,sessionID,sbTabInstance.currentWD,retData[0].toString(), sbTabInstance.serverHeader);
        }

        for ( int resultsTableDataModelBufferLooper=-1; ++resultsTableDataModelBufferLooper<resultsTableDataModelBuffer.getRowCount(); ) {
            sbTabInstance.resultsTableDataModel.addRow(new Object[] {resultsTableDataModelBuffer.getValueAt(resultsTableDataModelBufferLooper,0),resultsTableDataModelBuffer.getValueAt(resultsTableDataModelBufferLooper,1),resultsTableDataModelBuffer.getValueAt(resultsTableDataModelBufferLooper,2),resultsTableDataModelBuffer.getValueAt(resultsTableDataModelBufferLooper,3),resultsTableDataModelBuffer.getValueAt(resultsTableDataModelBufferLooper,4)});
        }
        
        sbTabInstance.resultsTable.setEnabled(true);
        sbTabInstance.resultsTable.setVisible(true);
        sbTabInstance.resultsTable.validate();
        
        if ( evince ) sbTabInstance.addTextToStatListing(sessionID,"( " + sbTabInstance.resultsTable.getRowCount() + " Resources processed in " + mainFrameRef.ftpClassInstance.getElapsedTime(sbTabInstance,sessionID) + " seconds. )" ,1, 1, false);

        if ( (retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1"))==false ) {
            if ( dataTransferHeader.length()>2 && dataTransferHeader.substring(0,3).equals("226")==false && dataTransferHeader.indexOf("\n"+"226")==-1 ) {
                dataTransferHeader=((String)mainFrameRef.ftpClassInstance.ftpGetResponse(sbTabInstance,sessionID,sbTabInstance.controlSocket, mainFrameRef.connectionTimeout, (evince?1:0))[0]);
                if ( dataTransferHeader.length()>1 && dataTransferHeader.substring(0,2).equals("-1") ) {
                    if ( evince ) sbTabInstance.addTextToStatListing(sessionID,dataTransferHeader.substring(2), 1, 1, false);
                }
            }
        }
        sbTabInstance.addTextToStatListing(sessionID,"",0,(evince?1:0),false); //spacer
        
        if ( killSockets ) {
            try {
                if ( sbTabInstance.controlSocket!=null ) ((java.net.Socket)sbTabInstance.controlSocket).close();
            } catch ( java.io.IOException ioe ) {}
        }

        sbTabInstance.enableDisable(2,0);
        sbTabInstance.repaint();
    }    
}
