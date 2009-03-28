
package ceefee.display;


class sb_resultstablemouseclicked implements Runnable {
    private ceefee.main mainFrameRef;
    private ceefee.display.sb sbTabInstance;
    private java.awt.event.MouseEvent evt;
    
    sb_resultstablemouseclicked( final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final java.awt.event.MouseEvent _evt ) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
        evt=_evt;
    }
    public void run() {
        if ( sbTabInstance.resultsTable.getSelectedColumn()==-1 || sbTabInstance.resultsTable.getSelectedRow()==-1 ) {
            if ( sbTabInstance.resultsTable.getRowCount()>0 ) {
                sbTabInstance.resultsTable.setRowSelectionInterval(0,0);
                sbTabInstance.resultsTable.setColumnSelectionInterval(0,0);
                sbTabInstance.enableFileMenuOptions(1);                
            } else {
                sbTabInstance.enableFileMenuOptions(0);
            }
        } else {
            sbTabInstance.enableFileMenuOptions(1);
        }

        Object[] retData=new Object[2];
        int intBuffer;


        if ( evt!=null && evt.getClickCount()==1 && evt.getButton()==evt.BUTTON3 ) {
            intBuffer=sbTabInstance.resultsTable.rowAtPoint(evt.getPoint());
            if ( intBuffer<sbTabInstance.resultsTable.getRowCount() && intBuffer!=-1 ) {
                sbTabInstance.resultsTable.setRowSelectionInterval(intBuffer,intBuffer);
                sbTabInstance.resultsTable.setColumnSelectionInterval(0,sbTabInstance.resultsTable.getColumnCount()-1);
            }
            sbTabInstance.ftpOptionsMenu.show(sbTabInstance.resultsTable,evt.getX(),evt.getY());
            sbTabInstance.ftpOptionsMenu.repaint();
        } else if ( evt.getClickCount()==2 && evt.getButton()==evt.BUTTON1 ) {
            if ( sbTabInstance.resultsTable.getRowCount()==0 ) return;
            sbTabInstance.enableDisable(1,1);
            sbTabInstance.reactionTime=new java.util.Date().getTime();

            retData=mainFrameRef.ftpClassInstance.ensureFtpControlSocketConnection(sbTabInstance,sbTabInstance.sessionID,sbTabInstance.controlSocket,sbTabInstance.hostAddress.getText(),sbTabInstance.hostPort.getText(),sbTabInstance.hostUsername.getText(),new String(sbTabInstance.hostPassword.getPassword()),sbTabInstance.getDataFormat(), true);
            if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                sbTabInstance.enableDisable(2,0);
                return;
            }
            sbTabInstance.controlSocket=(java.net.Socket)retData[0];
            
            retData[0]=String.valueOf(sbTabInstance.resultsTableDataModel.getValueAt(sbTabInstance.resultsTable.getSelectedRow(),0));
            if ( mainFrameRef.utilitiesClassInstance.isFolderFormatting(retData[0].toString()) ) {
                retData[0]=mainFrameRef.ftpClassInstance.changeWorkingDirectory(sbTabInstance,sbTabInstance.sessionID, sbTabInstance.controlSocket, sbTabInstance.currentWD+retData[0].toString().substring(2),true,true);
                if ( retData[0].toString().indexOf("Permission denied")!=-1 || (retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1")) ) {
                    sbTabInstance.enableDisable(2,0);
                } else {
                    mainFrameRef.ftpClassInstance.displaydir(sbTabInstance,sbTabInstance.sessionID,sbTabInstance.controlSocket,sbTabInstance.dataSocket,false,true,false);
                }

            } else if ( mainFrameRef.utilitiesClassInstance.isLinkFormatting(retData[0].toString()) ) {
                retData[0]=mainFrameRef.utilitiesClassInstance.removeFtpObjectLinkFormatting(retData[0].toString(),true);
                mainFrameRef.ftpClassInstance.processUnknownItem(sbTabInstance,null,retData[0].toString(),sbTabInstance.resultsTableDataModel.getValueAt(sbTabInstance.resultsTable.getSelectedRow(),3).toString());

            } else { // File
                sbTabInstance.enableDisable(2,0);
                sbTabInstance._startFtpDownload(sbTabInstance.resultsTableDataModel.getValueAt(sbTabInstance.resultsTable.getSelectedRow(),0).toString(),sbTabInstance.resultsTableDataModel.getValueAt(sbTabInstance.resultsTable.getSelectedRow(),3).toString(),false,false);
                mainFrameRef.showPanel(2);

            }

        }
    }
}