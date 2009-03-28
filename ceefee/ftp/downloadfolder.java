package ceefee.ftp;


class downloadfolder extends Thread {
    
    private ceefee.main mainFrameRef;
    
    private ceefee.display.sb sbTabInstance;
    private int _sessionID;
    
    private java.net.Socket controlSocket;
    private Object dataSocket;

    private String _remoteObject;
    
    private String _remotePath;
    
    private String _localPath;

    private String _serverHeader;
    
    private String _hostAddress;
    
    private String _hostPort;
    
    private String _hostUsername;
    
    private String _hostPassword;
    
    private String _dataFormat;
    
    private String _connectionMethod;
    


    public downloadfolder(final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final int __sessionID, final java.net.Socket _controlSocket, final java.lang.Object _dataSocket, final String __localPath, final String __remoteObject, final String __remotePath, final String __serverHeader, final String __hostAddress, final String __hostPort, final String __hostUsername, final String __hostPassword, final String __dataFormat, final String __connectionMethod) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
        _sessionID=__sessionID;
        controlSocket=_controlSocket;
        dataSocket=_dataSocket;
        _localPath=__localPath;
        _remoteObject=__remoteObject;
        _remotePath=__remotePath;
        _serverHeader=__serverHeader;
        _hostAddress=__hostAddress;
        _hostPort=__hostPort;
        _hostUsername=__hostUsername;
        _hostPassword=__hostPassword;
        _dataFormat=__dataFormat;
        _connectionMethod=__connectionMethod;
    }
    
    public void run() {
        Object[] retData=new Object[2];
        String shortFolderName=_remotePath;
        if ( shortFolderName.indexOf(System.getProperty("file.separator"),shortFolderName.indexOf(System.getProperty("file.separator"))+1)!=-1 )
            shortFolderName=shortFolderName.substring(shortFolderName.lastIndexOf(System.getProperty("file.separator"),shortFolderName.lastIndexOf(System.getProperty("file.separator"))-1));

        if ( sbTabInstance!=null ) sbTabInstance.reactionTime=new java.util.Date().getTime();
        
        mainFrameRef.currentstateInstance=new ceefee.display.currentstate(mainFrameRef, false, mainFrameRef.getX(), mainFrameRef.getY(), mainFrameRef.getSize(), "Connecting...", 12, java.awt.Color.black, javax.swing.SwingConstants.CENTER, "CeeFee - Downloading...", "Cancel");
        mainFrameRef.currentstateInstance.update("","Cancel");
        mainFrameRef.currentstateInstance.show();
        mainFrameRef.hide();
        
        retData=downloadFolder(_sessionID, mainFrameRef.currentstateInstance, controlSocket, _remoteObject, _remotePath, _localPath, _serverHeader, _hostAddress, _hostPort, _hostUsername, _hostPassword, _dataFormat, _connectionMethod, !mainFrameRef.displayOverwriteConfirmations);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            mainFrameRef.currentstateInstance.close();
            mainFrameRef.show();
            mainFrameRef.requestFocus();
        } else {
            mainFrameRef.currentstateInstance.close();
            if ( sbTabInstance!=null ) {
                sbTabInstance.addTextToStatListing(_sessionID,"( '"+_remoteObject+"' was successfully downloaded from '"+_remotePath+"'.)", 1, 1, false);
                sbTabInstance.addTextToStatListing(_sessionID,"( completion time: "+mainFrameRef.ftpClassInstance.getElapsedTime(sbTabInstance,_sessionID)+" second(s). )", 1,2,false);
            }
            
            mainFrameRef.show();
        }
        
    }

    Object[] downloadFolder(final int sessionID, final ceefee.display.currentstate currentstateInstance, java.net.Socket controlSocket, String remoteObject, String remoteObjectPath, String localPath, String serverHeader, final String hostAddress, final String hostPort, final String hostUsername, final String hostPassword, final String dataFormat, final String connectionMethod, final boolean overwrite) {
        Thread ftpDownloadClassInstanceThread;
        ceefee.ftp.download ftpDownloadClassInstance=null;
        Object[] retData=new Object[2];
        int objectLooper;
        String whimsicalString="";
        javax.swing.table.DefaultTableModel directoryContents=new javax.swing.table.DefaultTableModel();
        int folder0link1file2;

        
        retData=mainFrameRef.ftpClassInstance.ensureFtpControlSocketConnection(null, sessionID, controlSocket, hostAddress,hostPort,hostUsername,hostPassword,Integer.parseInt(dataFormat), false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Unable to connect control socket.", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
            return retData;
        }
        controlSocket=(java.net.Socket)retData[0];
        if ( serverHeader.equals("") ) {
            serverHeader=retData[1].toString();
        }

        if ( remoteObjectPath.length()>1 && remoteObjectPath.substring(remoteObjectPath.length()-1).equals("/")==false )
            remoteObjectPath=remoteObjectPath+"/";
        
        if ( remoteObject.length()>0 && remoteObject.substring(0,1).equals("/") )
            remoteObject=remoteObject.substring(1);

        if ( localPath.substring(localPath.length()-1).equals("\\")==false )
            localPath=localPath+"\\";
      
        retData[0]=mainFrameRef.ftpClassInstance.changeWorkingDirectory(null,sessionID,controlSocket,remoteObjectPath+remoteObject,false,false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Error 0x01: Unable to establish working directory.\n"+retData[0], "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
            return retData;
        }

        if ( connectionMethod.equals("1") ) {
            retData=mainFrameRef.ftpClassInstance.performPortOperation(null,sessionID,controlSocket,dataSocket,hostAddress,hostPort,hostUsername,hostPassword,"LIST","",false);
        } else {
            retData=mainFrameRef.ftpClassInstance.performPasvOperation(null,sessionID,controlSocket,dataSocket,hostAddress,hostPort,hostUsername,hostPassword,"LIST","",false);
        }
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory download failed! <Error 0x02>\n("+"Unable to obtain directory listing. "+")", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
            return retData;
        }
        dataSocket=retData[2];

        retData=mainFrameRef.ftpClassInstance.ftpGetResponse(null,sessionID, dataSocket, mainFrameRef.connectionTimeout, 0);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory download failed! <Error 0x03>\n("+"Corrupt directory data received. "+")", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
            return retData;
        }

        directoryContents=mainFrameRef.ftpClassInstance.getDirectoryContents(null, sessionID, remoteObjectPath, retData[0].toString(), serverHeader);

        try { ((java.net.Socket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}

        //closing connection message
        mainFrameRef.ftpClassInstance.ftpGetResponse(null, sessionID, controlSocket, 1, 0);
        
        // check for sub-folders
        for ( objectLooper=-1; ++objectLooper<directoryContents.getRowCount(); ) {
            // Process Folders (value is string minus first two bytes './foo')
            whimsicalString=directoryContents.getValueAt(objectLooper, 0).toString();

            if ( mainFrameRef.currentstateInstance.isVisible()==false || (ftpDownloadClassInstance!=null && ftpDownloadClassInstance.cancelAction) ) // user wants to cancel operation
                return new Object[] {"-1"};
            
            mainFrameRef.currentstateInstance.update(whimsicalString,"");

            if ( whimsicalString.substring(0,2).equals("./") ) {
                folder0link1file2=0;
                if ( whimsicalString.substring(2).equals(".")==false && whimsicalString.substring(2).equals("..")==false ) {
                    if ( whimsicalString.substring(whimsicalString.length()-1,whimsicalString.length()).equals("/") )
                        whimsicalString=whimsicalString.substring(0,whimsicalString.length()-1);
                    
                    whimsicalString=whimsicalString.substring(1);

                } else
                    continue;
                
            } else if ( mainFrameRef.utilitiesClassInstance.isLinkFormatting(whimsicalString) ) {
                folder0link1file2=1; //maybe
                whimsicalString=mainFrameRef.utilitiesClassInstance.removeFtpObjectLinkFormatting(whimsicalString,true);
                
            } else {
                folder0link1file2=2;
                
            }
            
            new java.io.File(localPath+remoteObject).mkdir();

            if ( folder0link1file2==0 || folder0link1file2==1 ) {
                // Ignore '.' AND '..'
                retData=downloadFolder(sessionID, currentstateInstance, controlSocket, whimsicalString, remoteObjectPath+remoteObject, localPath+remoteObject, serverHeader, hostAddress, hostPort, hostUsername, hostPassword, dataFormat, connectionMethod, overwrite);
                if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                    if ( folder0link1file2==0 )
                        return retData;
                    
                } else
                    continue;

            }
            if ( folder0link1file2==1 || folder0link1file2==2 ) {
                ftpDownloadClassInstance=new ceefee.ftp.download(mainFrameRef, new ceefee.display.sb(mainFrameRef,0,0),sessionID,currentstateInstance,whimsicalString,remoteObjectPath+remoteObject,"",localPath+remoteObject,0,hostAddress,hostPort,hostUsername,hostPassword,dataFormat,connectionMethod,true,false,false);
                ftpDownloadClassInstanceThread=new Thread(ftpDownloadClassInstance);
                ftpDownloadClassInstanceThread.setPriority(Thread.MIN_PRIORITY);
                ftpDownloadClassInstanceThread.start();
                while ( ftpDownloadClassInstanceThread.isAlive() ) {
                    if ( ftpDownloadClassInstanceThread.isInterrupted() ) return new Object[] {"-1"};
                    if ( mainFrameRef.currentstateInstance.isVisible()==false ) {
                        ftpDownloadClassInstance.cancelAction=true;
                        return new Object[] {"-1"};
                    }
                }
                retData=ftpDownloadClassInstance.retData;
                if ( (retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1")) || ftpDownloadClassInstance.success==false ) {
                    javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory download failed!\n( file: '"+whimsicalString+"'. )", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
                    ftpDownloadClassInstance.cancelAction=true;
                    return retData;
                }
                
            }

        }
        
        mainFrameRef.ftpClassInstance.changeWorkingDirectory(null, sessionID, controlSocket, "..", false, false);
        
        try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
        
        return new Object[] {""};

    }
    
}
