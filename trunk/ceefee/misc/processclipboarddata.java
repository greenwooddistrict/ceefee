
package ceefee.misc;


public class processclipboarddata implements Runnable {
    
    private ceefee.main mainFrameRef;
    private ceefee.display.sb sbTabInstance;
    
    
    public processclipboarddata( final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance ) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
    }
    
    public void run() {
        if ( mainFrameRef.clipboardData!=null || sbTabInstance.scourMethod==mainFrameRef.SEARCH_METHOD ) return;
        
        int sessionID=sbTabInstance.sessionID;
        Object[] retData=new Object[2];
        String newHostAddress;
        final int specifierLength            =6;
        String whimsicalString;
        int whimsicalByteLocation;

        /* Detect if the user has a FTP address in clipboard
        */
        mainFrameRef.clipboardData=mainFrameRef.utilitiesClassInstance.getClipboardData()[0].toString();
        if ( mainFrameRef.clipboardData!=null && mainFrameRef.clipboardData.equals("")==false ) {
            if ( mainFrameRef.clipboardData.length()>5 && mainFrameRef.clipboardData.substring(0,6).equals("ftp://") ) {
                if ( javax.swing.JOptionPane.showConfirmDialog(mainFrameRef,"A File Transfer Protocol URL was found in your clipboard. Would you like to connect to this URL?","hmmm",javax.swing.JOptionPane.YES_NO_OPTION)==javax.swing.JOptionPane.YES_OPTION ) {
                    sbTabInstance.endSession(true);
                    sessionID=sbTabInstance.sessionID;

                    mainFrameRef.clipboardData=mainFrameRef.clipboardData.substring(specifierLength);

                    sbTabInstance.hostDescriptionLock=true;
                    if ( (whimsicalByteLocation=mainFrameRef.clipboardData.indexOf("."))!=-1 && mainFrameRef.clipboardData.indexOf(".",whimsicalByteLocation+1)!=-1 ) {
                        whimsicalString=mainFrameRef.clipboardData.substring(whimsicalByteLocation+1,mainFrameRef.clipboardData.indexOf(".",whimsicalByteLocation+1));
                    } else {
                        if ( mainFrameRef.clipboardData.indexOf("/",specifierLength)==-1 )
                            whimsicalString=mainFrameRef.clipboardData;
                        else
                            whimsicalString=mainFrameRef.clipboardData.substring(0,mainFrameRef.clipboardData.indexOf("/"));
                    }

                    retData[0]=String.valueOf(mainFrameRef.utilitiesClassInstance.findItemInList(whimsicalString,sbTabInstance.hostDescription));
                    if ( retData[0]==null || (retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1")) ) {
                        sbTabInstance.hostDescription.addItem(whimsicalString);
                        retData[0]=whimsicalString;
                    } else {
                        try {
                            sbTabInstance.hostDescription.setSelectedIndex(Integer.parseInt(retData[0].toString()));
                        } catch ( java.lang.NumberFormatException nfe ) {}
                    }
                    sbTabInstance.hostDescriptionLock=false;
                    if ( mainFrameRef.clipboardData.indexOf("/",specifierLength)!=-1 ) {
                        sbTabInstance.hostAddress.setText(mainFrameRef.clipboardData.substring(0,mainFrameRef.clipboardData.indexOf("/",specifierLength)));
                    } else {
                        sbTabInstance.hostAddress.setText(mainFrameRef.clipboardData);
                    }
                    
                    if ( (whimsicalByteLocation=mainFrameRef.clipboardData.indexOf("/",specifierLength))!=-1 ) {
                        if ( (whimsicalString=mainFrameRef.clipboardData.substring(whimsicalByteLocation)).length()>0 ) {
                            sbTabInstance.login(true,false,false);
                            mainFrameRef.ftpClassInstance.processUnknownItem(sbTabInstance,null,mainFrameRef.utilitiesClassInstance.getObjectFromPath(whimsicalString,"/"),mainFrameRef.utilitiesClassInstance.getRootPathFromPath(whimsicalString,"/"));
                            return;
                        }
                    }
                    sbTabInstance.login(true,true,true);

                }
            }
        }
    }
}