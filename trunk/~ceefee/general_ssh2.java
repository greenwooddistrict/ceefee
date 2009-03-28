
import java.io.IOException;

import java.math.BigInteger;

import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;

import java.security.PublicKey;
import java.security.Signature;

import java.security.spec.DSAPublicKeySpec;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.*;
import java.io.*;


class _general_ssh2 {
    
    private ceefee.main mainFrameRef;

    // General
    final String sshMajorVersion                                                                ="2";
    final String sshMinorVersion                                                                ="0";
    
    private static final int COOKIE_LENGTH                                                      =16;

    private final static String[] clientsupported_kex_algorithms                                ={"diffie-hellman-group1-sha1"};
    private final static String[] clientsupported_server_host_key_algorithms                    ={"ssh-dss","ssh-rsa"};
    private final static String[] clientsupported_encryption_algorithms_client_to_server        ={"3des-cbc"};
    private final static String[] clientsupported_encryption_algorithms_server_to_client        ={"3des-cbc"};
    private final static String[] clientsupported_mac_algorithms_client_to_server               ={"hmac-sha1"};
    private final static String[] clientsupported_mac_algorithms_server_to_client               ={"hmac-sha1"};
    private final static String[] clientsupported_compression_algorithms_client_to_server       ={"zlib","none"};
    private final static String[] clientsupported_compression_algorithms_server_to_client       ={"zlib","none"};
    private final static String[] clientsupported_languages_client_to_server                    ={""};
    private final static String[] clientsupported_languages_server_to_client                    ={""};
            
    static final int UINT32                                                                     =4;
    
    // Packet
    private static final int CR                                                                 =13;
    private static final int NL                                                                 =10;

    // GENERAL
    private static final int SSH_MSG_DISCONNECT                                                 =1;
    private static final int SSH_MSG_IGNORE                                                     =2;
    private static final int SSH_MSG_UNIMPLEMENTED                                              =3;
    private static final int SSH_MSG_DEBUG                                                      =4;
    private static final int SSH_MSG_SERVICE_REQUEST                                            =5;
    private static final int SSH_MSG_SERVICE_ACCEPT                                             =6;
    private static final int SSH_MSG_KEXINIT                                                    =20;
    private static final int SSH_MSG_NEWKEYS                                                    =21;
    private static final int SSH_MSG_KEXDH_INIT                                                 =30;
    private static final int SSH_MSG_KEXDH_REPLY                                                =31;
    private static final int SSH_MSG_USERAUTH_REQUEST                                           =50;
    private static final int SSH_MSG_USERAUTH_FAILURE                                           =51;
    private static final int SSH_MSG_USERAUTH_SUCCESS                                           =52;
    private static final int SSH_MSG_USERAUTH_BANNER                                            =53;
    private static final int SSH_MSG_USERAUTH_PK_OK                                             =60;
    private static final int SSH_MSG_GLOBAL_REQUEST                                             =80;
    private static final int SSH_MSG_REQUEST_SUCCESS                                            =81;
    private static final int SSH_MSG_REQUEST_FAILURE                                            =82;
    private static final int SSH_MSG_CHANNEL_OPEN                                               =90;
    private static final int SSH_MSG_CHANNEL_OPEN_CONFIRMATION                                  =91;
    private static final int SSH_MSG_CHANNEL_OPEN_FAILURE                                       =92;
    private static final int SSH_MSG_CHANNEL_WINDOW_ADJUST                                      =93;
    private static final int SSH_MSG_CHANNEL_DATA                                               =94;
    private static final int SSH_MSG_CHANNEL_EXTENDED_DATA                                      =95;
    private static final int SSH_MSG_CHANNEL_EOF                                                =96;
    private static final int SSH_MSG_CHANNEL_CLOSE                                              =97;
    private static final int SSH_MSG_CHANNEL_REQUEST                                            =98;
    private static final int SSH_MSG_CHANNEL_SUCCESS                                            =99;
    private static final int SSH_MSG_CHANNEL_FAILURE                                            =100;

    // DISCONNECT
    private static final int SSH_DISCONNECT_HOST_NOT_ALLOWED_TO_CONNECT                         =1;
    private static final int SSH_DISCONNECT_PROTOCOL_ERROR                                      =2;
    private static final int SSH_DISCONNECT_KEY_EXCHANGE_FAILED                                 =3;
    private static final int SSH_DISCONNECT_RESERVED                                            =4;
    private static final int SSH_DISCONNECT_MAC_ERROR                                           =5;
    private static final int SSH_DISCONNECT_COMPRESSION_ERROR                                   =6;
    private static final int SSH_DISCONNECT_SERVICE_NOT_AVAILABLE                               =7;
    private static final int SSH_DISCONNECT_PROTOCOL_VERSION_NOT_SUPPORTED                      =8;
    private static final int SSH_DISCONNECT_HOST_KEY_NOT_VERIFIABLE                             =9;
    private static final int SSH_DISCONNECT_CONNECTION_LOST                                     =10;
    static final int SSH_DISCONNECT_BY_APPLICATION                                              =11;
    private static final int SSH_DISCONNECT_TOO_MANY_CONNECTIONS                                =12;
    private static final int SSH_DISCONNECT_AUTH_CANCELLED_BY_USER                              =13;
    private static final int SSH_DISCONNECT_NO_MORE_AUTH_METHODS_AVAILABLE                      =14;
    private static final int SSH_DISCONNECT_ILLEGAL_USER_NAME                                   =15;

     
    
    _general_ssh2(final ceefee.main _mainFrameRef) {
        mainFrameRef=_mainFrameRef;
    }

    String getPacketType( final byte packetTypeByte ) {
        if ( packetTypeByte==SSH_MSG_DISCONNECT )
            return "DISCONNECT";
        else if ( packetTypeByte==SSH_MSG_IGNORE )
            return "IGNORE";
        else if ( packetTypeByte==SSH_MSG_UNIMPLEMENTED )
            return "UNIMPLEMENTED";
        else if ( packetTypeByte==SSH_MSG_DEBUG )
            return "DEBUG";
        else if ( packetTypeByte==SSH_MSG_SERVICE_REQUEST )
            return "SERVICE_REQUEST";
        else if ( packetTypeByte==SSH_MSG_SERVICE_ACCEPT )
            return "SERVICE_ACCEPT";
        else if ( packetTypeByte==SSH_MSG_KEXINIT )
            return "KEXINIT";
        else if ( packetTypeByte==SSH_MSG_NEWKEYS )
            return "NEWKEYS";
        else if ( packetTypeByte==SSH_MSG_KEXDH_INIT )
            return "KEXDH_INIT";
        else if ( packetTypeByte==SSH_MSG_KEXDH_REPLY )
            return "KEXDH_REPLY";
        else if ( packetTypeByte==SSH_MSG_USERAUTH_REQUEST )
            return "USERAUTH_REQUEST";
        else if ( packetTypeByte==SSH_MSG_USERAUTH_FAILURE )
            return "USERAUTH_FAILURE";
        else if ( packetTypeByte==SSH_MSG_USERAUTH_SUCCESS )
            return "USERAUTH_SUCCESS";
        else if ( packetTypeByte==SSH_MSG_USERAUTH_BANNER )
            return "USERAUTH_BANNER";
        else if ( packetTypeByte==SSH_MSG_USERAUTH_PK_OK )
            return "USERAUTH_PK_OK";
        else if ( packetTypeByte==SSH_MSG_GLOBAL_REQUEST )
            return "GLOBAL_REQUEST";
        else if ( packetTypeByte==SSH_MSG_REQUEST_SUCCESS )
            return "REQUEST_SUCCESS";
        else if ( packetTypeByte==SSH_MSG_REQUEST_FAILURE )
            return "REQUEST_FAILURE";
        else if ( packetTypeByte==SSH_MSG_CHANNEL_OPEN )
            return "CHANNEL_OPEN";
        else if ( packetTypeByte==SSH_MSG_CHANNEL_OPEN_CONFIRMATION )
            return "CHANNEL_OPEN_CONFIRMATION";
        else if ( packetTypeByte==SSH_MSG_CHANNEL_OPEN_FAILURE )
            return "CHANNEL_OPEN_FAILURE";
        else if ( packetTypeByte==SSH_MSG_CHANNEL_WINDOW_ADJUST )
            return "CHANNEL_WINDOW_ADJUST";
        else if ( packetTypeByte==SSH_MSG_CHANNEL_DATA )
            return "CHANNEL_DATA";
        else if ( packetTypeByte==SSH_MSG_CHANNEL_EXTENDED_DATA )
            return "CHANNEL_EXTENDED_DATA";
        else if ( packetTypeByte==SSH_MSG_CHANNEL_EOF )
            return "CHANNEL_EOF";
        else if ( packetTypeByte==SSH_MSG_CHANNEL_CLOSE )
            return "CHANNEL_CLOSE";
        else if ( packetTypeByte==SSH_MSG_CHANNEL_REQUEST )
            return "CHANNEL_REQUEST";
        else if ( packetTypeByte==SSH_MSG_CHANNEL_SUCCESS )
            return "CHANNEL_SUCCESS";
        else if ( packetTypeByte==SSH_MSG_CHANNEL_FAILURE )
            return "CHANNEL_FAILURE";
        else
            return "Encrypted SSH2";
    }
        
