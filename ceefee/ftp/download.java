package ceefee.ftp;



public class download extends Thread {

    private ceefee.main mainFrameRef;
    
    private int sessionID;
    
    private ceefee.display.currentstate currentstateInstance;
        
    private String localFileName=null;
    private int itemsRow;

    private final int refreshRate                 =8000;
    
    private String localPath;
    private java.io.File electedFile;
    private String remoteFileName;
    private String remotePath;
    private String hostAddress;
    private String hostPort;
    private String hostUsername;
    private String hostPassword;
    private int dataFormat;
    private int connectionMethod;
    boolean automatic;
    private boolean exec;
    
    public Object[] retData=new Object[2];
            
    long fileLen;

    ceefee.sockets.accept acceptSocketClassInstance=null;
    
    private java.net.Socket controlSocket=null;
    private Object dataSocket=null;
    
    boolean cancelAction;
    
    boolean success;
    

    download(final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final int _sessionID, final ceefee.display.currentstate _currentstateInstance, final String _remoteFileName, String _remotePath, final String _localFileName, String _localPath, final int _fileLen, final String _hostAddress, final String _hostPort, final String _hostUsername, final String _hostPassword, final String _dataFormat, final String _connectionMethod, final boolean overrideOverwriteConfirmation, final boolean _automatic, final boolean _exec) {
        mainFrameRef=_mainFrameRef;
        //sbTabInstance=_sbTabInstance;
        sessionID=_sessionID;
        currentstateInstance=_currentstateInstance;
        localFileName=_localFileName;
        if ( _localPath.substring(_localPath.length()-1).equals("\\")==false )
            _localPath=_localPath+"\\";
        localPath=_localPath;
        remoteFileName=_remoteFileName;
        if ( _remotePath.substring(_remotePath.length()-1).equals("/")==false )
            _remotePath=_remotePath+"/";
        remotePath=_remotePath;
        fileLen=_fileLen;
        hostAddress=_hostAddress;
        hostPort=_hostPort;
        hostUsername=_hostUsername;
        hostPassword=_hostPassword;
        dataFormat=Integer.parseInt(_dataFormat);
        connectionMethod=Integer.parseInt(_connectionMethod);
        automatic=_automatic;
        exec=_exec;

        if ( (electedFile=new java.io.File(localPath)).exists()==false ) {
            if ( electedFile.mkdir()==false ) {
                failure("Unable to create directory.");
                return;
            }
        }

        electedFile=new java.io.File(localPath+remoteFileName);
        if ( electedFile.exists() ) {
            if ( overrideOverwriteConfirmation==false && mainFrameRef.displayOverwriteConfirmations && javax.swing.JOptionPane.showConfirmDialog(mainFrameRef, "The selected object already exists on your computer.\nWould you like to overwrite the pre-existing file?", "whoops", javax.swing.JOptionPane.YES_NO_OPTION)==javax.swing.JOptionPane.NO_OPTION ) {
                electedFile=new java.io.File(localPath+mainFrameRef.utilitiesClassInstance.getFreeFileName(localPath, electedFile.getName()));
            }
        }
        if ( automatic ) {
            if ( (itemsRow=mainFrameRef.utilitiesClassInstance.findItemInTable(electedFile.getName(), mainFrameRef.viewTab.downloadsTable))==-1 ) {
                mainFrameRef.viewTab.addToDownloadsTable(electedFile.getName());
                itemsRow=mainFrameRef.utilitiesClassInstance.findItemInTable(electedFile.getName(), (javax.swing.JTable)mainFrameRef.viewTab.downloadsTable);
            } else {
                mainFrameRef.viewTab.downloadsTable.setValueAt("", itemsRow, 1);
                mainFrameRef.viewTab.downloadsTable.setValueAt("", itemsRow, 2);
            }
            
            mainFrameRef.viewTab.downloadsTable.setValueAt("Connecting...", itemsRow, 1);
                
            addResumeAbility();
        }
        
    }

