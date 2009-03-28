package ceefee.display;

public class sb_activitylistactionperformed implements Runnable {
    
    private ceefee.main mainFrameRef;
    private ceefee.display.sb sbTabInstance;
    private int sessionID;
    private java.awt.event.ActionEvent evt;
    
    
    public sb_activitylistactionperformed( final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final int _sessionID, final java.awt.event.ActionEvent _evt ) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
        sessionID=_sessionID;
        evt=_evt;
    }
    public void run() {
        if ( evt.getActionCommand().equals("comboBoxChanged")==false ) return;

        sbTabInstance.enableDisable(1,1);

        String localNewWD;
        String selectedItem;
        Object[] retData=new Object[2];


        retData=mainFrameRef.ftpClassInstance.ensureFtpControlSocketConnection(sbTabInstance,sessionID,sbTabInstance.controlSocket,sbTabInstance.hostAddress.getText(),sbTabInstance.hostPort.getText(),sbTabInstance.hostUsername.getText(),new String(sbTabInstance.hostPassword.getPassword()),sbTabInstance.getDataFormat(),true);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            sbTabInstance.enableDisable(2,0);
            return ;
        }
        sbTabInstance.controlSocket=(java.net.Socket)retData[0];
        
        if ( sbTabInstance.activityList.getEditor().getItem().toString().equals(".") ) {
            selectedItem=sbTabInstance.currentWD;
        } else if ( sbTabInstance.activityList.getEditor().getItem().toString().equals("..") ) {
            if ( sbTabInstance.currentWD==null || sbTabInstance.currentWD.equals("") ) {
                sbTabInstance.currentWD=mainFrameRef.ftpClassInstance.getWorkingDirectory(sbTabInstance, sessionID, sbTabInstance.controlSocket, true);
            }
            if ( sbTabInstance.currentWD.length()>1 && sbTabInstance.currentWD.substring(0,2).equals("-1") ) {
                sbTabInstance.currentWD="";
                sbTabInstance.enableDisable(2,0);
                return;
            }
            selectedItem=mainFrameRef.utilitiesClassInstance.getMotherPathFromPath(sbTabInstance.currentWD,"/");
        } else {
            selectedItem=sbTabInstance.activityList.getEditor().getItem().toString();
        }
        if ( ((String)(retData[0]=String.valueOf(mainFrameRef.utilitiesClassInstance.findItemInList(selectedItem, sbTabInstance.activityList)))).length()>1 && retData[0].toString().substring(0,2).equals("-1") )
            sbTabInstance.activityList.addItem(selectedItem);
            
        sbTabInstance.activityList.setSelectedItem(selectedItem);

        // Try to change to directory...
        localNewWD=mainFrameRef.ftpClassInstance.changeWorkingDirectory(sbTabInstance, sessionID,sbTabInstance.controlSocket,selectedItem,false,false);
        if ( localNewWD.length()>1 && localNewWD.substring(0,2).equals("-1") ) {
            sbTabInstance.addTextToStatListing(sessionID,"( "+"Directory not found!" +" )",1,2,false);
            sbTabInstance.enableDisable(2,0);
            return;
        }

        mainFrameRef.ftpClassInstance.displaydir(sbTabInstance,sessionID,sbTabInstance.controlSocket,sbTabInstance.dataSocket,false,true,false);

    }
}