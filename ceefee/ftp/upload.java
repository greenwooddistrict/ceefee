
package ceefee.ftp;


class upload extends Thread {

    private ceefee.main mainFrameRef;
    
    private ceefee.display.sb sbTabInstance;

    private ceefee.display.currentstate currentstateInstance;
    
    private String hostAddress;
    private String hostPort;
    private String hostUsername;
    private String hostPassword;
    private int dataFormat;
    private int connectionMethod;
    private boolean move;
    
    private String remotePath;
    private String remoteFileName;
    private String absolutePath;

    private Object dataSocket=null;
    private java.net.Socket controlSocket=null;
    
    private int sessionID;
    
    private boolean resuming;
    
    private int itemsRow;
    
    private final int pollingInterval                =8000;

    private java.io.File electedFile;
            
    Object[] retData=new Object[2];
    
    private boolean automatic;
    
    private boolean evince;

    boolean cancelAction;
    
    boolean success;
    
    
    
    upload(final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final int _sessionID, final ceefee.display.currentstate _currentstateInstance, final String _absolutePath, final String _remotePath, final String _remoteFileName, final String _hostAddress, final String _hostPort, final String _hostUsername, final String _hostPassword, final String _dataFormat, final String _connectionMethod, final boolean _move, final boolean _resuming, final boolean _automatic, final boolean _evince) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
        currentstateInstance=_currentstateInstance;
        hostAddress=_hostAddress;
        hostPort=_hostPort;
        hostUsername=_hostUsername;
        hostPassword=_hostPassword;
        dataFormat=Integer.parseInt(_dataFormat);
        connectionMethod=Integer.parseInt(_connectionMethod);
        
        absolutePath=_absolutePath;
        remotePath=_remotePath;
        remoteFileName=_remoteFileName;
        electedFile=new java.io.File(absolutePath);
        if ( remoteFileName.equals("") )
            remoteFileName=electedFile.getName();
        
        move=_move;

        sessionID=_sessionID;
    
        resuming=_resuming;
        
        evince=_evince;
        
        automatic=_automatic;

