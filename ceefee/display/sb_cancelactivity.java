package ceefee.display;

public class sb_cancelactivity extends java.lang.Thread {
    
    private ceefee.main mainFrameRef;
    private ceefee.display.sb sbTabInstance;
    
    
    sb_cancelactivity ( final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance ) {
        mainFrameRef=_mainFrameRef;
        if ( _sbTabInstance.scourMethod==mainFrameRef.SEARCH_METHOD )
            sbTabInstance=mainFrameRef.searchTab[_sbTabInstance.tabIndex];
        else if ( _sbTabInstance.scourMethod==mainFrameRef.BROWSE_METHOD )
            sbTabInstance=mainFrameRef.browseTab[_sbTabInstance.tabIndex];
            
    }
    
    public void run() {

        int tabIndex=0;
        

        /* kill sockets
         */
        sbTabInstance.killSockets();

        /* kill running threads
        */
        if ( sbTabInstance.socketConnectThread!=null && sbTabInstance.socketConnectThread.isAlive() ) {
            sbTabInstance.socketConnectThread.interrupt();
        }

        if ( sbTabInstance.establishFtpConnectionInstanceThread!=null && sbTabInstance.establishFtpConnectionInstanceThread.isAlive() ) {
            sbTabInstance.establishFtpConnectionInstanceThread.interrupt();
        }


        if ( sbTabInstance.scourMethod==mainFrameRef.SEARCH_METHOD && sbTabInstance.searching ) {
            sbTabInstance.searching=false;
        } else if ( sbTabInstance.scourMethod==mainFrameRef.BROWSE_METHOD || (sbTabInstance.scourMethod==mainFrameRef.SEARCH_METHOD && sbTabInstance.searching==false)) {
            if ( (sbTabInstance.scourMethod==mainFrameRef.SEARCH_METHOD && ((tabIndex=mainFrameRef.searchTabPane.indexOfTab("< New >"))!=-1 && mainFrameRef.searchTabPane.getTabCount()>1)) || (sbTabInstance.scourMethod==mainFrameRef.BROWSE_METHOD && ((tabIndex=mainFrameRef.browseTabPane.indexOfTab("< New >"))!=-1 && mainFrameRef.browseTabPane.getTabCount()>1)) ) {
                sbTabInstance.refreshFtpSettings();
                mainFrameRef.removeTabPaneTab(sbTabInstance.scourMethod,tabIndex);
            }

            if ( sbTabInstance.scourMethod==mainFrameRef.SEARCH_METHOD )
                mainFrameRef.searchTabPane.setTitleAt(sbTabInstance.tabIndex, "< New >");
            else if ( sbTabInstance.scourMethod==mainFrameRef.BROWSE_METHOD )
                mainFrameRef.browseTabPane.setTitleAt(sbTabInstance.tabIndex, "< New >");

            sbTabInstance.enableDisable(0, 0);

        }

    }      
}