
package ceefee.ftp;


public class establishftpconnection implements Runnable {

    private ceefee.main mainFrameRef;
    
    private ceefee.display.sb sbTabInstance;
    
    private int sessionID;
    
    private java.util.regex.Pattern regexPattern=null;
    private Object[] retData=new Object[2];
    
    private boolean establishWorkingDirectory;
    
    private boolean displayDirectory;

    

    public establishftpconnection(final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final int _sessionID, final boolean _establishWorkingDirectory, final boolean _displayDirectory) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
        sessionID=_sessionID;
        establishWorkingDirectory=_establishWorkingDirectory;
        displayDirectory=_displayDirectory;
    }

    void _portError() {
        javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"  -The port field only accepts numerical values between 0 and 65536.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
        sbTabInstance.enableDisable(0,0);
        return ;
    }
    public void run() {
        String searchItem="";
        int buffer=0;
        
        
        if ( sbTabInstance.hostDescription.getEditor().getItem()==null || sbTabInstance.hostDescription.getEditor().getItem().equals("") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"  -No host description was specified.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            sbTabInstance.enableDisable(0,0);
            return ;
        } else if ( sbTabInstance.hostAddress.getText().equals("") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"  -No host address was specified.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            sbTabInstance.enableDisable(0,0);
            return ;
        } else if ( sbTabInstance.hostPort.getText().equals("") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"  -No port number was specified.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            sbTabInstance.enableDisable(0,0);
            return ;
        } else if ( regexPattern.compile("\\d*").matcher(sbTabInstance.hostPort.getText()).matches()==false ) {
            _portError();
            return;
        }
        if ( sbTabInstance.hostUsername.getText().equals("") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"  -No username was specified.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            sbTabInstance.enableDisable(0,0);
            return ;
        } else if ( String.valueOf(sbTabInstance.hostPassword.getPassword()).equals("") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"  -No password was specified.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            sbTabInstance.enableDisable(0,0);
            return ;
        }
        if ( sbTabInstance.scourMethod==mainFrameRef.SEARCH_METHOD ) {
            if ( (searchItem=sbTabInstance.searchList.getEditor().getItem().toString()).equals("") ) {
                javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"  -No search text was found. Please enter a word (or words) in relation to the file that you are searching for.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
                sbTabInstance.enableDisable(0,0);
                return ;
            } else {
                if ( ((String)(retData[0]=String.valueOf(mainFrameRef.utilitiesClassInstance.findItemInList(searchItem, sbTabInstance.searchList)))).length()>1 && retData[0].toString().substring(0,2).equals("-1") )
                    sbTabInstance.searchList.addItem(searchItem);
                
                retData[0]="";

                sbTabInstance.searchList.setSelectedItem(searchItem);
            }
            
            mainFrameRef.storeHistoryTrail(sbTabInstance,sbTabInstance.searchList,String.valueOf(sbTabInstance.lastValidServerIndex));
        }

        sbTabInstance.hostDescriptionLock=true;
        sbTabInstance.hostDescription.getEditor().setItem(sbTabInstance.hostDescription.getEditor().getItem().toString().replaceAll("[|]"," "));
        sbTabInstance.hostDescription.getEditor().setItem(sbTabInstance.hostDescription.getEditor().getItem().toString().replaceAll("[:]"," "));
        sbTabInstance.hostDescriptionLock=false;

        sbTabInstance.hostAddress.setText(sbTabInstance.hostAddress.getText().replaceAll("[|]", " "));
        sbTabInstance.hostAddress.setText(sbTabInstance.hostAddress.getText().replaceAll("[:]", " "));

        sbTabInstance.protocolLock=true;
        sbTabInstance.protocol.setSelectedItem(sbTabInstance.protocol.getSelectedItem().toString().replaceAll("[|]", " "));
        sbTabInstance.protocol.setSelectedItem(sbTabInstance.protocol.getSelectedItem().toString().replaceAll("[:]", " "));
        sbTabInstance.protocolLock=false;

        sbTabInstance.hostUsername.setText(sbTabInstance.hostUsername.getText().replaceAll("[|]", " "));
        sbTabInstance.hostUsername.setText(sbTabInstance.hostUsername.getText().replaceAll("[:]", " "));

        sbTabInstance.hostPassword.setText(String.valueOf(sbTabInstance.hostPassword.getPassword()).replaceAll("[|]", " "));
        sbTabInstance.hostPassword.setText(String.valueOf(sbTabInstance.hostPassword.getPassword()).replaceAll("[:]", " "));

        if ( sbTabInstance.scourMethod==mainFrameRef.SEARCH_METHOD ) {
            mainFrameRef.searchTabPane.setTitleAt(sbTabInstance.tabIndex, sbTabInstance.hostDescription.getSelectedItem().toString());
        } else if ( sbTabInstance.scourMethod==mainFrameRef.BROWSE_METHOD )
            mainFrameRef.browseTabPane.setTitleAt(sbTabInstance.tabIndex, sbTabInstance.hostDescription.getSelectedItem().toString());

        mainFrameRef.addNewConnectionTab(sbTabInstance.scourMethod);
        //if ( sbTabInstance.hostDescription.getSelectedIndex()==-1 ) {
        //    sbTabInstance.hostDescription.removeItem("");
        //    sbTabInstance.hostDescription.addItem(sbTabInstance.hostDescription.getSelectedItem());
       // }

        sbTabInstance.reactionTime=new java.util.Date().getTime();

        if ( sbTabInstance.scourMethod==mainFrameRef.SEARCH_METHOD ) {
            sbTabInstance.saveSiteData(sbTabInstance.getProtocol(), sbTabInstance.hostDescription.getSelectedItem().toString(), sbTabInstance.hostAddress.getText(), sbTabInstance.hostPort.getText(), sbTabInstance.hostUsername.getText(), String.valueOf(sbTabInstance.hostPassword.getPassword()), String.valueOf(sbTabInstance.getDataFormat()), String.valueOf(sbTabInstance.getConnectionMethod()), String.valueOf(sbTabInstance.lastValidServerIndex));
            mainFrameRef.fileioClassInstance.storeDataToFile("_hosts.dat", "ftpserver", "", String.valueOf(sbTabInstance.getConnectionMethod()), 7, 7, String.valueOf(sbTabInstance.lastValidServerIndex), false);
        } else if ( sbTabInstance.scourMethod==mainFrameRef.BROWSE_METHOD ) {
            sbTabInstance.saveSiteData(sbTabInstance.getProtocol(), sbTabInstance.hostDescription.getSelectedItem().toString(), sbTabInstance.hostAddress.getText(), sbTabInstance.hostPort.getText(), sbTabInstance.hostUsername.getText(), String.valueOf(sbTabInstance.hostPassword.getPassword()), String.valueOf(sbTabInstance.getDataFormat()), String.valueOf(sbTabInstance.getConnectionMethod()), String.valueOf(sbTabInstance.lastValidServerIndex));
            mainFrameRef.fileioClassInstance.storeDataToFile("_hosts.dat", "ftpserver", "", String.valueOf(sbTabInstance.getConnectionMethod()), 7, 7, String.valueOf(sbTabInstance.lastValidServerIndex), false);
        }

        if ( sbTabInstance.getProtocol().equals(mainFrameRef.securityLayer[0]) ) { // Standard FTP
            while ( (sbTabInstance.reconnectAttempt++)<mainFrameRef.connectionAttempts ) {
                retData=mainFrameRef.ftpClassInstance.connectFtpControlSocket(sbTabInstance,sessionID,sbTabInstance.hostAddress.getText(),sbTabInstance.hostPort.getText(),sbTabInstance.hostUsername.getText(),String.valueOf(sbTabInstance.hostPassword.getPassword()),sbTabInstance.getDataFormat(),false,true);

                if ( sbTabInstance.sessionID!=sessionID ) {
                    sbTabInstance.reconnectAttempt=0;
                    sbTabInstance.killSockets();
                    return;
                } else if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                    if ( sbTabInstance.controlButton2.isEnabled()!=true ) {
                        sbTabInstance.controlButton2.setEnabled(true);
                    }
                } else {
                    sbTabInstance.controlSocket=(java.net.Socket)retData[0];
                    sbTabInstance.serverHeader=retData[1].toString();
                    if ( sbTabInstance.scourMethod==mainFrameRef.SEARCH_METHOD ) {
                        mainFrameRef.ftpClassInstance.findFtpItem(sbTabInstance,sessionID,sbTabInstance.startLocation.getText(),sbTabInstance.searchList.getEditor().getItem().toString(),true);
                    } else if ( sbTabInstance.scourMethod==mainFrameRef.BROWSE_METHOD ) {
                        if ( sbTabInstance.activityList.getItemCount()>2 && establishWorkingDirectory ) {
                            sbTabInstance.currentWD=sbTabInstance.activityList.getEditor().getItem().toString();

                            sbTabInstance.currentWD=mainFrameRef.ftpClassInstance.changeWorkingDirectory(sbTabInstance,sessionID, sbTabInstance.controlSocket, sbTabInstance.currentWD,true,true);
                            
                            if ( sbTabInstance.currentWD.length()>1 && sbTabInstance.currentWD.substring(0,2).equals("-1") ) {
                                sbTabInstance.enableDisable(2,0);
                                sbTabInstance.reconnectAttempt=0;
                                if ( sbTabInstance.controlSocket!=null && sbTabInstance.controlSocket.isConnected() ) {
                                    try { sbTabInstance.controlSocket.close(); } catch ( java.io.IOException ioe ) {}
                                }
                                return;            
                            }
                        }
                        
                        if ( displayDirectory )
                            mainFrameRef.ftpClassInstance.displaydir(sbTabInstance, sessionID, sbTabInstance.controlSocket,sbTabInstance.dataSocket,false, true, false);
                    }
                    break;
                }
                sbTabInstance.iffyConnection=true;
            }
        } else if ( sbTabInstance.getProtocol().equals(mainFrameRef.securityLayer[1]) ) { // SSL FTP
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"This feature is only available in the 'Professional' version.","Please upgrade!",javax.swing.JOptionPane.INFORMATION_MESSAGE);
            sbTabInstance.reconnectAttempt=0;
            sbTabInstance.endSession(false);

        } else if ( sbTabInstance.getProtocol().equals(mainFrameRef.securityLayer[2]) ) { //SSH2
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"This feature is only available in the 'Professional' version.","Please upgrade!",javax.swing.JOptionPane.INFORMATION_MESSAGE);
            sbTabInstance.reconnectAttempt=0;
            sbTabInstance.endSession(false);
        }
        sbTabInstance.reconnectAttempt=0;
        
    }
}