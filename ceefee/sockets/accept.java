
package ceefee.sockets;


public class accept {
    
    private ceefee.main mainFrameRef;
    
    
    private java.net.ServerSocket socketInstance;
    private java.net.Socket newSocketInstance;
    
    acceptThread acceptThreadInstance;
        
    
    /** Creates a new instance of class_socket_accept */
    public accept(final ceefee.main _mainFrameRef, final java.net.ServerSocket _socketInstance) {       
        mainFrameRef=_mainFrameRef;
        socketInstance=_socketInstance;
        
        acceptThreadInstance=new acceptThread();
        acceptThreadInstance.start();
    }
   
    class acceptThread extends Thread {
        public void run() {
            try {
                newSocketInstance=socketInstance.accept();
                socketInstance.close();
            } catch ( java.io.IOException ioe ) {
                newSocketInstance=null;
            }
            return;
        }
    }
    
    public Object[] accept() {
        try {
            acceptThreadInstance.join(mainFrameRef.connectionTimeout);
        } catch ( java.lang.InterruptedException ie ) {
            return new Object[] {"-1"};
        }
        return new Object[] {newSocketInstance};
    }
    
}