    public void run() {
        int strike=0;
        do {
            
            if ( automatic ) {
                if ( mainFrameRef.viewTab.downloadsTable.getValueAt(itemsRow,1).toString().equals("Annulled.") || mainFrameRef.viewTab.downloadsTable.getValueAt(itemsRow,1).toString().equals("(100%)  Done.") )
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

                if ( (++strike)==5 ) { // do not keep wasting bandwidth/resources
                    mainFrameRef.fileioClassInstance.logError(getClass().getName()+"\t"+retData[0].toString());
                    return;
                }
            //}
            
        } while ( success==false && cancelAction==false );
    }
    
    void addResumeAbility() {
        // store information for resume capability
        final String dataFile="_downloads.dat";
        
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFile, "item", "", electedFile.getName(), 1, 12, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFile, "item", "", remotePath, 2, 12, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFile, "item", "", String.valueOf(localFileName), 3, 12, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFile, "item", "", localPath, 4, 12, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFile, "item", "", String.valueOf(fileLen), 5, 12, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFile, "item", "", hostAddress, 6, 12, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFile, "item", "", hostAddress, 7, 12, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFile, "item", "", hostPort, 8, 12, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFile, "item", "", hostUsername, 9, 12, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFile, "item", "", hostPassword, 10, 12, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFile, "item", "", String.valueOf(dataFormat), 11, 12, String.valueOf(itemsRow), false);
        mainFrameRef.fileioClassInstance.storeDataToFile(dataFile, "item", "", String.valueOf(connectionMethod), 12, 12, String.valueOf(itemsRow), false);
        
        mainFrameRef.viewTab.updateTransfersQueueFileItemCount(0);
    }