    byte[] getObjectLengthForSsh2BinaryPacket( int objectValue, final int objectSize ) {
        int buffer;
        byte[] objectLength=new byte[objectSize];
               

        for ( int byteLooper=objectSize; --byteLooper>-1; ) {
            buffer=((objectValue>>(objectSize-1-byteLooper))&(0xFF));
            objectLength[byteLooper]=(byte)buffer;
            objectValue-=buffer;
        }
        return objectLength;
    }
    
    int getObjectLengthFromSsh2BinaryPacket( final byte[] packetData, int offset, final int objectSize ) {
        int lengthBuffer=0;
        
        // String Length
        // byte[s]: x thru (x+4)
        for ( int byteLooper=objectSize; --byteLooper>-1; ) {
            if ( byteLooper>0 )
                lengthBuffer+=((0xff)&packetData[offset++])*(byteLooper*256);
            else
                lengthBuffer+=((0xff)&packetData[offset++]);
        }
        return lengthBuffer;
    }
    
    int exchangeProtocolVersion( final ceefee.display.sb sbTabInstance, final int sessionID ) {
        byte[] retVal;
        String retValString;
        byte[] retValBuffer;
        
        int bytesRead;
        
        int whimsicalByteLocation;

        int endOfLineCharacter;
        
                
        retValBuffer=new byte[mainFrameRef.MAX_WINDOWSIZE];
        try {
            bytesRead=sbTabInstance.sshInputStream.read(retValBuffer);
            if ( bytesRead==-1 ) {
                sbTabInstance.addTextToStatListing(sessionID,"( " + "Remote host closed connection." + " )",1,2);
                try {
                    sbTabInstance.controlSocket.close();
                } catch ( java.io.IOException ioe ) {}
                return -1;
            }
            retVal=new byte[bytesRead];
            for ( int retValLooper=-1; ++retValLooper<bytesRead; ) {
                retVal[retValLooper]=retValBuffer[retValLooper];
            }
            retValString=new String(retVal);
            sbTabInstance.addTextToStatListing(sessionID,retValString,1,1);
            whimsicalByteLocation=retValString.indexOf("SSH-");
            if ( whimsicalByteLocation==-1 ) {
                sbTabInstance.addTextToStatListing(sessionID,"( " + "Invalid SSH server." + " )",1,2);
                try {
                    sbTabInstance.controlSocket.close();
                } catch ( java.io.IOException ioe ) {}
                return -1;            
            } else if ( retValString.substring(whimsicalByteLocation,8).equals("SSH-1.99")==false && retValString.substring(whimsicalByteLocation,7).equals("SSH-2.0")==false ) { // Server only supports SSH1
                sbTabInstance.addTextToStatListing(sessionID,"( " + "Incompatible SSH version." + " )",1,2);
                try {
                    sbTabInstance.browseControlSocket.close();
                } catch ( java.io.IOException ioe ) {}
                return -1;
            }

            if ( retVal[retVal.length-1]==NL ) {
                if ( retVal[retVal.length-2]==CR ) {
                    endOfLineCharacter=CR;
                    sbTabInstance.SSH2_SERVER_VERSION_STRING=retValString.substring(0, retVal.length-2).getBytes();
                } else {
                    endOfLineCharacter=NL;
                    sbTabInstance.SSH2_SERVER_VERSION_STRING=retValString.substring(0, retVal.length-1).getBytes();
                }
            } else {
                sbTabInstance.addTextToStatListing(sessionID,"( " + "Corrupt SSH_MSG_KEXINIT packet. <Error 0x01>" + " )",1,2);
                try {
                    sbTabInstance.browseControlSocket.close();
                } catch ( java.io.IOException ioe ) {}
                return -1;
            }

            sbTabInstance.sshOutputStream.write((new String(sbTabInstance.SSH2_CLIENT_VERSION_STRING)+"\r\n").getBytes());
            sbTabInstance.sshOutputStream.flush();
            sbTabInstance.addTextToStatListing(sessionID,new String(sbTabInstance.SSH2_CLIENT_VERSION_STRING),0,2);

            return 0;

        } catch ( java.io.IOException ioe ) {
            sbTabInstance.addTextToStatListing(sessionID,"( " + ioe.getMessage() + " )",1,2);
            try {
                sbTabInstance.browseControlSocket.close();
            } catch ( java.io.IOException ioe2 ) {}
            return -1;
        }
    }
    
    byte[] getObjectFromSsh2BinaryPacket( final byte[] packetData, final int objectOffset, final int objectSize ) {
        byte[] objectBuffer=new byte[objectSize];
        
        for ( int byteLooper=-1; ++byteLooper<objectSize; ) {
            objectBuffer[byteLooper]=packetData[objectOffset+byteLooper];
        }
        return objectBuffer;
    }
    