        if ( _automatic ) {
            if ( (itemsRow=mainFrameRef.utilitiesClassInstance.findItemInTable(electedFile.getName(), mainFrameRef.viewTab.uploadsTable))==-1 ) {
                mainFrameRef.viewTab.addToUploadsTable(electedFile.getName());
                itemsRow=mainFrameRef.utilitiesClassInstance.findItemInTable(electedFile.getName(), (javax.swing.JTable)mainFrameRef.viewTab.uploadsTable);
            } else {
                mainFrameRef.viewTab.uploadsTable.setValueAt("", itemsRow, 1);
                mainFrameRef.viewTab.uploadsTable.setValueAt("", itemsRow, 2);
            }
            
            mainFrameRef.viewTab.uploadsTable.setValueAt("Connecting...", itemsRow, 1);
            
            addResumeAbility();
        }

    }
    
    public void run() {
        int strike=0;

        do {
            
            if ( automatic ) {
                if ( mainFrameRef.viewTab.uploadsTable.getValueAt(itemsRow,1).toString().equals("Annulled.") || mainFrameRef.viewTab.uploadsTable.getValueAt(itemsRow,1).toString().equals("(100%)  Done.") )
                    return;
            }

            runCrux();

            //if ( automatic==false ) {
            //    return;
            //} else {
                try {
                    sleep((long)(mainFrameRef.connectionTimeout*(itemsRow+1)*(strike*2)));
                } catch ( java.lang.InterruptedException ie ) {
                    return;
                }
                
                if ( (++strike)==5 ) { // do not keep wasting bandwidth resources...
                    mainFrameRef.fileioClassInstance.logError(getClass().getName()+"\t"+retData[0]);
                    return;
                }
            //}

        } while ( success==false && cancelAction==false );
    }
    
    void addResumeAbility() {
        final String dataFileName="_uploads.dat";
        
        // store information for resume capability
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFileName, "item", "", electedFile.getPath(), 1, 10, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFileName, "item", "", remotePath, 2, 10, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFileName, "item", "", remoteFileName, 3, 10, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFileName, "item", "", hostAddress, 4, 10, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFileName, "item", "", hostPort, 5, 10, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFileName, "item", "", hostUsername, 6, 10, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFileName, "item", "", hostPassword, 7, 10, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFileName, "item", "", String.valueOf(dataFormat), 8, 10, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFileName, "item", "", String.valueOf(connectionMethod), 9, 10, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFileName, "item", "", String.valueOf(move), 10, 10, String.valueOf(itemsRow), false);

        mainFrameRef.viewTab.updateTransfersQueueFileItemCount(1);
    }

    void failure( final String errorMsg ) {
        if ( controlSocket!=null ) {
            try { controlSocket.close(); } catch ( java.io.IOException ioe) {}
        }
        controlSocket=null;

        if ( dataSocket!=null ) {
            try {
                if ( dataSocket.getClass().getName().equals("java.net.ServerSocket") )
                    ((java.net.ServerSocket)dataSocket).close();
                else if ( dataSocket.getClass().getName().equals("java.net.Socket") )
                    ((java.net.Socket)dataSocket).close();
            } catch ( java.io.IOException ioe ) {}
        }
        dataSocket=null;

        if ( automatic ) {
            if ( mainFrameRef.viewTab.uploadsTable.getValueAt(itemsRow,1).toString().equals("Annulled.")==false ) {
                mainFrameRef.fileioClassInstance.logError(getClass().getName()+"\t"+errorMsg);
                mainFrameRef.viewTab.uploadsTable.setValueAt("Upload Failed!", itemsRow, 1);
                mainFrameRef.viewTab.uploadsTable.setValueAt("", itemsRow, 2);
            }
        } else
            mainFrameRef.fileioClassInstance.logError(getClass().getName()+"\t"+errorMsg);
        
    }
    
    public void runCrux() {

        ceefee.sockets.accept acceptSocketClassInstance=null;
            
        final String originalPath=absolutePath;
        int whimsicalByteLocation;
        byte[] fileBuffer=null;
        int byteCount=0;
        int lastByteCount=0;
        int transferRate;
        final long fileLen=electedFile.length();
        long lastCheckTime=java.util.Calendar.getInstance().getTimeInMillis();


        retData=mainFrameRef.ftpClassInstance.ensureFtpControlSocketConnection(null, -1, controlSocket, hostAddress, hostPort, hostUsername, hostPassword, dataFormat, false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            failure((String)retData[0]);
            return;
        }
        controlSocket=(java.net.Socket)retData[0];
        
        retData[0]=mainFrameRef.ftpClassInstance.changeWorkingDirectory(null,sessionID, controlSocket,remotePath, false, evince);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            failure(retData[0].toString());
            return;
        }

        dataSocket=mainFrameRef.ftpClassInstance.connectFtpDataSocket(sbTabInstance,sessionID, controlSocket, connectionMethod, false)[0];
        if ( connectionMethod==1 ) { //PORT MODE
            acceptSocketClassInstance=new ceefee.sockets.accept(mainFrameRef, (java.net.ServerSocket)dataSocket);
        }

        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance,sessionID, controlSocket,"STOR "+remoteFileName, true, 0);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            failure(retData[0].toString());
            return;
        }

        if ( connectionMethod==1 ) { //PORT MODE
            dataSocket=acceptSocketClassInstance.accept()[0];
        }

        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            failure((String)retData[0]);
            return;
        }
        
        while ( byteCount<fileLen ) {
            if ( cancelAction ) return;

            fileBuffer=mainFrameRef.fileioClassInstance.getDataFromFile(absolutePath,byteCount,mainFrameRef.connectionMTU);

            if ( (automatic==false && fileBuffer.length==0) || (automatic && mainFrameRef.viewTab.uploadsTable.getValueAt(itemsRow,1).toString().equals("Annulled.")) ) {
                if ( automatic ) mainFrameRef.viewTab.removeItemsResumeAbility(1,itemsRow);
                try { ((java.net.Socket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}
                // TRY to delete partial upload
                mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance, sessionID, controlSocket,"DELE "+remoteFileName, true, (evince?2:0));

                mainFrameRef.ftpClassInstance.changeWorkingDirectory(null,sessionID,controlSocket,originalPath, false, evince);
                return;
            }

            if ( fileBuffer.length==0 ) {
                failure((String)(retData[0]="Unable to connect data socket."));
                return;
            }

            try {
                ((java.net.Socket)dataSocket).getOutputStream().write(fileBuffer);
            } catch ( java.io.IOException ioe ) {
                failure((String)(retData[0]=ioe.getMessage()));
                return;
            }
            byteCount+=mainFrameRef.connectionMTU;

            if ( (java.util.Calendar.getInstance().getTimeInMillis()-lastCheckTime)>pollingInterval ) {
                if ( automatic==false && currentstateInstance.isVisible() ) {
                    if ( lastByteCount!=0 )
                        currentstateInstance.statusLabel.setText(remoteFileName+" (" + String.valueOf((int)((byteCount/(double)fileLen)*100) + "%)"));
                    lastCheckTime=java.util.Calendar.getInstance().getTimeInMillis();
                    lastByteCount=byteCount;
                } else {
                    if ( lastByteCount!=0 ) {
                        mainFrameRef.viewTab.uploadsTable.setValueAt("(" + String.valueOf((int)((byteCount/(double)fileLen)*100) + "%)   " + mainFrameRef.utilitiesClassInstance.formatSize(byteCount, 0) + " / " + mainFrameRef.utilitiesClassInstance.formatSize(fileLen, 0) ), itemsRow, 1);
                        transferRate=(int)((byteCount-lastByteCount)/(double)((java.util.Calendar.getInstance().getTimeInMillis()-lastCheckTime)/1E3));
                        mainFrameRef.viewTab.uploadsTable.setValueAt(mainFrameRef.utilitiesClassInstance.formatSize(transferRate,2) + "/s", itemsRow, 2);
                    }
                }
                lastCheckTime=java.util.Calendar.getInstance().getTimeInMillis();
                lastByteCount=byteCount;
            }

        }
        try { ((java.net.Socket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}

        mainFrameRef.ftpClassInstance.ftpGetResponse(sbTabInstance,sessionID, controlSocket, mainFrameRef.connectionTimeout, 0);

        if ( automatic ) {
            mainFrameRef.viewTab.removeItemsResumeAbility(1,itemsRow);

            mainFrameRef.soundClassInstance.playSound("_alert2.wav");

            mainFrameRef.viewTab.uploadsTable.setValueAt("(100%)  Done.", itemsRow, 1);
            mainFrameRef.viewTab.uploadsTable.setValueAt("", itemsRow, 2);
        }

        if ( move ) {
            // delete file
        }

        if ( sbTabInstance!=null && sessionID==sbTabInstance.sessionID ) {
            if ( automatic )
                mainFrameRef.ftpClassInstance.displaydir(sbTabInstance,sessionID,controlSocket,dataSocket,true,false,false);
            
        } else
            try { (controlSocket).close(); } catch ( java.io.IOException ioe ) {}
        
        success=true;
        
        return;
    }
}
