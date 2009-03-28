
package ceefee.display;


class sb_renameremoteobject implements Runnable {
    
    private ceefee.main mainFrameRef;
    private ceefee.display.sb sbTabInstance;
    private int sessionID;
    private Object[] retData=new Object[2];
    
    
    sb_renameremoteobject( final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final int _sessionID ) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
        sessionID=_sessionID;
    }
    public void run() {
        retData=mainFrameRef.ftpClassInstance.ensureFtpControlSocketConnection(sbTabInstance, sbTabInstance.sessionID, sbTabInstance.controlSocket, sbTabInstance.hostAddress.getText(), sbTabInstance.hostPort.getText(), sbTabInstance.hostUsername.getText(), new String(sbTabInstance.hostPassword.getPassword()), sbTabInstance.getDataFormat(), false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            sbTabInstance.enableDisable(-1, 0);
            return;
        }
        sbTabInstance.controlSocket=(java.net.Socket)retData[0];

        sbTabInstance.enableDisable(-1, 1);

        String originalFile = sbTabInstance.resultsTableDataModel.getValueAt(sbTabInstance.resultsTable.getSelectedRow(), 0).toString();
        if ( mainFrameRef.utilitiesClassInstance.isLinkFormatting(originalFile) )
            originalFile=mainFrameRef.utilitiesClassInstance.removeFtpObjectLinkFormatting(originalFile,true);
        
        String destFile = javax.swing.JOptionPane.showInputDialog(mainFrameRef, "Desired name:", "Rename Object", javax.swing.JOptionPane.DEFAULT_OPTION);
        if ( destFile==null || destFile.equals("") ) {
            sbTabInstance.enableDisable(-1, 0);
            return;
        }
        if ( mainFrameRef.utilitiesClassInstance.isFolderFormatting(originalFile) )
            originalFile=originalFile.substring(2);

        Object[] retData=new Object[2];
        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance, sbTabInstance.sessionID, sbTabInstance.controlSocket, "RNFR "+originalFile, true, 1);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            sbTabInstance.enableDisable(-1, 0);
            return;
        }
        
        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance, sbTabInstance.sessionID, sbTabInstance.controlSocket, "RNTO " + destFile, true, 2);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            sbTabInstance.enableDisable(-1, 0);
            return;
        } else {
            mainFrameRef.ftpClassInstance.displaydir(sbTabInstance, sbTabInstance.sessionID, sbTabInstance.controlSocket, sbTabInstance.dataSocket, false, true, false);
            return;
        }

    }
    
}