    int KEXINIT( final ceefee.display.sb sbTabInstance, final int sessionID, byte[] payLoad ) {
        sbTabInstance.SSH2_SERVER_KEXINIT_PAYLOAD=payLoad;
        /* General
        */
        String buffer;
        java.util.StringTokenizer strtok;
        
        int looper;

        byte[] stringLength;
        
        int currentByteLocation=0;
                        
        general_ssh2_binarypacket ssh2BinaryPacketClassInstance;
        byte[] binaryPacket;
        
                        
        
        /* SSH_MSG_KEXINIT Packet Variables
        */        
        byte[] serversupported_kex_algorithms={};
        byte[] serversupported_server_host_key_algorithms={};
        byte[] serversupported_encryption_algorithms_client_to_server={};
        byte[] serversupported_encryption_algorithms_server_to_client={};
        byte[] serversupported_mac_algorithms_client_to_server={};
        byte[] serversupported_mac_algorithms_server_to_client={};
        byte[] serversupported_compression_algorithms_client_to_server={};
        byte[] serversupported_compression_algorithms_server_to_client={};
        byte[] serversupported_languages_client_to_server={};
        byte[] serversupported_languages_server_to_client={};
        byte first_kex_packet_follows;
        //uint32    0 (reserved for future extension)
        /* Client
        */
        byte[] cookie=new byte[COOKIE_LENGTH];
        
        
        //
        // Receive server's KEXINIT packet
        // 
        if ( payLoad[0]!=mainFrameRef.ssh2ClassInstance.SSH_MSG_KEXINIT ) {
            sbTabInstance.addTextToStatListing(sessionID,"( " + "Corrupt SSH_MSG_KEXINIT packet. <Error 0x03>" + " )",1,2);
            try {
                sbTabInstance.browseControlSocket.close();
            } catch ( java.io.IOException ioe ) {}
            return -1;
        }

        currentByteLocation=1+COOKIE_LENGTH; //skip KEXINIT byte and cookie bytes

        //1
        serversupported_kex_algorithms=mainFrameRef.ssh2ClassInstance.getObjectFromSsh2BinaryPacket(payLoad, currentByteLocation+mainFrameRef.ssh2ClassInstance.UINT32,mainFrameRef.ssh2ClassInstance.getObjectLengthFromSsh2BinaryPacket(payLoad,currentByteLocation,mainFrameRef.ssh2ClassInstance.UINT32));
        //sbTabInstance.addTextToStatListing(sessionID,sbTabInstance,"kex_algorithms:\t\t" + new String(kex_algorithms),1,1);
        currentByteLocation+=mainFrameRef.ssh2ClassInstance.UINT32+serversupported_kex_algorithms.length;

        //2
        serversupported_server_host_key_algorithms=mainFrameRef.ssh2ClassInstance.getObjectFromSsh2BinaryPacket(payLoad, currentByteLocation+mainFrameRef.ssh2ClassInstance.UINT32,mainFrameRef.ssh2ClassInstance.getObjectLengthFromSsh2BinaryPacket(payLoad,currentByteLocation,mainFrameRef.ssh2ClassInstance.UINT32));
        //sbTabInstance.addTextToStatListing(sessionID,sbTabInstance,"server_host_key_algorithms:\t\t" + new String(server_host_key_algorithms),1,1);
        currentByteLocation+=mainFrameRef.ssh2ClassInstance.UINT32+serversupported_server_host_key_algorithms.length;

        //3
        serversupported_encryption_algorithms_client_to_server=mainFrameRef.ssh2ClassInstance.getObjectFromSsh2BinaryPacket(payLoad, currentByteLocation+mainFrameRef.ssh2ClassInstance.UINT32,mainFrameRef.ssh2ClassInstance.getObjectLengthFromSsh2BinaryPacket(payLoad,currentByteLocation,mainFrameRef.ssh2ClassInstance.UINT32));
        //sbTabInstance.addTextToStatListing(sessionID,sbTabInstance,"encryption_algorithms_client_to_server:\t\t" + new String(serversupported_encryption_algorithms_client_to_server),1,1);
        currentByteLocation+=mainFrameRef.ssh2ClassInstance.UINT32+serversupported_encryption_algorithms_client_to_server.length;
        //4
        serversupported_encryption_algorithms_server_to_client=mainFrameRef.ssh2ClassInstance.getObjectFromSsh2BinaryPacket(payLoad, currentByteLocation+mainFrameRef.ssh2ClassInstance.UINT32,mainFrameRef.ssh2ClassInstance.getObjectLengthFromSsh2BinaryPacket(payLoad,currentByteLocation,mainFrameRef.ssh2ClassInstance.UINT32));
        //sbTabInstance.addTextToStatListing(sessionID,sbTabInstance,"encryption_algorithms_server_to_client:\t\t" + new String(serversupported_encryption_algorithms_server_to_client),1,1);
        currentByteLocation+=mainFrameRef.ssh2ClassInstance.UINT32+serversupported_encryption_algorithms_server_to_client.length;

        //5
        serversupported_mac_algorithms_client_to_server=mainFrameRef.ssh2ClassInstance.getObjectFromSsh2BinaryPacket(payLoad, currentByteLocation+mainFrameRef.ssh2ClassInstance.UINT32,mainFrameRef.ssh2ClassInstance.getObjectLengthFromSsh2BinaryPacket(payLoad,currentByteLocation,mainFrameRef.ssh2ClassInstance.UINT32));
        //sbTabInstance.addTextToStatListing(sessionID,sbTabInstance,"mac_algorithms_client_to_server:\t\t" + new String(mac_algorithms_client_to_server),1,1);
        currentByteLocation+=mainFrameRef.ssh2ClassInstance.UINT32+serversupported_mac_algorithms_client_to_server.length;
        //6
        serversupported_mac_algorithms_server_to_client=mainFrameRef.ssh2ClassInstance.getObjectFromSsh2BinaryPacket(payLoad, currentByteLocation+mainFrameRef.ssh2ClassInstance.UINT32,mainFrameRef.ssh2ClassInstance.getObjectLengthFromSsh2BinaryPacket(payLoad,currentByteLocation,mainFrameRef.ssh2ClassInstance.UINT32));
        //sbTabInstance.addTextToStatListing(sessionID,sbTabInstance,"mac_algorithms_server_to_client:\t\t" + new String(mac_algorithms_server_to_client),1,1);
        currentByteLocation+=mainFrameRef.ssh2ClassInstance.UINT32+serversupported_mac_algorithms_server_to_client.length;

        //7
        serversupported_compression_algorithms_client_to_server=mainFrameRef.ssh2ClassInstance.getObjectFromSsh2BinaryPacket(payLoad, currentByteLocation+mainFrameRef.ssh2ClassInstance.UINT32,mainFrameRef.ssh2ClassInstance.getObjectLengthFromSsh2BinaryPacket(payLoad,currentByteLocation,mainFrameRef.ssh2ClassInstance.UINT32));
        //sbTabInstance.addTextToStatListing(sessionID,sbTabInstance,"compression_algorithms_client_to_server:\t\t" + new String(compression_algorithms_client_to_server),1,1);
        currentByteLocation+=mainFrameRef.ssh2ClassInstance.UINT32+serversupported_compression_algorithms_client_to_server.length;
        //8
        serversupported_compression_algorithms_server_to_client=mainFrameRef.ssh2ClassInstance.getObjectFromSsh2BinaryPacket(payLoad, currentByteLocation+mainFrameRef.ssh2ClassInstance.UINT32,mainFrameRef.ssh2ClassInstance.getObjectLengthFromSsh2BinaryPacket(payLoad,currentByteLocation,mainFrameRef.ssh2ClassInstance.UINT32));
        //sbTabInstance.addTextToStatListing(sessionID,sbTabInstance,"compression_algorithms_server_to_client:\t\t" + new String(compression_algorithms_server_to_client),1,1);
        currentByteLocation+=mainFrameRef.ssh2ClassInstance.UINT32+serversupported_compression_algorithms_server_to_client.length;

        //9
        serversupported_languages_client_to_server=mainFrameRef.ssh2ClassInstance.getObjectFromSsh2BinaryPacket(payLoad, currentByteLocation+mainFrameRef.ssh2ClassInstance.UINT32,mainFrameRef.ssh2ClassInstance.getObjectLengthFromSsh2BinaryPacket(payLoad,currentByteLocation,mainFrameRef.ssh2ClassInstance.UINT32));
        //sbTabInstance.addTextToStatListing(sessionID,sbTabInstance,"languages_client_to_server:\t\t" + new String(languages_client_to_server),1,1);
        currentByteLocation+=mainFrameRef.ssh2ClassInstance.UINT32+serversupported_languages_client_to_server.length;
        //10
        serversupported_languages_server_to_client=mainFrameRef.ssh2ClassInstance.getObjectFromSsh2BinaryPacket(payLoad, currentByteLocation+mainFrameRef.ssh2ClassInstance.UINT32,mainFrameRef.ssh2ClassInstance.getObjectLengthFromSsh2BinaryPacket(payLoad,currentByteLocation,mainFrameRef.ssh2ClassInstance.UINT32));
        //sbTabInstance.addTextToStatListing(sessionID,sbTabInstance,"languages_server_to_client:\t\t" + new String(languages_server_to_client),1,1);
        currentByteLocation+=mainFrameRef.ssh2ClassInstance.UINT32+serversupported_languages_server_to_client.length;

        //11
        if ( payLoad[currentByteLocation++]==0 ) {
            first_kex_packet_follows=0;
        } else {
            first_kex_packet_follows=1;
        }
        
        //12
        //uint32    0 (reserved for future extension)


        //1
        strtok=new java.util.StringTokenizer(new String(serversupported_kex_algorithms),",");
        for ( looper=-1; ++looper<strtok.countTokens(); ) {
            buffer=strtok.nextToken();
            if ( mainFrameRef.utilitiesClassInstance.isStringInArray(buffer,clientsupported_kex_algorithms) ) {
                sbTabInstance.ssh2ConnectClassInstance.session_kex_algorithms=buffer.getBytes();
                break;
            } else if ( strtok.countTokens()==0 ) {
                sbTabInstance.addTextToStatListing(sessionID,"No supported 'kex_algorithms' available.",0,2);
                return -1;
            }
        }

        //2
        strtok=new java.util.StringTokenizer(new String(serversupported_server_host_key_algorithms),",");
        for ( looper=-1; ++looper<strtok.countTokens(); ) {
            buffer=strtok.nextToken();
            if ( mainFrameRef.utilitiesClassInstance.isStringInArray(buffer,clientsupported_server_host_key_algorithms) ) {
                sbTabInstance.ssh2ConnectClassInstance.session_server_host_key_algorithms=buffer.getBytes();
                break;
            } else if ( strtok.countTokens()==0 ) {
                sbTabInstance.addTextToStatListing(sessionID,"No supported 'server_host_key_algorithms' methods available.",0,2);
                return -1;
            }
        }
        
        //3
        strtok=new java.util.StringTokenizer(new String(serversupported_encryption_algorithms_client_to_server),",");
        for ( looper=-1; ++looper<strtok.countTokens(); ) {
            buffer=strtok.nextToken();
            if ( mainFrameRef.utilitiesClassInstance.isStringInArray(buffer,clientsupported_encryption_algorithms_client_to_server) ) {
                sbTabInstance.ssh2ConnectClassInstance.session_encryption_algorithms_client_to_server=buffer.getBytes();
                break;
            } else if ( strtok.countTokens()==0 ) {
                sbTabInstance.addTextToStatListing(sessionID,"No supported 'encryption_algorithms_client_to_server' methods available.",0,2);
                return -1;
            }
        }
        //4
        strtok=new java.util.StringTokenizer(new String(serversupported_encryption_algorithms_server_to_client),",");
        for ( looper=-1; ++looper<strtok.countTokens(); ) {
            buffer=strtok.nextToken();
            if ( mainFrameRef.utilitiesClassInstance.isStringInArray(buffer,clientsupported_encryption_algorithms_server_to_client) ) {
                sbTabInstance.ssh2ConnectClassInstance.session_encryption_algorithms_server_to_client=buffer.getBytes();
                break;
            } else if ( strtok.countTokens()==0 ) {
                sbTabInstance.addTextToStatListing(sessionID,"No supported 'encryption_algorithms_server_to_client' methods available.",0,2);
                return -1;
            }
        }

        //5
        strtok=new java.util.StringTokenizer(new String(serversupported_mac_algorithms_client_to_server),",");
        for ( looper=-1; ++looper<strtok.countTokens(); ) {
            buffer=strtok.nextToken();
            if ( mainFrameRef.utilitiesClassInstance.isStringInArray(buffer,clientsupported_mac_algorithms_client_to_server) ) {
                sbTabInstance.ssh2ConnectClassInstance.session_mac_algorithms_client_to_server=buffer.getBytes();
                break;
            } else if ( strtok.countTokens()==0 ) {
                sbTabInstance.addTextToStatListing(sessionID,"No supported 'mac_algorithms_client_to_server' methods available.",0,2);
                return -1;
            }
        }
        //6
        strtok=new java.util.StringTokenizer(new String(serversupported_mac_algorithms_server_to_client),",");
        for ( looper=-1; ++looper<strtok.countTokens(); ) {
            buffer=strtok.nextToken();
            if ( mainFrameRef.utilitiesClassInstance.isStringInArray(buffer,clientsupported_mac_algorithms_server_to_client) ) {
                sbTabInstance.ssh2ConnectClassInstance.session_mac_algorithms_server_to_client=buffer.getBytes();
                break;
            } else if ( strtok.countTokens()==0 ) {
                sbTabInstance.addTextToStatListing(sessionID,"No supported 'mac_algorithms_server_to_client' methods available.",0,2);
                return -1;
            }
        }

        //7
        strtok=new java.util.StringTokenizer(new String(serversupported_compression_algorithms_client_to_server),",");
        for ( looper=-1; ++looper<strtok.countTokens(); ) {
            buffer=strtok.nextToken();
            if ( mainFrameRef.utilitiesClassInstance.isStringInArray(buffer,clientsupported_compression_algorithms_client_to_server) ) {
                sbTabInstance.ssh2ConnectClassInstance.session_compression_algorithms_client_to_server=buffer.getBytes();
                break;
            } else if ( strtok.countTokens()==0 ) {
                sbTabInstance.addTextToStatListing(sessionID,"No supported 'compression_algorithms_client_to_server' methods available.",0,2);
                return -1;
            }
        }
        //8
        strtok=new java.util.StringTokenizer(new String(serversupported_compression_algorithms_server_to_client),",");
        for ( looper=-1; ++looper<strtok.countTokens(); ) {
            buffer=strtok.nextToken();
            if ( mainFrameRef.utilitiesClassInstance.isStringInArray(buffer,clientsupported_compression_algorithms_server_to_client) ) {
                sbTabInstance.ssh2ConnectClassInstance.session_compression_algorithms_server_to_client=buffer.getBytes();
                break;
            } else if ( strtok.countTokens()==0 ) {
                sbTabInstance.addTextToStatListing(sessionID,"No supported 'compression_algorithms_server_to_client' methods available.",0,2);
                return -1;
            }
        }


        
        /*
         System.out.println("session0: " + new String(sbTabInstance.ssh2ConnectClassInstance.session_kex_algorithms));
         System.out.println("session1: " + new String(sbTabInstance.ssh2ConnectClassInstance.session_server_host_key_algorithms));
         System.out.println("session2: " + new String(sbTabInstance.ssh2ConnectClassInstance.session_encryption_algorithms_client_to_server));
         System.out.println("session3: " + new String(sbTabInstance.ssh2ConnectClassInstance.session_encryption_algorithms_server_to_client));
         System.out.println("session4: " + new String(sbTabInstance.ssh2ConnectClassInstance.session_mac_algorithms_client_to_server));
         System.out.println("session5: " + new String(sbTabInstance.ssh2ConnectClassInstance.session_mac_algorithms_server_to_client));
         System.out.println("session6: " + new String(sbTabInstance.ssh2ConnectClassInstance.session_compression_algorithms_client_to_server));
         System.out.println("session7: " + new String(sbTabInstance.ssh2ConnectClassInstance.session_compression_algorithms_server_to_client));
        */
        
        

        /*
           Send client's KEXINIT packet
        */ 
        sbTabInstance.SSH2_CLIENT_KEXINIT_PAYLOAD=new byte[1+COOKIE_LENGTH+UINT32+sbTabInstance.ssh2ConnectClassInstance.session_kex_algorithms.length+
            UINT32+sbTabInstance.ssh2ConnectClassInstance.session_server_host_key_algorithms.length+
            UINT32+sbTabInstance.ssh2ConnectClassInstance.session_encryption_algorithms_client_to_server.length+
            UINT32+sbTabInstance.ssh2ConnectClassInstance.session_encryption_algorithms_server_to_client.length+
            UINT32+sbTabInstance.ssh2ConnectClassInstance.session_mac_algorithms_client_to_server.length+
            UINT32+sbTabInstance.ssh2ConnectClassInstance.session_mac_algorithms_server_to_client.length+
            UINT32+sbTabInstance.ssh2ConnectClassInstance.session_compression_algorithms_client_to_server.length+
            UINT32+sbTabInstance.ssh2ConnectClassInstance.session_compression_algorithms_server_to_client.length+
            UINT32+sbTabInstance.ssh2ConnectClassInstance.session_languages_client_to_server.length+
            UINT32+sbTabInstance.ssh2ConnectClassInstance.session_languages_server_to_client.length+1+1];
        
        currentByteLocation=0;
        
        payLoad[currentByteLocation++]=SSH_MSG_KEXINIT;
        
        mainFrameRef.rng.nextBytes(cookie);
        for ( looper=-1; ++looper<16; ) {
            payLoad[currentByteLocation++]=cookie[looper];
        }
        
        stringLength=getObjectLengthForSsh2BinaryPacket(sbTabInstance.ssh2ConnectClassInstance.session_kex_algorithms.length,UINT32);
        for ( looper=-1; ++looper<stringLength.length; ) {
            payLoad[currentByteLocation++]=stringLength[looper];
        }
        for ( looper=-1; ++looper<sbTabInstance.ssh2ConnectClassInstance.session_kex_algorithms.length; ) {
            payLoad[currentByteLocation++]=sbTabInstance.ssh2ConnectClassInstance.session_kex_algorithms[looper];
        }

        stringLength=getObjectLengthForSsh2BinaryPacket(sbTabInstance.ssh2ConnectClassInstance.session_server_host_key_algorithms.length,UINT32);
        for ( looper=-1; ++looper<stringLength.length; ) {
            payLoad[currentByteLocation++]=stringLength[looper];
        }
        for ( looper=-1; ++looper<sbTabInstance.ssh2ConnectClassInstance.session_server_host_key_algorithms.length; ) {
            payLoad[currentByteLocation++]=sbTabInstance.ssh2ConnectClassInstance.session_server_host_key_algorithms[looper];
        }

        stringLength=getObjectLengthForSsh2BinaryPacket(sbTabInstance.ssh2ConnectClassInstance.session_encryption_algorithms_client_to_server.length,UINT32);
        for ( looper=-1; ++looper<stringLength.length; ) {
            payLoad[currentByteLocation++]=stringLength[looper];
        }
        for ( looper=-1; ++looper<sbTabInstance.ssh2ConnectClassInstance.session_encryption_algorithms_client_to_server.length; ) {
            payLoad[currentByteLocation++]=sbTabInstance.ssh2ConnectClassInstance.session_encryption_algorithms_client_to_server[looper];
        }
        stringLength=getObjectLengthForSsh2BinaryPacket(sbTabInstance.ssh2ConnectClassInstance.session_encryption_algorithms_server_to_client.length,UINT32);
        for ( looper=-1; ++looper<stringLength.length; ) {
            payLoad[currentByteLocation++]=stringLength[looper];
        }
        for ( looper=-1; ++looper<sbTabInstance.ssh2ConnectClassInstance.session_encryption_algorithms_server_to_client.length; ) {
            payLoad[currentByteLocation++]=sbTabInstance.ssh2ConnectClassInstance.session_encryption_algorithms_server_to_client[looper];
        }

        stringLength=getObjectLengthForSsh2BinaryPacket(sbTabInstance.ssh2ConnectClassInstance.session_mac_algorithms_client_to_server.length,UINT32);
        for ( looper=-1; ++looper<stringLength.length; ) {
            payLoad[currentByteLocation++]=stringLength[looper];
        }
        for ( looper=-1; ++looper<sbTabInstance.ssh2ConnectClassInstance.session_mac_algorithms_client_to_server.length; ) {
            payLoad[currentByteLocation++]=sbTabInstance.ssh2ConnectClassInstance.session_mac_algorithms_client_to_server[looper];
        }
        stringLength=getObjectLengthForSsh2BinaryPacket(sbTabInstance.ssh2ConnectClassInstance.session_mac_algorithms_server_to_client.length,UINT32);
        for ( looper=-1; ++looper<stringLength.length; ) {
            payLoad[currentByteLocation++]=stringLength[looper];
        }
        for ( looper=-1; ++looper<sbTabInstance.ssh2ConnectClassInstance.session_mac_algorithms_server_to_client.length; ) {
            payLoad[currentByteLocation++]=sbTabInstance.ssh2ConnectClassInstance.session_mac_algorithms_server_to_client[looper];
        }

        stringLength=getObjectLengthForSsh2BinaryPacket(sbTabInstance.ssh2ConnectClassInstance.session_compression_algorithms_client_to_server.length,UINT32);
        for ( looper=-1; ++looper<stringLength.length; ) {
            payLoad[currentByteLocation++]=stringLength[looper];
        }
        for ( looper=-1; ++looper<sbTabInstance.ssh2ConnectClassInstance.session_compression_algorithms_client_to_server.length; ) {
            payLoad[currentByteLocation++]=sbTabInstance.ssh2ConnectClassInstance.session_compression_algorithms_client_to_server[looper];
        }
        stringLength=getObjectLengthForSsh2BinaryPacket(sbTabInstance.ssh2ConnectClassInstance.session_compression_algorithms_server_to_client.length,UINT32);
        for ( looper=-1; ++looper<stringLength.length; ) {
            payLoad[currentByteLocation++]=stringLength[looper];
        }
        for ( looper=-1; ++looper<sbTabInstance.ssh2ConnectClassInstance.session_compression_algorithms_server_to_client.length; ) {
            payLoad[currentByteLocation++]=sbTabInstance.ssh2ConnectClassInstance.session_compression_algorithms_server_to_client[looper];
        }

        stringLength=getObjectLengthForSsh2BinaryPacket(sbTabInstance.ssh2ConnectClassInstance.session_languages_client_to_server.length,UINT32);
        for ( looper=-1; ++looper<stringLength.length; ) {
            payLoad[currentByteLocation++]=stringLength[looper];
        }
        for ( looper=-1; ++looper<sbTabInstance.ssh2ConnectClassInstance.session_languages_client_to_server.length; ) {
            payLoad[currentByteLocation++]=sbTabInstance.ssh2ConnectClassInstance.session_languages_client_to_server[looper];
        }
        stringLength=getObjectLengthForSsh2BinaryPacket(sbTabInstance.ssh2ConnectClassInstance.session_languages_server_to_client.length,UINT32);
        for ( looper=-1; ++looper<stringLength.length; ) {
            payLoad[currentByteLocation++]=stringLength[looper];
        }
        for ( looper=-1; ++looper<sbTabInstance.ssh2ConnectClassInstance.session_languages_server_to_client.length; ) {
            payLoad[currentByteLocation++]=sbTabInstance.ssh2ConnectClassInstance.session_languages_server_to_client[looper];
        }
        
        payLoad[currentByteLocation++]=0;
        
        payLoad[currentByteLocation++]=0;
        

        if ( sendSsh2BinaryPacket(sbTabInstance,sessionID,payLoad)==-1 ) {
            return -1;
        } else {
            return 0;
        }
            
    }