    void failure( final String errorMsg ) {
        if ( acceptSocketClassInstance!=null )
            try { ((java.net.Socket)acceptSocketClassInstance.accept()[0]).close(); } catch ( java.io.IOException ioe ) {}
            
        if ( controlSocket!=null )
            try { controlSocket.close(); } catch ( java.io.IOException ioe) {}
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
            if ( mainFrameRef.viewTab.downloadsTable.getValueAt(itemsRow,1).toString().equals("Annulled.")==false ) {
                mainFrameRef.fileioClassInstance.logError(getClass().getName()+"\t"+errorMsg);
                mainFrameRef.viewTab.downloadsTable.setValueAt("download Failed!", itemsRow, 1);
                mainFrameRef.viewTab.downloadsTable.setValueAt("", itemsRow, 2);
            }
        } else
            mainFrameRef.fileioClassInstance.logError(getClass().getName()+"\t"+errorMsg);

    }

    void runCrux() {
        byte[] transmission=new byte[mainFrameRef.connectionMTU];
        int whimsicalByteLocation;
        long byteCount=0;
        long lastByteCount=0;
        int transferRate;
        int bytesReceived;
        long lastCheckTime=java.util.Calendar.getInstance().getTimeInMillis();
        Thread threadFtpDownload;



        retData=mainFrameRef.ftpClassInstance.ensureFtpControlSocketConnection(null,sessionID,controlSocket,hostAddress,hostPort,hostUsername,hostPassword,dataFormat, false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            failure((String)retData[0]);
            return;
        }
        controlSocket=(java.net.Socket)retData[0];

        retData[0]=mainFrameRef.ftpClassInstance.changeWorkingDirectory(null,-1,controlSocket,remotePath,false,true);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            failure(retData[0].toString());
            return;
        }

        retData=mainFrameRef.ftpClassInstance.connectFtpDataSocket(null, sessionID, controlSocket, connectionMethod, false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            failure(retData[0].toString());
            return;
        }
        dataSocket=retData[0];

        if ( connectionMethod==1 ) //1==PORT MODE
            acceptSocketClassInstance=new ceefee.sockets.accept(mainFrameRef, (java.net.ServerSocket)dataSocket);

        if ( electedFile.length()>0 && electedFile.length()<fileLen ) { // file is partially downloaded so finish it
            retData=mainFrameRef.ftpClassInstance.ftpSendCommand(null,-1,controlSocket,"REST " + electedFile.length(), true, 0);
            if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                byteCount=0; // start from byte 0 ; resume failed
            } else {
                byteCount=electedFile.length();
            }
        }

        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(null, -1, controlSocket,"RETR " + electedFile.getName(), true, 0);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            failure(retData[0].toString());
            return;
        }

        if ( fileLen==-1 ) {
            // try to get file size
            if ( retData[0].toString().indexOf("(")!=-1 && retData[0].toString().indexOf("bytes)")!=-1 ) {
                // extract file size
                whimsicalByteLocation=retData[0].toString().indexOf(" bytes)");
                fileLen=Long.parseLong(retData[0].toString().substring(retData[0].toString().lastIndexOf("(",whimsicalByteLocation)+1,whimsicalByteLocation));
            }
        }

        if ( connectionMethod==1 ) { //1==PORT
            dataSocket=acceptSocketClassInstance.accept()[0];
        }

        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            failure(retData[0].toString());
            return;
        }

        try {
            mainFrameRef.fileioClassInstance.storeDataToFile(electedFile.getPath(), "".getBytes(), 0, 0, false); // for 0 length files
            while ( (bytesReceived=((java.net.Socket)dataSocket).getInputStream().read(transmission))!=-1 ) {
                if ( cancelAction ) return;
                mainFrameRef.fileioClassInstance.storeDataToFile(electedFile.getPath(), transmission, byteCount, bytesReceived, false);

                if ( (automatic==false && bytesReceived==0) || (automatic && mainFrameRef.viewTab.downloadsTable.getValueAt(itemsRow,1).toString().equals("Annulled.")) ) {
                    if ( automatic )
                        mainFrameRef.viewTab.removeItemsResumeAbility(0,itemsRow);
                    electedFile.delete();
                    return;
                }

                byteCount=electedFile.length(); //+=bytesReceived;
                if ( (java.util.Calendar.getInstance().getTimeInMillis()-lastCheckTime)>refreshRate ) {
                    if ( automatic==false && currentstateInstance.isVisible() ) {
                        if ( byteCount!=0 ) {
                            currentstateInstance.statusLabel.setText(remoteFileName+" (" + mainFrameRef.utilitiesClassInstance.formatSize(Double.parseDouble(String.valueOf(byteCount)),1)+")");
                        }
                    } else {
                        if ( fileLen==-1 ) { // can't show a 'percentage' or 'velocity'
                            mainFrameRef.viewTab.downloadsTable.setValueAt(mainFrameRef.utilitiesClassInstance.formatSize(byteCount,2), itemsRow, 1);
                        } else {
                            if ( lastByteCount!=0 ) {
                                mainFrameRef.viewTab.downloadsTable.setValueAt("(" + String.valueOf((int)((byteCount/(double)fileLen)*100) + "%)   " + mainFrameRef.utilitiesClassInstance.formatSize(byteCount, 0) + " / " + mainFrameRef.utilitiesClassInstance.formatSize(fileLen, 0) ), itemsRow, 1);
                                transferRate=(int)((byteCount-lastByteCount)/(double)((java.util.Calendar.getInstance().getTimeInMillis()-lastCheckTime)/1E3));
                                mainFrameRef.viewTab.downloadsTable.setValueAt(mainFrameRef.utilitiesClassInstance.formatSize(transferRate,2) + "/s", itemsRow, 2);
                            }
                        }
                    }
                    lastCheckTime=java.util.Calendar.getInstance().getTimeInMillis();
                    lastByteCount=byteCount;
                }
            }
        } catch ( java.io.IOException ioe ) {
            failure((String)(retData[0]=ioe.getMessage()));
            return;
        }

        try { ((java.net.Socket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}

        mainFrameRef.ftpClassInstance.ftpGetResponse(null, -1, controlSocket, mainFrameRef.connectionTimeout, 0);
        
        try { ((java.net.Socket)controlSocket).close(); } catch ( java.io.IOException ioe ) {}
        
        if ( automatic ) {
            mainFrameRef.viewTab.removeItemsResumeAbility(0,itemsRow);
        
            mainFrameRef.soundClassInstance.playSound("_alert1.wav");
            
            mainFrameRef.viewTab.downloadsTable.setValueAt("(100%)  Done.", itemsRow, 1);
            mainFrameRef.viewTab.downloadsTable.setValueAt("", itemsRow, 2);
        }
        
        if ( localFileName!=null )
            electedFile.renameTo(new java.io.File(localPath+localFileName));
        
        if ( automatic && exec )
            mainFrameRef.utilitiesClassInstance.system(electedFile.getPath());
        
        success=true;
        return;
    }
    
}
