
package ceefee.display;


class sb_upbuttonactionperformed implements Runnable {

    private ceefee.main mainFrameRef;
    private ceefee.display.sb sbTabInstance;
    private int sessionID;
    
    
    sb_upbuttonactionperformed( final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final int _sessionID ) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
        sessionID=_sessionID;
    }
    public void run() {
        if ( sbTabInstance.controlButton1.isEnabled() || sbTabInstance.busy==true )
            return;
        else
            sbTabInstance.enableDisable(1,1);

        String localNewWD;
        Object[] retData=new Object[2];
        


        retData=mainFrameRef.ftpClassInstance.ensureFtpControlSocketConnection(sbTabInstance, sessionID, sbTabInstance.controlSocket, sbTabInstance.hostAddress.getText(), sbTabInstance.hostPort.getText(), sbTabInstance.hostUsername.getText(), new String(sbTabInstance.hostPassword.getPassword()), sbTabInstance.getDataFormat(), true);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            sbTabInstance.enableDisable(2,0);
            return;
        }
        sbTabInstance.controlSocket=(java.net.Socket)retData[0];

        if ( sbTabInstance.currentWD.equals("") ) {
            sbTabInstance.currentWD=mainFrameRef.ftpClassInstance.getWorkingDirectory(sbTabInstance, sessionID, sbTabInstance.controlSocket, true);
        }
        if ( sbTabInstance.currentWD.substring(sbTabInstance.currentWD.length()-1).equals("/")==false ) {
            sbTabInstance.currentWD="";
            sbTabInstance.enableDisable(2,0);
            return;
        }
        
        localNewWD=mainFrameRef.utilitiesClassInstance.getMotherPathFromPath(sbTabInstance.currentWD,"/");
        sbTabInstance.activityListLock=true;
        sbTabInstance.activityList.setSelectedItem(localNewWD);
        sbTabInstance.activityListLock=false;

        retData[0]=mainFrameRef.ftpClassInstance.changeWorkingDirectory(sbTabInstance,sessionID, sbTabInstance.controlSocket,localNewWD,true,true);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            sbTabInstance.enableDisable(2,0);
            return;
        } else {
            mainFrameRef.ftpClassInstance.displaydir(sbTabInstance,sessionID,sbTabInstance.controlSocket,sbTabInstance.dataSocket,false,true,false);
            return;
        }
    }
}