    int KEXDH ( final ceefee.display.sb sbTabInstance, final int sessionID ) {
        /* Send client's SSH_MSG_KEXDH_INIT packet
         
           byte      SSH_MSG_KEXDH_INIT
           mpint     e
        */
        general_ssh2_binarypacket ssh2BinaryPacketClassInstance;
        
        int currentByteLocation=0;
        
        int byteLooper;
        
        byte[] binaryPacket;
        
        byte[] payLoad;
        
        byte[] mpIntE;
        byte[] eByteArray;

        
        //q=(p-1)/2
        BigInteger q=sbTabInstance.p.subtract(new BigInteger("1"));
        q=q.divide(new BigInteger("2"));
                
        //1<x<q
        sbTabInstance.x=new BigInteger(String.valueOf(mainFrameRef.rng.nextLong()));
        if ( sbTabInstance.x.compareTo(new BigInteger(String.valueOf(mainFrameRef.MAX_UINT32)))!=1 )
            sbTabInstance.x=sbTabInstance.x.add(new BigInteger(String.valueOf(mainFrameRef.MAX_UINT32)));
        while ( q.compareTo(new BigInteger(String.valueOf(sbTabInstance.x)))!=1 ) {
            sbTabInstance.x=sbTabInstance.x.divide(sbTabInstance.g);
        }
                    
        sbTabInstance.e=sbTabInstance.g.modPow(sbTabInstance.x,sbTabInstance.p);
        
        eByteArray=sbTabInstance.e.toByteArray();
        mpIntE=getObjectLengthForSsh2BinaryPacket(eByteArray.length,UINT32);
        payLoad=new byte[mpIntE.length+1+eByteArray.length];
        payLoad[currentByteLocation++]=SSH_MSG_KEXDH_INIT;
        for ( byteLooper=-1; ++byteLooper<mpIntE.length; ) {
            payLoad[currentByteLocation++]=mpIntE[byteLooper];
        }
        for ( byteLooper=-1; ++byteLooper<eByteArray.length; ) {
            payLoad[currentByteLocation++]=eByteArray[byteLooper];
        }
                
        sbTabInstance.addTextToStatListing(sessionID,"",0,1);
        
        // Send Client KEXDH Packet
        if ( sendSsh2BinaryPacket(sbTabInstance,sessionID,payLoad)==-1 )
            return -1;

        // Receive Server KEXDH Packet         
        return parseSsh2BinaryPacket(sbTabInstance,sessionID,2);
    }
    
