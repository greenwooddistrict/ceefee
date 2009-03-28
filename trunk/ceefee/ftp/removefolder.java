
package ceefee.ftp;


public class removefolder extends Thread {
    private ceefee.main mainFrameRef;
    
    private ceefee.display.sb sbTabInstance;
    
    private java.net.Socket controlSocket;
    //private java.net.Socket dataSocket;

    private String _serverHeader;
    private String _hostAddress;
    private String _hostPort;
    private String _hostUsername;
    private String _hostPassword;

    private int sessionID;
    
    private String _remoteFolderPath;
    
    private int _connectionMethod;
    
    private String originalWD;
    
    Object[] retData=new Object[2];
        
    boolean cancelAction;
    
            
    public removefolder(final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final int _sessionID, final java.net.Socket _controlSocket, final java.net.Socket _dataSocket, final String _originalWD, final String __remoteFolderPath, final String __serverHeader, final String __hostAddress, final String __hostPort, final String __hostUsername, final String __hostPassword, final int __connectionMethod ) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
        sessionID=_sessionID;
        controlSocket=_controlSocket;
        _serverHeader=__serverHeader;
        _hostAddress=__hostAddress;
        _hostPort=__hostPort;
        _hostUsername=__hostUsername;
        _hostPassword=__hostPassword;
        //dataSocket=_dataSocket;
        originalWD=_originalWD;
        _remoteFolderPath=__remoteFolderPath;
        _connectionMethod=__connectionMethod;
    }
    
    public void run() {
        
        String shortFolderPath=_remoteFolderPath;
        if ( shortFolderPath.indexOf("/",shortFolderPath.indexOf("/")+1)!=-1 )
            shortFolderPath=shortFolderPath.substring(shortFolderPath.lastIndexOf("/",shortFolderPath.lastIndexOf("/")-1));

        if ( sbTabInstance!=null )
            controlSocket=sbTabInstance.controlSocket;
        
        
        mainFrameRef.currentstateInstance=new ceefee.display.currentstate(mainFrameRef, false, mainFrameRef.getX(), mainFrameRef.getY(), mainFrameRef.getSize(), ".", 12, java.awt.Color.black, javax.swing.SwingConstants.CENTER, "Removing '"+shortFolderPath+"'.", "Cancel");
        mainFrameRef.currentstateInstance.update("","Cancel");
        mainFrameRef.currentstateInstance.show();
        mainFrameRef.hide();

        retData=removeRemoteFolder(_remoteFolderPath,_serverHeader);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            mainFrameRef.currentstateInstance.close();
            mainFrameRef.show();
            mainFrameRef.requestFocus();
            mainFrameRef.ftpClassInstance.changeWorkingDirectory(null, sessionID, controlSocket, originalWD, false, false);
        } else {
            mainFrameRef.currentstateInstance.close();
            if ( sbTabInstance!=null ) sbTabInstance.addTextToStatListing(sessionID,"( Directory '" + shortFolderPath + "' successfully removed. )", 1, 2, false);
            mainFrameRef.show();
        }
            
        if ( sbTabInstance!=null )
            mainFrameRef.ftpClassInstance.displaydir(sbTabInstance,sessionID,controlSocket,sbTabInstance.dataSocket,false,false,false);
        else
            try { (controlSocket).close(); } catch ( java.io.IOException ioe ) {}
        
    }
    
    Object[] removeRemoteFolder(final String remoteFolder, final String serverHeader) {
        int objectLooper;
        String buffer="";
        Object[] retData=new Object[2];
        javax.swing.table.DefaultTableModel directoryContents;
        Object dataSocket=null;
        if ( sbTabInstance!=null )
            dataSocket=sbTabInstance.dataSocket;
        

        retData[0]=mainFrameRef.ftpClassInstance.changeWorkingDirectory(null,sessionID, controlSocket, remoteFolder, false, false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory deletion failed! <Error 0x02>\n("+"Unable to obtain directory listing. "+")", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
            return retData;
        }
        
        if ( _connectionMethod==1 ) {
            retData=mainFrameRef.ftpClassInstance.performPortOperation(null,sessionID,controlSocket,dataSocket,_hostAddress,_hostPort,_hostUsername,_hostPassword,"LIST","",false);
        } else {
            retData=mainFrameRef.ftpClassInstance.performPasvOperation(null,sessionID,controlSocket,dataSocket,_hostAddress,_hostPort,_hostUsername,_hostPassword,"LIST","",false);
        }
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory deletion failed! <Error 0x02>\n("+"Unable to obtain directory listing. "+")", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
            return retData;
        }
        dataSocket=retData[2];

        retData=mainFrameRef.ftpClassInstance.ftpGetResponse(null,sessionID, dataSocket, mainFrameRef.connectionTimeout, 0);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory deletion failed! <Error 0x03>\n("+"Corrupt directory data received. "+")", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
            return retData;
        }

        try { ((java.net.Socket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}

        directoryContents=mainFrameRef.ftpClassInstance.getDirectoryContents(null, sessionID, remoteFolder, retData[0].toString(), serverHeader);
        if ( directoryContents==null ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory deletion failed! <Error 0x05>\n("+"Unable to parse directory contents. "+")", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
            return new Object[] {"-1"};
        }

        //closing connection message
        retData=mainFrameRef.ftpClassInstance.ftpGetResponse(null,sessionID, controlSocket, mainFrameRef.connectionTimeout, 0);

        // check for sub-folders
        for ( objectLooper=-1; ++objectLooper<directoryContents.getRowCount(); ) {
            // Process Folders (value is string minus first two bytes './foo')
            buffer=directoryContents.getValueAt(objectLooper, 0).toString();
            if ( mainFrameRef.currentstateInstance.isVisible()==false || cancelAction ) { // user wants to cancel operation
                return new Object[] {"-1"};
            }
            mainFrameRef.currentstateInstance.update(buffer,"");
            
            if ( buffer.substring(0,2).equals("./") ) {
                // Ignore '.' AND '..'
                if ( buffer.substring(2).equals(".")==false && buffer.substring(2).equals("..")==false ) {
                    buffer=buffer.substring(2);
                    if ( buffer.substring(buffer.length()-1,buffer.length()).equals("/") )
                        buffer=buffer.substring(0,buffer.length()-1);
                    
                    retData=removeRemoteFolder(buffer,serverHeader);
                    if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                        cancelAction=true;
                        return retData;
                    }
                }
            } else { // Process Files
                retData=mainFrameRef.ftpClassInstance.ftpSendCommand(null, sessionID, controlSocket, "DELE "+buffer, true, 0);
                if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                    javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory deletion failed! <Error 0x07>\n(Unable to remove object '"+remoteFolder+buffer+"' )", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
                    cancelAction=true;
                    return retData;
                }
            }
        }

        mainFrameRef.ftpClassInstance.changeWorkingDirectory(null, sessionID, controlSocket, "..", false, false);
        
        retData=mainFrameRef.ftpClassInstance.deleteRemoteDirectory(sbTabInstance,sessionID,controlSocket,remoteFolder,false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") )
            return retData;
        
        return new Object[] {""};
    }
}
