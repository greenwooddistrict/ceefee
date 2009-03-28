
import java.math.BigInteger;


class _general_ssh2_connect {//implements Runnable {    
    
    private ceefee.main mainFrameRef;
    
    private ceefee.display.sb sbTabInstance;
    
    byte[] session_kex_algorithms={};
    byte[] session_server_host_key_algorithms={};
    byte[] session_encryption_algorithms_client_to_server={};
    byte[] session_encryption_algorithms_server_to_client={};
    byte[] session_mac_algorithms_client_to_server={};
    byte[] session_mac_algorithms_server_to_client={};
    byte[] session_compression_algorithms_client_to_server={};
    byte[] session_compression_algorithms_server_to_client={};
    byte[] session_languages_client_to_server={};
    byte[] session_languages_server_to_client={};
    

    
    _general_ssh2_connect(final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
    }
    
    int connect( final int sessionID ) {//public void run() {
        int retVal;
        
        
        
        if ( mainFrameRef.ssh2ClassInstance.exchangeProtocolVersion(_sbTabInstance,sessionID)==-1 )
            return -1;

        // Receive Server KEXINIT / Send Client KEXINIT
        retVal=mainFrameRef.ssh2ClassInstance.parseSsh2BinaryPacket(_sbTabInstance,sessionID,1);
        if ( retVal==-1 )
            return -1;
        
        retVal=mainFrameRef.ssh2ClassInstance.KEXDH(_sbTabInstance,sessionID);
        if ( retVal==-1 )
            return -1;
        
        retVal=mainFrameRef.ssh2ClassInstance.NEWKEYS(_sbTabInstance,sessionID);
        if ( retVal==-1 )
            return -1;


        //done.
        //retVal=mainFrameRef.ssh2ClassInstance.disconnect(sbTabInstance,mainFrameRef.ssh2ClassInstance.SSH_DISCONNECT_BY_APPLICATION,new byte[] {'d','o','n','e'});
        
        sbTabInstance.enableDisable(2,0);
        return retVal;
    }
}