    int parseSsh2BinaryPacket( final ceefee.display.sb sbTabInstance, final int sessionID, final int numOfLines ) {
        /* General
        */
        byte[] payLoad;
        
        byte[] retVal;
        byte[] retValBuffer;
        
        int bytesRead=0;

        int byteLooper;
        
        int currentByteLocation=0;
        
        int packetLength;
        int paddingLength;
        


        retValBuffer=new byte[mainFrameRef.MAX_WINDOWSIZE];
        try {
            bytesRead=sbTabInstance.sshInputStream.read(retValBuffer);
        } catch ( java.io.IOException ioe ) {
            sbTabInstance.addTextToStatListing(sessionID,"( " + ioe.getMessage() + " )",1,2);
            try {
                sbTabInstance.browseControlSocket.close();
            } catch ( java.io.IOException ioe2 ) {}
            return -1;
        }
        if ( bytesRead==-1 ) {
            sbTabInstance.addTextToStatListing(sessionID,"( " + "No response received from server." + " )",1,2);
            try { sbTabInstance.browseControlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return -1;
        }

        retVal=new byte[bytesRead];
        for ( byteLooper=-1; ++byteLooper<bytesRead; ) {
            retVal[byteLooper]=retValBuffer[byteLooper];
        }
        sbTabInstance.addTextToStatListing(sessionID,"Server " + getPacketType(retVal[5])+" packet received.",1,numOfLines);

        /* Packet Length
           byte[s]: 0 thru 3
        */
        packetLength=getObjectLengthFromSsh2BinaryPacket(retVal,0,UINT32);
        currentByteLocation+=UINT32;

        /* Padding Length
           byte[s]: 4
        */
        paddingLength=retVal[currentByteLocation++];
        if ( paddingLength<0 || paddingLength>255 ) {
            sbTabInstance.addTextToStatListing(sessionID,"( " + "Corrupt SSH packet encountered. <Error 0x02>" + " )",1,2);
            try { sbTabInstance.browseControlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return -1;
        }

        /* Payload
           byte[s]: 5 thru x
        */            
        payLoad=new byte[(packetLength-paddingLength-1)];
        for ( byteLooper=-1; ++byteLooper<payLoad.length; ) {
            payLoad[byteLooper]=retVal[currentByteLocation++];
        }
        return processSsh2BinaryPacket(sbTabInstance,sessionID,payLoad);
        
        //currentByteLocation+=payLoad.length;
        
        /* Random Padding [junk]
           bytes[s]: x thru y
        */
        // padding=retVal.substring(currentByteLocation,currentByteLocation+(paddingLength-1)); 
    }

    int processSsh2BinaryPacket( final ceefee.display.sb sbTabInstance, final int sessionID, byte[] packetPayLoad ) {
        /* SSH2 Operation Selection
        */
        int byteLooper;
        int objectLength;
        
        byte[] buffer;
        
        
        if ( packetPayLoad[0]==SSH_MSG_DISCONNECT ) {
            // SSH_MSG_DISCONNECT
            //    packet type
            //    disconnect code [UINT32] [machine readable]
            //    string_length [UINT32]
            //    disconnect code [string] [human readable]
            //    string_length [UINT32]
            //    language tag

            objectLength=getObjectLengthFromSsh2BinaryPacket(packetPayLoad,1+UINT32,UINT32);
            sbTabInstance.addTextToStatListing(sessionID,"( " + new String(packetPayLoad,1+UINT32+UINT32,objectLength) + " )",1,2);
            try { sbTabInstance.browseControlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return 0;
            
        } else if ( packetPayLoad[0]==SSH_MSG_KEXINIT ) {
            // SSH_MSG_KEXINIT

            return KEXINIT(sbTabInstance, sessionID,packetPayLoad);
            
        } else if ( packetPayLoad[0]==SSH_MSG_KEXDH_REPLY ) {
            /* SSH_MSG_KEXDH_REPLY           
               
               byte      SSH_MSG_KEXDH_REPLY
               string    server public host key and certificates (K_S)
               mpint     f
               string    signature of H
            */
            String keyPath;
            byte[] serversupported_session_server_host_key_algorithms;
            int currentByteLocation=0;
            byte[] K_S;
            BigInteger f;
            byte[] serverSignature;
            try {
                MessageDigest md=MessageDigest.getInstance("SHA1");

                currentByteLocation++;
                objectLength=getObjectLengthFromSsh2BinaryPacket(packetPayLoad, currentByteLocation, UINT32);
                currentByteLocation+=UINT32;
                K_S=getObjectFromSsh2BinaryPacket(packetPayLoad, currentByteLocation, objectLength);
                currentByteLocation+=K_S.length;

                objectLength=getObjectLengthFromSsh2BinaryPacket(packetPayLoad, currentByteLocation, UINT32);
                currentByteLocation+=UINT32;
                f=new BigInteger(getObjectFromSsh2BinaryPacket(packetPayLoad, currentByteLocation, objectLength));
                if ( f.compareTo(new BigInteger("1"))==-1 || f.compareTo(sbTabInstance.p)>-1 ) {
                    sbTabInstance.addTextToStatListing(sessionID,"( " + "Remote host sent an invalid 'f' value. Session terminated." + " )",0,2);
                    return -1;
                }
                currentByteLocation+=f.toByteArray().length;

                objectLength=getObjectLengthFromSsh2BinaryPacket(packetPayLoad, currentByteLocation, UINT32);
                currentByteLocation+=UINT32;
                K_S=getObjectFromSsh2BinaryPacket(packetPayLoad, currentByteLocation, objectLength);
                currentByteLocation+=K_S.length;

                //k=f^x mod p;
                sbTabInstance.K=f.modPow(sbTabInstance.x,sbTabInstance.p);

                sbTabInstance.H=new byte[1];
                for ( byteLooper=-1; ++byteLooper<sbTabInstance.SSH2_CLIENT_VERSION_STRING.length; ) {
                    sbTabInstance.H[byteLooper]=sbTabInstance.SSH2_CLIENT_VERSION_STRING[byteLooper];
                }
                for ( byteLooper-=1; ++byteLooper<sbTabInstance.SSH2_SERVER_VERSION_STRING.length; ) {
                    sbTabInstance.H[byteLooper]=sbTabInstance.SSH2_SERVER_VERSION_STRING[byteLooper];
                }
                for ( byteLooper-=1; ++byteLooper<sbTabInstance.SSH2_CLIENT_KEXINIT_PAYLOAD.length; ) {
                    sbTabInstance.H[byteLooper]=sbTabInstance.SSH2_CLIENT_KEXINIT_PAYLOAD[byteLooper];
                }
                for ( byteLooper-=1; ++byteLooper<sbTabInstance.SSH2_SERVER_KEXINIT_PAYLOAD.length; ) {
                    sbTabInstance.H[byteLooper]=sbTabInstance.SSH2_SERVER_KEXINIT_PAYLOAD[byteLooper];
                }
                for ( byteLooper-=1; ++byteLooper<K_S.length; ) {
                    sbTabInstance.H[byteLooper]=K_S[byteLooper];
                }
                buffer=sbTabInstance.e.toByteArray();
                for ( byteLooper-=1; ++byteLooper<buffer.length; ) {
                    sbTabInstance.H[byteLooper]=buffer[byteLooper];
                }
                buffer=f.toByteArray();
                for ( byteLooper-=1; ++byteLooper<buffer.length; ) {
                    sbTabInstance.H[byteLooper]=buffer[byteLooper];
                }
                buffer=sbTabInstance.K.toByteArray();
                for ( byteLooper-=1; ++byteLooper<buffer.length; ) {
                    sbTabInstance.H[byteLooper]=buffer[byteLooper];
                }

                currentByteLocation=0;

                objectLength=getObjectLengthFromSsh2BinaryPacket(K_S, currentByteLocation, UINT32);
                currentByteLocation+=UINT32;
                serversupported_session_server_host_key_algorithms=getObjectFromSsh2BinaryPacket(K_S, currentByteLocation, objectLength);
                currentByteLocation+=serversupported_session_server_host_key_algorithms.length;

                currentByteLocation=0;

                if ( new String(serversupported_session_server_host_key_algorithms).equals("ssh-dss") ) {
                    /*  "ssh-dss" key format
                         string    "ssh-dss"
                         mpint     p
                         mpint     q
                         mpint     g
                         mpint     y
                    */                
                    BigInteger p;
                    BigInteger q;
                    BigInteger g;
                    BigInteger y;


                    objectLength=getObjectLengthFromSsh2BinaryPacket(K_S, currentByteLocation, UINT32);
                    currentByteLocation+=UINT32;
                    p=new BigInteger(getObjectFromSsh2BinaryPacket(K_S, currentByteLocation, objectLength));
                    currentByteLocation+=p.toByteArray().length;

                    objectLength=getObjectLengthFromSsh2BinaryPacket(K_S, currentByteLocation, UINT32);
                    currentByteLocation+=UINT32;
                    q=new BigInteger(getObjectFromSsh2BinaryPacket(K_S, currentByteLocation, objectLength));
                    currentByteLocation+=q.toByteArray().length;

                    currentByteLocation=0;
                    objectLength=getObjectLengthFromSsh2BinaryPacket(K_S, currentByteLocation, UINT32);
                    currentByteLocation+=UINT32;
                    g=new BigInteger(getObjectFromSsh2BinaryPacket(K_S, currentByteLocation, objectLength));
                    currentByteLocation+=g.toByteArray().length;

                    objectLength=getObjectLengthFromSsh2BinaryPacket(K_S, currentByteLocation, UINT32);
                    currentByteLocation+=UINT32;
                    y=new BigInteger(getObjectFromSsh2BinaryPacket(K_S, currentByteLocation, objectLength));
                    currentByteLocation+=y.toByteArray().length;

                    /*try {
                        signature.getInstance("DSA");
                    } catch ( java.security.NoSuchAlgorithmException nsae ) {
                        System.out.println("1no such algorithm foo");
                        return -1;
                    }*/
                    /*try {
                        signature.getInstance("DSS");
                    } catch ( java.security.NoSuchAlgorithmException nsae ) {
                        System.out.println("2no such algorithm foo");
                        return -1;
                    }*/
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    System.out.println("still got x: " + sbTabInstance.x);
                    DSAPrivateKeySpec dsaKey = new DSAPrivateKeySpec(sbTabInstance.x, p, q, g);

                    KeyFactory kf = KeyFactory.getInstance("DSA");
                    try {
                        DSAPrivateKey prvkey = (DSAPrivateKey) kf.generatePrivate(dsaKey);
                    } catch ( java.security.spec.InvalidKeySpecException ikse ) {
                        System.out.println("faile it");
                    }
                    System.out.println("still got x: " + sbTabInstance.x);
                    
    
                    // Create the DSA key factory
                    //DSAKeyFactory du=KeyFactory.getInstance("DSA");
                    //KeyFactory keyFactory = KeyFactory.getInstance("SHAwithDSA");

                    // Create the DSA private key
                    KeySpec privateKeySpec = new DSAPrivateKeySpec(sbTabInstance.x, p, q, g);
                    //try {
                        //PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
                    //} catch ( java.security.spec.InvalidKeySpecException ikse ) {
                    //    System.out.flush();
                    //    System.out.println("progress?");
                    //    System.out.flush();
                    //}
    
                    // Create the DSA public key
                    KeySpec publicKeySpec = new DSAPublicKeySpec(y, p, q, g);
                    try {
                        PublicKey publicKey = KeyFactory.getInstance("DSA").generatePublic(publicKeySpec);
                        
                        
                        
                        byte[] fin=md.digest(K_S);
                    
                        Signature jon=Signature.getInstance("DSS");
                        jon.initVerify(publicKey);
                        System.out.println("l: "+jon.verify(fin));
                        System.out.println("2: "+jon.verify(K_S));
                        fin=md.digest(sbTabInstance.H);
                        System.out.println("3: "+jon.verify(fin));
                        System.out.println("4: "+jon.verify(sbTabInstance.H));
                        
                    } catch ( java.security.spec.InvalidKeySpecException ikse ) {
                        System.out.flush();
                        System.out.println("give up");
                        System.out.flush();                        
                    } catch ( java.security.InvalidKeyException ike ) {
                        System.out.flush();
                        System.out.println("ha");
                        System.out.flush();
                    } catch ( java.security.SignatureException se ) {
                        System.out.flush();
                        System.out.println("too bad");
                        System.out.flush();
                    }

                } else if ( new String(serversupported_session_server_host_key_algorithms).equals("ssh-rsa") ) {
                    /*  "ssh-rsa" key format
                         string    "ssh-rsa"
                         mpint     e
                         mpint     n
                    */

                    BigInteger e;
                    BigInteger n;


                    currentByteLocation=0;

                    objectLength=getObjectLengthFromSsh2BinaryPacket(K_S, currentByteLocation, UINT32);
                    currentByteLocation+=UINT32;
                    e=new BigInteger(getObjectFromSsh2BinaryPacket(K_S, currentByteLocation, objectLength));
                    currentByteLocation+=e.toByteArray().length;

                    objectLength=getObjectLengthFromSsh2BinaryPacket(K_S, currentByteLocation, UINT32);
                    currentByteLocation+=UINT32;
                    n=new BigInteger(getObjectFromSsh2BinaryPacket(K_S, currentByteLocation, objectLength));
                    currentByteLocation+=n.toByteArray().length;


                    System.out.println("ssh-rsa not yet implemented.");


                    return -1;

                } else {
                    sbTabInstance.addTextToStatListing(sessionID,"( " + "Invalid remote SSH2 key." + " )",0,2);
                    if ( javax.swing.JOptionPane.showConfirmDialog(mainFrameRef, "The remote host's SSH2 key does not match local records.\nWould you like to terminate the connection?", "Security Alert!", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE)==javax.swing.JOptionPane.YES_OPTION ) {
                        return -1;
                    }
                }
                /* Check KEY for variation
                   object=S_K
                */
                keyPath=String.valueOf(sbTabInstance.browseControlSocket.getInetAddress());
                keyPath=keyPath.substring(keyPath.indexOf("/")+1);
                keyPath=System.getProperty("user.dir")+System.getProperty("file.separator")+"_archive"+System.getProperty("file.separator")+"host"+keyPath+".k";
                if ( new String(mainFrameRef.fileioClassInstance.getDataFromFile(keyPath, 0, K_S.length+(8))).equals(new String(K_S))==false ) {
                    sbTabInstance.addTextToStatListing(sessionID,"( " + "Invalid remote SSH2 secure key." + " )",0,2);
                    if ( javax.swing.JOptionPane.showConfirmDialog(mainFrameRef, "The remote host's SSH2 secure key does not match local records.\nWould you like to terminate the connection?", "Alert!", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE)==javax.swing.JOptionPane.YES_OPTION ) {
                        return -1;
                    }
                }
                mainFrameRef.fileioClassInstance.storeDataToFile(keyPath, K_S, 0, K_S.length, false);



                System.out.println("public Key should be setup");

                return 0;

            } catch ( java.security.NoSuchAlgorithmException nsae ) {
                sbTabInstance.addTextToStatListing(sessionID,"( " + nsae.getMessage() + ". Session terminated." + " )",0,2);
                try { sbTabInstance.browseControlSocket.close(); } catch ( java.io.IOException ioe ) {}
                return -1;
            }
        } else {
            // ?
            return 0;

        }
    }
    
    int sendSsh2BinaryPacket( final ceefee.display.sb sbTabInstance, final int sessionID, final byte[] payLoad ) {
        byte[] binaryPacket;
        general_ssh2_binarypacket ssh2BinaryPacketClassInstance;
                
        ssh2BinaryPacketClassInstance=new general_ssh2_binarypacket(mainFrameRef,payLoad,0,new byte[0]);
        try {
            binaryPacket=ssh2BinaryPacketClassInstance.getBinaryPacket();
            if ( new String(binaryPacket).equals("-1") ) return -1;
            sbTabInstance.sshOutputStream.write(binaryPacket);
            sbTabInstance.sshOutputStream.flush();
            sbTabInstance.addTextToStatListing(sessionID,"Client " + getPacketType(payLoad[0])+" packet transmitted.",0,1);
            return 0;
        } catch ( java.io.IOException ioe ) {
            sbTabInstance.addTextToStatListing(sessionID,"( " + ioe.getMessage() + " )",1,2);
            System.out.println("write error");
            return -1;
        }
    }
            
    int NEWKEYS( final ceefee.display.sb sbTabInstance, final int sessionID ) {
        byte[] payLoad={SSH_MSG_NEWKEYS};

 
        // Send Client NEWKEYS Packet
        if ( sendSsh2BinaryPacket(sbTabInstance,sessionID,payLoad)==-1 )
            return -1;

        // Receive Server NEWKEYS Packet
        if ( mainFrameRef.ssh2ClassInstance.parseSsh2BinaryPacket(sbTabInstance,sessionID,2)==-1 )
            return -1;
        else {
            sbTabInstance.enableDisable(2,0);
            return 0;
        }

    }
    
    int disconnect( final ceefee.display.sb sbTabInstance, final int sessionID, final int reason, final byte[] description ) {
        /* byte      SSH_MSG_DISCONNECT
           uint32    reason code
           string    description [RFC2279]
           string    language tag [RFC3066]
        */
        int byteLooper;
        byte[] buffer;
        int currentByteLocation=0;
        byte[] payLoad=new byte[1+UINT32+
            UINT32+description.length+
            UINT32];

        
        
        payLoad[currentByteLocation++]=SSH_MSG_DISCONNECT;
        
        buffer=getObjectLengthForSsh2BinaryPacket(reason,UINT32);
        for ( byteLooper=-1; ++byteLooper<buffer.length; ) {
            payLoad[currentByteLocation++]=buffer[byteLooper];
        }
        
        buffer=getObjectLengthForSsh2BinaryPacket(description.length,UINT32);
        for ( byteLooper=-1; ++byteLooper<buffer.length; ) {
            payLoad[currentByteLocation++]=buffer[byteLooper];
        }
        for ( byteLooper=-1; ++byteLooper<description.length; ) {
            payLoad[currentByteLocation++]=description[byteLooper];
        }

        buffer=getObjectLengthForSsh2BinaryPacket(0,UINT32);
        for ( byteLooper=-1; ++byteLooper<buffer.length; ) {
            payLoad[currentByteLocation++]=buffer[byteLooper];
        }
        
        // Send Client DISCONNECT Packet
        if ( sendSsh2BinaryPacket(sbTabInstance,sessionID,payLoad)==-1 )
            return -1;

        // Receive Server DISCONNECT Packet
        if ( mainFrameRef.ssh2ClassInstance.parseSsh2BinaryPacket(sbTabInstance,sessionID,2)==-1 )
            return -1;
        else {
            sbTabInstance.enableDisable(2,0);
            return 0;
        }
        
    }
}