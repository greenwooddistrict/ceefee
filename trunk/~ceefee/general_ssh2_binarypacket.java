/*     uint32    packet_length
       byte      padding_length
       byte[n1]  payload; n1 = packet_length - padding_length - 1
       byte[n2]  random padding; n2 = padding_length
       byte[m]   mac (message authentication code); m = mac_length
*/
public class general_ssh2_binarypacket {
  
    private final static int MIN_PADDING_LENGTH            =8; // must be a multiple of 8 {4,8,16,24,32,et cetera...}
    private final static int MAX_PADDING_LENGTH            =255;
    
    private ceefee.main mainFrameRef;
    
    private byte[] binaryPacket;
    
    private byte[] payLoad;
    private int cipherBlockSize;
    private byte[] mac;
    
    
    public general_ssh2_binarypacket(final ceefee.main _mainFrameRef, final byte[] _payLoad, int _cipherBlockSize, byte[] _mac) {
        mainFrameRef=_mainFrameRef;
        payLoad=_payLoad;
        cipherBlockSize=_cipherBlockSize;
        mac=_mac;
    }
    
    byte[] getBinaryPacket() {
        
        byte[] padding;
        int paddingLength=0;
        
        int packetLength;
        byte[] packetLengthB;
        
        byte[] mac=new byte[] {};
        
        int byteLooper=0;
        
        int currentByteLocation=0;
        
        
        
        if ( cipherBlockSize==0 ) {
            // Pre-KeyExchange Packet
            paddingLength=MIN_PADDING_LENGTH;
        } else {
            System.out.println("not here yet");
        }

        // UINT32 - Packet Length
        packetLength=payLoad.length+paddingLength+1;
        packetLengthB=mainFrameRef.ssh2ClassInstance.getObjectLengthForSsh2BinaryPacket(packetLength,mainFrameRef.ssh2ClassInstance.UINT32);
        
        // Random Padding
        padding=new byte[paddingLength];
        mainFrameRef.rng.nextBytes(padding);
        
        // MAC
        //mac=("").getBytes();

        
        binaryPacket=new byte[packetLength+packetLengthB.length+mac.length];

        for ( byteLooper=-1; ++byteLooper<packetLengthB.length; ) {
            binaryPacket[currentByteLocation++]=packetLengthB[byteLooper];
        }
        binaryPacket[currentByteLocation++]=(byte)paddingLength;
        for ( byteLooper=-1; ++byteLooper<payLoad.length; ) {
            binaryPacket[currentByteLocation++]=payLoad[byteLooper];
        }
        for ( byteLooper=-1; ++byteLooper<padding.length; ) {
            binaryPacket[currentByteLocation++]=padding[byteLooper];
        }
        for ( byteLooper=-1; ++byteLooper<mac.length; ) {
            binaryPacket[currentByteLocation++]=mac[byteLooper];
        }


        return binaryPacket;
    }
    
}