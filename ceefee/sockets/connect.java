
package ceefee.sockets;


public class connect implements Runnable {
    
    private ceefee.main mainFrameRef;
    private ceefee.display.sb sbTabInstance;

    private int sessionID;
    private String hostAddy;
    private int hostPort;
    private boolean evince;

    private java.lang.Thread _connectThread;
    
    java.net.Socket newSocket=null;

        
    connect(final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final int _sessionID, final java.lang.Thread _connectThread, final String _hostAddy, final String _hostPort, final boolean _evince ) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
        sessionID=_sessionID;
        hostAddy=_hostAddy;
        hostPort=Integer.parseInt(_hostPort);
        evince=_evince;
    }
            
    public void run() {
        final java.net.InetSocketAddress rIsa=new java.net.InetSocketAddress(hostAddy, hostPort);

        class _connect implements Runnable {
            public void run() {
                newSocket=new java.net.Socket();
                try {
                    newSocket.connect(rIsa, mainFrameRef.connectionTimeout);
                } catch ( java.io.IOException ioe ) {
                    if ( sbTabInstance!=null ) sbTabInstance.addTextToStatListing(sessionID,"( Unable to establish ftp connection; " + ioe.getMessage() + " )",1,2,false);
                    //if ( sbTabInstance!=null && evince ) sbTabInstance.addTextToStatListing(sessionID,"( Unable to connect socket. " + ioe.getMessage() + " )",1,2);
                    
                    newSocket=null;
                }
            }
        }
        
        _connect _connectInstance=new _connect();
        _connectThread=new Thread(_connectInstance);
        
        _connectThread.start();
        try {
            _connectThread.join();
        } catch ( java.lang.InterruptedException ie ) {
            try {
                newSocket.close();
            } catch ( java.io.IOException ioe ) {}
            newSocket=null;
        }
    }

}