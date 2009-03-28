package ceefee.sockets;


public class general {
    
    private ceefee.main mainFrameRef;
    
    
    /** Creates a new instance of general_socket */
    public general(final ceefee.main _mainFrameRef) {
        mainFrameRef=_mainFrameRef;
    }
    
    public Object[] connectSocket( final ceefee.display.sb sbTabInstance, final int sessionID, final String hostAddy, final String hostPort, final boolean suppressErrors ) {

        ceefee.sockets.connect socketConnectInstance;
        
        if ( sbTabInstance==null ) {
            Thread connectThread=null;
            socketConnectInstance=new ceefee.sockets.connect(mainFrameRef,sbTabInstance,sessionID,connectThread,hostAddy,hostPort,suppressErrors);
        } else {
            socketConnectInstance=new ceefee.sockets.connect(mainFrameRef,sbTabInstance,sessionID,sbTabInstance.socketConnectThread,hostAddy,hostPort,suppressErrors);
        }
        
        java.lang.Thread socketConnectInstanceThread=new Thread(socketConnectInstance);
        socketConnectInstanceThread.start();
        try {
            socketConnectInstanceThread.join();
        } catch ( java.lang.InterruptedException ie) {
            return new Object[] {"-1"};
        }

        return new Object[] {socketConnectInstance.newSocket};
    }

}
    