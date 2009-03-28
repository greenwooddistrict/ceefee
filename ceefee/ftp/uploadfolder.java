package ceefee.ftp;


class uploadfolder extends Thread {
    
    private ceefee.main mainFrameRef;
    
    private ceefee.display.sb sbTabInstance;
  
    private int _sessionID;
    
    private java.net.Socket controlSocket;
    private Object dataSocket;

    private String _remotePath;
    
    private String _folderPath;

    private String _hostAddress;
    
    private String _hostPort;
    
    private String _hostUsername;
    
    private String _hostPassword;
    
    private String _dataFormat;
    
    private String _connectionMethod;
    
    private boolean _move;
        
    


    public uploadfolder(final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final int __sessionID, final java.net.Socket _controlSocket, final java.lang.Object _dataSocket, final String __folderPath, final String __remotePath, final String __hostAddress, final String __hostPort, final String __hostUsername, final String __hostPassword, final String __dataFormat, final String __connectionMethod, final boolean __move) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
        _sessionID=__sessionID;
        controlSocket=_controlSocket;
        dataSocket=_dataSocket;
        _folderPath=__folderPath;
        _remotePath=__remotePath;
        _hostAddress=__hostAddress;
        _hostPort=__hostPort;
        _hostUsername=__hostUsername;
        _hostPassword=__hostPassword;
        _dataFormat=__dataFormat;
        _connectionMethod=__connectionMethod;
        _move=__move;
    }
    
    public void run() {
        Object[] retData=new Object[2];
        String shortFolderName=_folderPath;
        if ( shortFolderName.indexOf(System.getProperty("file.separator"),shortFolderName.indexOf(System.getProperty("file.separator"))+1)!=-1 )
            shortFolderName=shortFolderName.substring(shortFolderName.lastIndexOf(System.getProperty("file.separator"),shortFolderName.lastIndexOf(System.getProperty("file.separator"))-1));

        if ( sbTabInstance!=null ) sbTabInstance.reactionTime=new java.util.Date().getTime();
        
        mainFrameRef.currentstateInstance=new ceefee.display.currentstate(mainFrameRef, false, mainFrameRef.getX(), mainFrameRef.getY(), mainFrameRef.getSize(), "Connecting...", 12, java.awt.Color.black, javax.swing.SwingConstants.CENTER, "CeeFee - Uploading...", "Cancel");
        mainFrameRef.currentstateInstance.update("","Cancel");
        mainFrameRef.currentstateInstance.show();
        mainFrameRef.hide();
        
        retData=uploadFolder(_sessionID, controlSocket, _folderPath, _remotePath, _hostAddress, _hostPort, _hostUsername, _hostPassword, _dataFormat, _connectionMethod, _move, !mainFrameRef.displayOverwriteConfirmations);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            mainFrameRef.currentstateInstance.close();
            mainFrameRef.show();
            mainFrameRef.requestFocus();
        } else {
            mainFrameRef.currentstateInstance.close();
            if ( sbTabInstance!=null ) {
                sbTabInstance.addTextToStatListing(_sessionID,"( '" + shortFolderName + "' was successfully uploaded to '"+_remotePath+"'.)",1,1,false);
                sbTabInstance.addTextToStatListing(_sessionID,"( completion time: "+mainFrameRef.ftpClassInstance.getElapsedTime(sbTabInstance,_sessionID)+" second(s). )", 1,2,false);
            }

            mainFrameRef.show();
        }
        
        if ( sbTabInstance!=null ) {
            retData[0]=mainFrameRef.ftpClassInstance.changeWorkingDirectory(null,_sessionID,sbTabInstance.controlSocket,_remotePath,false,false);
            mainFrameRef.ftpClassInstance.displaydir(sbTabInstance,_sessionID,sbTabInstance.controlSocket,sbTabInstance.dataSocket,false,false,false);

        } else {
            if ( controlSocket!=null )
                try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
            if ( dataSocket!=null )
                try { ((java.net.Socket)dataSocket).close();  } catch ( java.io.IOException ioe ) {}
            
        }

    }
        
    Object[] uploadFolder(final int sessionID, java.net.Socket controlSocket, String objectPath, String remotePath, final String hostAddress, final String hostPort, final String hostUsername, final String hostPassword, final String dataFormat, final String connectionMethod, final boolean move, boolean overwrite ) {
        Thread ftpUploadClassInstanceThread;
        ceefee.ftp.upload ftpUploadClassInstance;
        if ( remotePath.length()<1 || remotePath.substring(remotePath.length()-1,remotePath.length()).equals("/")==false )
            remotePath=remotePath+"/";
        if ( objectPath.length()<1 || objectPath.substring(objectPath.length()-1,objectPath.length()).equals("\\")==false )
            objectPath=objectPath+"\\";
        java.io.File localDirectory=new java.io.File(objectPath);
        java.io.File[] fileList;
        Object[] retData=new Object[2];

        
        retData=mainFrameRef.ftpClassInstance.ensureFtpControlSocketConnection(null,sessionID,controlSocket,hostAddress,hostPort,hostUsername,hostPassword,Integer.parseInt(dataFormat), false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Unable to connect control socket.", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
            return retData;
        }
        controlSocket=(java.net.Socket)retData[0];
        
        retData[0]=mainFrameRef.ftpClassInstance.changeWorkingDirectory(null,sessionID,controlSocket,remotePath,false,false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory upload failed!\n( Unable to establish working directory. )", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
            return retData;
        }

        localDirectory=new java.io.File(objectPath);
        if ( localDirectory.exists()==false || localDirectory.isDirectory()==false ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory upload failed! <Error 0x04>\n(The local data path '"+objectPath+"' is invalid. )", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
            return new Object[] {"-1"};
        }
        
        remotePath=remotePath+localDirectory.getName()+"/";

        retData=mainFrameRef.ftpClassInstance.createRemoteDirectory(null, sessionID, controlSocket, remotePath, hostAddress, hostPort, hostUsername, hostPassword, dataFormat, false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            if ( retData[0].toString().indexOf("exists")!=-1 || retData[0].toString().indexOf("failed to create")!=-1 ) {
                if ( overwrite==false && sbTabInstance!=null && javax.swing.JOptionPane.showConfirmDialog(mainFrameRef,"This object already exists on the remote host.\nWould you like to overwrite the existing object?","hey!",javax.swing.JOptionPane.YES_NO_OPTION)==javax.swing.JOptionPane.NO_OPTION ) {
                    javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory upload failed! <Error 0x03>\n(Unable to create directory '"+remotePath+"' )", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return retData;
                } else {
                    overwrite=true;
                }
            }
        }

        fileList=localDirectory.listFiles();
        for ( int fileLooper=-1; ++fileLooper<fileList.length; ) {
            if ( mainFrameRef.currentstateInstance.isVisible()==false ) { // user wants to cancel
                return new Object[] {"-1"};
            }
            mainFrameRef.currentstateInstance.update("'"+fileList[fileLooper].getName()+"'","");

            if ( fileList[fileLooper].isDirectory() ) {
                retData=uploadFolder(sessionID, controlSocket, fileList[fileLooper].getPath(), remotePath, hostAddress, hostPort, hostUsername, hostPassword, dataFormat, connectionMethod, move, overwrite);
                if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") )
                    return retData;

            } else {
                //upload(final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final int _sessionID, final String _absolutePath, final String _remotePath, final String _remoteFileName, final String _hostAddress, final String _hostPort, final String _hostUsername, final String _hostPassword, final String _dataFormat, final String _connectionMethod, final boolean _move, final boolean _resuming, final boolean _automatic) {
                ftpUploadClassInstance=new ceefee.ftp.upload(mainFrameRef,sbTabInstance,sessionID,mainFrameRef.currentstateInstance,fileList[fileLooper].getPath(),remotePath,"",hostAddress,hostPort,hostUsername,hostPassword,dataFormat,connectionMethod,move,false,false,false);
                ftpUploadClassInstanceThread=new Thread(ftpUploadClassInstance);
                ftpUploadClassInstanceThread.setPriority(Thread.MIN_PRIORITY);
                ftpUploadClassInstanceThread.start();
                while ( ftpUploadClassInstanceThread.isAlive() ) {
                    if ( ftpUploadClassInstanceThread.isInterrupted() ) {
                        ftpUploadClassInstance.cancelAction=true;
                        return new Object[] {"-1"};
                    }
                    if ( mainFrameRef.currentstateInstance.isVisible()==false ) {
                        ftpUploadClassInstance.cancelAction=true;
                        return new Object[] {"-1"};
                    }
                }

                retData=ftpUploadClassInstance.retData;
                if ( (retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1")) || ftpUploadClassInstance.success==false ) {
                    javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory upload failed!\n( file: '"+fileList[fileLooper].getPath()+"'. )", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
                    ftpUploadClassInstance.cancelAction=true;
                    return retData;
                }
                
                if ( move );
                    // delete source file
                
            }
        }

        retData[0]=mainFrameRef.ftpClassInstance.changeWorkingDirectory(null,sessionID,controlSocket,mainFrameRef.utilitiesClassInstance.getMotherPathFromPath(remotePath,"/"),false,false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory upload failed!\n(Unable to restore working directory 0x06)", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
            return retData;
        }

        return new Object[] {""};
    }
    
}
