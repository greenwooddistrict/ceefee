
package ceefee.ftp;


import java.util.Calendar;

import java.util.regex.Pattern;

import java.util.StringTokenizer;


public class general {
    
    private ceefee.main mainFrameRef;
    
    private Pattern regexPattern=null;

    
    
    /** Creates a new instance of ftpClass */
    public general(final ceefee.main _mainFrameRef) {
        this.mainFrameRef=_mainFrameRef;
    }        
    
    private String getLastLineOfFtpReply( final String tStringData ) {

        int startPoint;

        int point1;

        String tBuffer;
                


        if ( tStringData.indexOf(mainFrameRef.lineSeparator)==-1 ) {
            return tStringData;
        } else if ( tStringData.lastIndexOf(mainFrameRef.lineSeparator,tStringData.lastIndexOf(mainFrameRef.lineSeparator)-1)==-1 ) {
            return tStringData;
        } else {
            startPoint=tStringData.length()-1;
            do {
                startPoint=tStringData.lastIndexOf(mainFrameRef.lineSeparator,startPoint)-1;
                if ( startPoint==-2 || startPoint==-1 ) return tStringData;
                point1=tStringData.lastIndexOf(mainFrameRef.lineSeparator,startPoint)+2;

                tBuffer=tStringData.substring(point1);
            } while (regexPattern.compile("\\d*").matcher(tBuffer.substring(0,3)).matches()==false || (tBuffer.substring(3,4).equals(" ")==false && tBuffer.substring(3,4).equals("-")==false));
            return tBuffer;
        }
    }
    
    String getElapsedTime( final ceefee.display.sb sbTabInstance, final int sessionID ) {
        if ( sbTabInstance==null || sbTabInstance.sessionID!=sessionID ) return "-1";
        
        String tdwdp;
        

        tdwdp=String.valueOf((new java.util.Date().getTime()-(sbTabInstance.reactionTime))/((double)1000));
        tdwdp=tdwdp.substring(0,tdwdp.lastIndexOf(".")+2);

        sbTabInstance.reactionTime=0;
        
        return tdwdp;
    }
    
    public void showFindFtpItemResults( final ceefee.display.sb sbTabInstance, final int sessionID ) {
        if ( sbTabInstance==null || sessionID!=sbTabInstance.sessionID ) return;
        
        sbTabInstance.addTextToStatListing(sessionID,"( "+sbTabInstance.resultsTable.getRowCount()+" Results found in "+getElapsedTime(sbTabInstance,sessionID)+" seconds. )", 1, 2, false);
        
    }
    public Object[] findFtpItem( final ceefee.display.sb sbTabInstance, final int sessionID, String startLocation, final String searchText, final boolean evince ) {
        final int connectionMethod=sbTabInstance.getConnectionMethod();

        java.util.StringTokenizer strtok=null;
        int objectLooper;
        javax.swing.table.DefaultTableModel directoryContents;
        javax.swing.table.DefaultTableModel resultsTableDataModelBuffer=new javax.swing.table.DefaultTableModel(0,5);
        String buffer="";
        Object[] retData=new Object[2];
        String currentObject;
        int tokenLooper;

        sbTabInstance.reactionTime=new java.util.Date().getTime();
        sbTabInstance.searching=true;


        if ( sbTabInstance==null || sessionID!=sbTabInstance.sessionID ) {
            return new Object[] {"-1"};
        }

        if ( startLocation.substring(startLocation.length()-1,startLocation.length()).equals("/")==false )
            startLocation=startLocation+"/";

        retData[0]=mainFrameRef.ftpClassInstance.changeWorkingDirectory(sbTabInstance, sessionID, sbTabInstance.controlSocket, startLocation, true, true);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            return retData;
        }
        sbTabInstance.currentWD=(String)(retData[0]=getWorkingDirectory(sbTabInstance, sessionID, sbTabInstance.controlSocket,false));
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            sbTabInstance.currentWD="";
            sbTabInstance.addTextToStatListing(sessionID,"( "+retData[0].toString().substring(2)+" )", 1, 2, false);
            return retData;
        }

        if ( connectionMethod==1 ) {
            retData=mainFrameRef.ftpClassInstance.performPortOperation(sbTabInstance,sessionID,sbTabInstance.controlSocket,sbTabInstance.dataSocket,sbTabInstance.hostAddress.getText(),sbTabInstance.hostPort.getText(),sbTabInstance.hostUsername.getText(),new String(sbTabInstance.hostPassword.getPassword()),"LIST","",false);
        } else {
            retData=mainFrameRef.ftpClassInstance.performPasvOperation(sbTabInstance,sessionID,sbTabInstance.controlSocket,sbTabInstance.dataSocket,sbTabInstance.hostAddress.getText(),sbTabInstance.hostPort.getText(),sbTabInstance.hostUsername.getText(),new String(sbTabInstance.hostPassword.getPassword()),"LIST","",false);
        }

        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            sbTabInstance.addTextToStatListing(sessionID,"( "+retData[0].toString().substring(2)+" )",1,2,false);
            return retData;
        }
        retData=mainFrameRef.ftpClassInstance.ftpGetResponse(sbTabInstance, sessionID, sbTabInstance.dataSocket, mainFrameRef.connectionTimeout, 0);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            sbTabInstance.addTextToStatListing(sessionID,"( "+retData[0].toString().substring(2)+" )",1,2,false);
            return retData;
        }

        try { ((java.net.Socket)sbTabInstance.dataSocket).close(); } catch ( java.io.IOException ioe ) {}

        directoryContents=mainFrameRef.ftpClassInstance.getDirectoryContents(sbTabInstance, sessionID, sbTabInstance.currentWD, retData[0].toString(), sbTabInstance.serverHeader);
        if ( directoryContents==null ) {
            if ( sbTabInstance!=null && evince ) sbTabInstance.addTextToStatListing(sessionID,"( "+retData[0].toString().substring(2)+" )",1,1,false);
            showFindFtpItemResults(sbTabInstance,sessionID);
            return new Object[] {"-1"};
        }

        //closing connection message
        retData=mainFrameRef.ftpClassInstance.ftpGetResponse(sbTabInstance, sessionID, sbTabInstance.controlSocket, mainFrameRef.connectionTimeout, 0);

        for ( objectLooper=-1; ++objectLooper<directoryContents.getRowCount(); ) {
            if ( sbTabInstance.searching==false ) {
                return new Object[] {"-1"};
            }
            if ( (currentObject=directoryContents.getValueAt(objectLooper,0).toString()).substring(0,2).equals("./") ) {
                // Process Folder
                if ( (currentObject=currentObject.substring(2)).equals(".")==false && currentObject.equals("..")==false ) {
                    sbTabInstance.addTextToStatListing(sessionID,"",0,1,false);
                    sbTabInstance.addTextToStatListing(sessionID,"( "+"Searching in: " + startLocation+currentObject+" )",1,2,false);
                    if ( findFtpItem(sbTabInstance, sessionID, startLocation+currentObject, searchText, evince)==null )
                        return new Object[] {"-1"};
                }

            } else {
                // Process File
                currentObject=currentObject.toLowerCase();
                strtok=new java.util.StringTokenizer(searchText," ");
                for ( tokenLooper=-1; ++tokenLooper<strtok.countTokens(); ) {
                    if ( currentObject.indexOf((buffer=strtok.nextToken()).toLowerCase())==-1 )
                        break;
                    else if ( strtok.countTokens()==0 ) {
                        sbTabInstance.resultsTableDataModel.addRow(new String[] {directoryContents.getValueAt(objectLooper,0).toString(), directoryContents.getValueAt(objectLooper,1).toString(), directoryContents.getValueAt(objectLooper,2).toString(), directoryContents.getValueAt(objectLooper,3).toString(), directoryContents.getValueAt(objectLooper,4).toString()});
                        if ( sbTabInstance.busy ) sbTabInstance.enableDisable(2, 0);
                    }
                }
                
            }
        }

        if ( sbTabInstance.startLocation.getText().equals(startLocation) || sbTabInstance.startLocation.getText().equals(startLocation.substring(0,startLocation.length()-1)) ) {
            sbTabInstance.addTextToStatListing(sessionID,"",1,1,false);
            sbTabInstance.addTextToStatListing(sessionID,"( "+"Search complete!"+" )",1,1,false);
            showFindFtpItemResults(sbTabInstance, sessionID);
        } else if ( sessionID!=sbTabInstance.sessionID ) {
            sbTabInstance.addTextToStatListing(sessionID,"( "+"Search Cancelled."+" )",1,2,false);
        }
        return new Object[] {""};
        
    }
    
    public void displaydir(final ceefee.display.sb sbTabInstance, final int sessionID, final java.net.Socket controlSocket, final Object dataSocket, final boolean killSockets, final boolean evince, final boolean corrected) {
        new Thread(new ceefee.ftp.displaydirectory(mainFrameRef, sbTabInstance, sessionID, controlSocket, dataSocket, killSockets, evince, corrected)).start();
    }

    public void processUnknownItem(final ceefee.display.sb sbTabInstance, final ceefee.display.currentstate currentstateInstance, final String selectedItem, String itemPath ) {
        String whimsicalString;

        //try for folder first (change directory)
        whimsicalString=mainFrameRef.ftpClassInstance.changeWorkingDirectory(sbTabInstance,sbTabInstance.sessionID, sbTabInstance.controlSocket,itemPath+selectedItem,false,true);
        if ( whimsicalString.indexOf("Permission denied")!=-1 ) {
            sbTabInstance.addTextToStatListing(sbTabInstance.sessionID,"CWD "+itemPath+selectedItem,0,1,false);
            sbTabInstance.addTextToStatListing(sbTabInstance.sessionID,whimsicalString.substring(2),1,2,false);
            sbTabInstance.enableDisable(2,0);
        } else if ( whimsicalString.length()>1 && whimsicalString.substring(0,2).equals("-1") ) {
            //download it
            sbTabInstance.enableDisable(2,0);
            if ( itemPath.substring(itemPath.length()-1,itemPath.length()).equals("/") )
                itemPath=itemPath.substring(0,itemPath.length()-1);
            mainFrameRef.ftpClassInstance.startFtpDownload(sbTabInstance,sbTabInstance.sessionID,currentstateInstance,sbTabInstance.controlSocket,sbTabInstance.dataSocket,selectedItem,itemPath,false,null,mainFrameRef.downloadDir, "0", sbTabInstance.serverHeader, sbTabInstance.hostAddress.getText(), sbTabInstance.hostPort.getText(), sbTabInstance.hostUsername.getText(), new String(sbTabInstance.hostPassword.getPassword()), String.valueOf(sbTabInstance.getDataFormat()), String.valueOf(sbTabInstance.getConnectionMethod()), false, false);
            mainFrameRef.showPanel(mainFrameRef.VIEW_PANEL);
        } else {
            mainFrameRef.ftpClassInstance.displaydir(sbTabInstance,sbTabInstance.sessionID,sbTabInstance.controlSocket,sbTabInstance.dataSocket,false,true,false);
        }
    }
    
    public synchronized Object[] ensureFtpControlSocketConnection(final ceefee.display.sb sbTabInstance, final int sessionID, java.net.Socket socketInstance, String hostAddress, String hostPort, String hostUsername, String hostPassword, final int dataFormat, final boolean evince ) {
        byte[] byteArray=new byte[mainFrameRef.connectionMTU];
        int byteLen;
        String byteArrayString;


        if ( socketInstance!=null ) {
            try {
                socketInstance.setSoTimeout(1);

                byteLen=socketInstance.getInputStream().read(byteArray); //should return EOF?
                if ( byteLen!=-1 ) {
                    byteArrayString=new String(byteArray,0,byteLen);
                    if ( byteArrayString.indexOf("221")==-1 && byteArrayString.indexOf("421")==-1 ) {
                        return new Object[] {socketInstance};
                    }
                }
            } catch ( java.io.IOException ioe ) {
                if ( ioe.getMessage().equals("Read timed out") ) {
                    if ( mainFrameRef.utilitiesClassInstance.isInternalIP(socketInstance.getLocalAddress().getHostAddress())==false || mainFrameRef.utilitiesClassInstance.getHighestConnectionIP().equals(socketInstance.getLocalAddress().getHostAddress()) ) {
                        return new Object[] {socketInstance};
                    } else {
                        if ( sbTabInstance!=null ) sbTabInstance.iffyConnection=true;
                    }
                } else if ( ioe.getMessage().equals("Connection reset")==false && ioe.getMessage().equals("Socket Closed")==false && ioe.getMessage().equals("Socket is closed")==false && ioe.getMessage().equals("Socket is not connected")==false ) {
                    if ( sbTabInstance!=null ) sbTabInstance.iffyConnection=true;
                    return new Object[] {socketInstance};
                }
            }

            //reconnect socket
            try {
                socketInstance.close();
                socketInstance=null;
            } catch ( java.io.IOException ioe ) {}
        }

        return connectFtpControlSocket(sbTabInstance,sessionID,hostAddress,hostPort,hostUsername,hostPassword,dataFormat,false,evince);
    }
    
    public String getWorkingDirectory( final ceefee.display.sb sbTabInstance, final int sessionID, final java.net.Socket controlSocket, final boolean evince ) {
        Object[] retData=new Object[2];
                
        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance, sessionID, controlSocket, "PWD", true, 1);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            sbTabInstance.reconnectAttempt=0;
            if ( sbTabInstance!=null && evince ) sbTabInstance.addTextToStatListing(sessionID,"( "+retData[0].toString().substring(2)+" )",1,1,false);
            return retData[0].toString();
        }
        
        if ( retData[0].toString().indexOf("\"")!=-1 ) {
            if ( retData[0].toString().indexOf("\"",retData[0].toString().indexOf("\"")+1)!=-1 ) {
                sbTabInstance.currentWD=retData[0].toString().substring(retData[0].toString().indexOf("\"")+1,retData[0].toString().indexOf("\"",retData[0].toString().indexOf("\"")+1));
                if ( sbTabInstance.currentWD.substring(sbTabInstance.currentWD.length()-1).equals("/")==false )
                    sbTabInstance.currentWD=sbTabInstance.currentWD+"/";
                if ( sbTabInstance.currentWD.substring(0,1).equals("/")==false )
                    sbTabInstance.currentWD="/"+sbTabInstance.currentWD;
            } else {
                sbTabInstance.currentWD="/";
                return "-1";
            }
        } else {
            sbTabInstance.currentWD="/";
            return "-1";
        }

        sbTabInstance.activityListLock=true;
        sbTabInstance.activityList.addItem(sbTabInstance.currentWD);
        sbTabInstance.activityList.setSelectedItem(sbTabInstance.currentWD);
        sbTabInstance.activityListLock=false;

        return sbTabInstance.currentWD;
    }

    public Object[] connectFtpDataSocket( final ceefee.display.sb sbTabInstance, final int sessionID, final java.net.Socket controlSocket, final int pasvportMode, final boolean evince ) {
        Object dataSocket=null;
        
        Object[] retData=new Object[2];
        
        java.util.StringTokenizer remoteFtpDtpInfo;

        String remoteDataAddy;

        String bufferAddress="";
        String bufferPortByte1="";
        String bufferPortByte2="";

        int portRegister;





        if ( pasvportMode==1 ) {   
            try {
                dataSocket=new java.net.ServerSocket(0,0); //0 backlog slots - program is a servlet not a server
            } catch ( java.io.IOException ioe ) {
                if ( evince ) {
                    if ( sbTabInstance!=null ) sbTabInstance.addTextToStatListing(sessionID,"( " + ioe.getMessage() +". )",1,2,false);
                }
                try { ((java.net.ServerSocket)dataSocket).close(); } catch ( java.io.IOException ioe1 ) {}
                return new Object[] {"-1"};
            }

            portRegister=((java.net.ServerSocket)dataSocket).getLocalPort();
            bufferAddress=mainFrameRef.utilitiesClassInstance.getHighestConnectionIP();
            if ( sbTabInstance!=null ) {
                if ( (bufferAddress.length()>1 && bufferAddress.substring(0,2).equals("-1")) ) {
                    if ( evince )
                        sbTabInstance.addTextToStatListing(sessionID,"( " + bufferAddress +". )",1,2,false);
                    try { ((java.net.ServerSocket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}
                    return new Object[] {bufferAddress};
                    
                } else if ( (sbTabInstance!=null && sbTabInstance.sessionID!=sessionID) ) {
                    try { ((java.net.ServerSocket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}
                    return new Object[] {"-1"};
                }
            }
            bufferPortByte1=String.valueOf(portRegister/256);
            bufferPortByte2=String.valueOf(portRegister-(Integer.parseInt(bufferPortByte1)*256));

            remoteDataAddy=bufferAddress.replace('.', ',') + "," + bufferPortByte1 + "," + bufferPortByte2;

            retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance, sessionID, controlSocket, "PORT " + remoteDataAddy, true, (evince?1:0));
            if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                try { ((java.net.ServerSocket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}
                return retData;
            } else if ( retData[0].toString().equals("") ) {
                try { ((java.net.ServerSocket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}
                return new Object[] {"-1"};
            } else if ( (sbTabInstance!=null && sbTabInstance.sessionID!=sessionID) ) {
                try { ((java.net.ServerSocket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}
                return new Object[] {"-1"};
            }
            
        } else {

            retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance, sessionID, controlSocket, "PASV", true, (evince?1:0));
            System.out.println(System.getProperty("line.separator")+dataSocket);
            if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                if (dataSocket!=null)
                    try { ((java.net.ServerSocket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}
                return retData;
            } else if ( retData[0].toString().equals("") ) {
                if (dataSocket!=null)
                    try { ((java.net.ServerSocket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}
                return new Object[] {"-1"};
            } else if ( (sbTabInstance!=null && sbTabInstance.sessionID!=sessionID) ) {
                if (dataSocket!=null)
                    try { ((java.net.ServerSocket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}
                return new Object[] {"-1"};
            }

            if ( retData[0].toString().indexOf("(")==-1 ) {
                if ( evince ) {
                    if ( sbTabInstance!=null ) sbTabInstance.addTextToStatListing(sessionID,"( Invalid response received. )",1,2,false);
                }
                try { ((java.net.ServerSocket)dataSocket).close(); } catch ( java.io.IOException ioe ) {}
                return retData;
            }

            bufferAddress=retData[0].toString().substring(retData[0].toString().indexOf("(")+1,retData[0].toString().indexOf(")"));

            remoteFtpDtpInfo= new java.util.StringTokenizer(bufferAddress.substring(0,bufferAddress.lastIndexOf(",",bufferAddress.lastIndexOf(",")-1)),",");

            remoteDataAddy=remoteFtpDtpInfo.nextToken() + "." + remoteFtpDtpInfo.nextToken() + "." + remoteFtpDtpInfo.nextToken() + "." + remoteFtpDtpInfo.nextToken();

            remoteFtpDtpInfo=new java.util.StringTokenizer( bufferAddress.substring(bufferAddress.lastIndexOf(",",bufferAddress.lastIndexOf(",")-1)+1,bufferAddress.length()),",");

            bufferPortByte1=remoteFtpDtpInfo.nextToken();
            bufferPortByte2=remoteFtpDtpInfo.nextToken();

            portRegister=(Integer.parseInt(bufferPortByte1)*256)+Integer.parseInt(bufferPortByte2);

            try {
                if ( dataSocket!=null ) ((java.net.Socket)dataSocket).close();
                dataSocket=new java.net.Socket(remoteDataAddy,portRegister);
            } catch ( java.io.IOException ioe ) {
                if ( evince ) {
                    if ( sbTabInstance!=null ) sbTabInstance.addTextToStatListing(sessionID,"( " + ioe.getMessage() + ". )",1,2,false);
                }
                try { ((java.net.ServerSocket)dataSocket).close(); } catch ( java.io.IOException ioe1 ) {}
                return new Object[] {"-1"};
            }
        }
        
        return new Object[] {dataSocket};
    }

    public Object[] connectFtpControlSocket(final ceefee.display.sb sbTabInstance, final int sessionID, final String hostAddress, final String hostPort, final String hostUsername, final String hostPassword, final int dataFormat, final boolean disableNagle, final boolean evince) {
        String serverHeader;
        Object[] retData=new Object[2];
        java.net.Socket controlSocket;
        final String actualDataFormat;


        if ( sbTabInstance!=null && sessionID==sbTabInstance.sessionID && evince ) sbTabInstance.addTextToStatListing(sessionID,"( " + (java.util.Calendar.getInstance().getTime()) + " )", 1,1,false);
        if ( sbTabInstance!=null && sessionID==sbTabInstance.sessionID && evince ) sbTabInstance.addTextToStatListing(sessionID,"( Connecting. Please Wait... )", 1, 1, false);

        retData=mainFrameRef.socketClassInstance.connectSocket(sbTabInstance, sessionID, hostAddress,hostPort,evince);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            return retData;
        }
        controlSocket=(java.net.Socket)retData[0];

        retData=mainFrameRef.ftpClassInstance.ftpGetResponse(sbTabInstance, sessionID, controlSocket, mainFrameRef.connectionTimeout, (evince?1:0));
        serverHeader=retData[0].toString();
        if ( sbTabInstance!=null ) sbTabInstance.serverHeader=serverHeader;
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return retData;
        } else if ( retData[0].toString().equals("") ) {
            try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return new Object[] {"-1"};
        } else if ( (sbTabInstance!=null && sbTabInstance.sessionID!=sessionID) ) {
            try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return new Object[] {"-1"};
        }

        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance, sessionID,controlSocket, "USER " + hostUsername, true, (evince?1:0));
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return retData;
        } else if ( retData[0].toString().equals("") ) {
            try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return new Object[] {"-1"};
        } else if ( (sbTabInstance!=null && sbTabInstance.sessionID!=sessionID) ) {
            try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return new Object[] {"-1"};
        }

        if ( sbTabInstance!=null && sessionID==sbTabInstance.sessionID && evince ) sbTabInstance.addTextToStatListing(sessionID, "PASS ********", 0, 1, false);
        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance, sessionID, controlSocket, "PASS " + hostPassword, false, 0);
        retData=mainFrameRef.ftpClassInstance.ftpGetResponse(sbTabInstance, sessionID, controlSocket, mainFrameRef.connectionTimeout, 1);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return retData;
        } else if ( retData[0].toString().equals("") ) {
            if ( sbTabInstance!=null && sessionID==sbTabInstance.sessionID ) sbTabInstance.addTextToStatListing(sessionID,"( " + "No response received." + " )",1,2,false);
            try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return new Object[] {"-1"};
        } else if ( (sbTabInstance!=null && sbTabInstance.sessionID!=sessionID) ) {
            try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return new Object[] {"-1"};
        }

        if ( dataFormat==0 ) {
            actualDataFormat = "I";
        } else {
            actualDataFormat = "A";
        }
        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance, sessionID, controlSocket, "TYPE " + actualDataFormat,true,(evince?1:0));
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return retData;
        } else if ( retData[0].toString().equals("") ) {
            if ( sbTabInstance!=null && sessionID==sbTabInstance.sessionID ) sbTabInstance.addTextToStatListing(sessionID,"( " + "No response received." + " )",1,2,false);
            try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return new Object[] {"-1"};
        } else if ( (sbTabInstance!=null && sbTabInstance.sessionID!=sessionID) ) {
            try { controlSocket.close(); } catch ( java.io.IOException ioe ) {}
            return new Object[] {"-1"};
        }

        if ( retData[0].toString().length()>0 && retData[0].toString().substring(0,1).equals("2")==false )//indexOf("Type set to")==-1 && retData[0].toString().indexOf("Type ok")==-1 && retData[0].toString().indexOf("Switching to ")==-1 ) {
            retData=mainFrameRef.ftpClassInstance.ftpGetResponse( sbTabInstance, sessionID, controlSocket, mainFrameRef.connectionTimeout, (evince?1:0));

        return new Object[] {controlSocket,serverHeader};
    }
    
    public Object[] ftpGetResponse(final ceefee.display.sb sbTabInstance, final int sessionID, Object socketInstance, final int transmissionDelay, final int linesEvinced ) {
        String stringAggregate="";
        String newData;
        String lastReply;
        
        int iteration=0;
        
        boolean getMore=false;
        
        byte[] rawSocketData;
        int bytesRead=0;
        
        

        do {
            if ( stringAggregate!=null ) {
                if ( sbTabInstance!=null && sessionID!=sbTabInstance.sessionID )
                    return new Object[] {"-1"+stringAggregate};
            }

            iteration++;
            rawSocketData=new byte[mainFrameRef.connectionMTU];
            try {
                if (!socketInstance.getClass().getName().equals("java.net.Socket"))
                    return new Object[] {socketInstance};
                //System.out.print(socketInstance.getClass().getName()+System.getProperty("line.separator"));
                ((java.net.Socket)socketInstance).setSoTimeout(transmissionDelay);
                bytesRead=((java.net.Socket)socketInstance).getInputStream().read(rawSocketData);
            } catch ( java.io.IOException ioe ) {
                if ( linesEvinced>0 ) {
                    if ( sbTabInstance!=null ) sbTabInstance.addTextToStatListing(sessionID,"( " + ioe.getMessage() + " )",1,2,false);
                }

                //if ( ioe.getMessage().equals("Read timed out") ) {
                    if ( stringAggregate==null || stringAggregate.equals("") ) {
                        return new Object[] {(String)"-1"+stringAggregate};
                    } else {
                        return  new Object[] {(String)stringAggregate,socketInstance};
                    }
                //} else {
                //    return stringAggregate;
                //}
            }
            if ( bytesRead==-1 )
                return new Object[] {stringAggregate,socketInstance};
            
            stringAggregate=stringAggregate+(newData=new String(rawSocketData, 0, bytesRead));
            if ( stringAggregate.length()>0 && stringAggregate.substring(stringAggregate.length()-1).equals("\n")==false ) {
                getMore=true;
                continue;
            }
            if ( stringAggregate.equals("") || stringAggregate.length()<4 )
                return new Object[] {"-1"+stringAggregate};
            
            lastReply = getLastLineOfFtpReply(stringAggregate).substring(0, 4);

            if ( lastReply.substring(0,4).equals("221 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "221 ")!=-1 ) {
                //service closing control connection.
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("421 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "421 ")!=-1 ) {
                //service not available, closing control connection.
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("425 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "425 ")!=-1 ) {
                //425 Can't open data connection.
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("426 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "426 ")!=-1 ) {
                //Connection closed; transfer aborted.
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("450 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "450 ")!=-1 ) {
                //Requested file action not taken.
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("451 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "451 ")!=-1 ) {
                //Requested action aborted: local error in processing.
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("452 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "452 ")!=-1 ) {
                //Requested action not taken
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("500 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "500 ")!=-1 ) {
                //Syntax error, command unrecognized. This may include errors such as command line too long.
                getMore=false;                    
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("501 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "501 ")!=-1 ) {
                //Syntax error in parameters or arguments.
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("502 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "502 ")!=-1 ) {               
                //Command not implemented.
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("503 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "503 ")!=-1 ) {
                //Bad sequence of commands.
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("504 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "504 ")!=-1 ) {
                //Command not implemented for that parameter.
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("530 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "530 ")!=-1 ) {
                //Not logged in.
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("550 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "550 ")!=-1 ) {
                //requested action not taken
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("552 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "552 ")!=-1 ) {
                //Requested file action aborted.
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else if ( lastReply.substring(0,4).equals("553 ") || stringAggregate.indexOf(mainFrameRef.lineSeparator + "553 ")!=-1 ) {
                //requested action not taken
                getMore=false;
                if ( stringAggregate.substring(stringAggregate.length()-2).equals(mainFrameRef.lineSeparator) ) stringAggregate=stringAggregate.substring(0,stringAggregate.length()-2);
                if ( sbTabInstance!=null ) {
                    if ( linesEvinced>0 ) {
                        if ( stringAggregate.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,"( " + stringAggregate + " )",1,2,false);
                    }
                }
                return new Object[] {"-1"+stringAggregate};
            } else {
                if ( lastReply.substring(3,4).equals("-") ) {
                    getMore=true;
                    //connectionDelay=mainFrameRef.connectionTimeout;
                } else if ( regexPattern.compile("\\d*").matcher(lastReply.substring(0,3)).matches() && lastReply.substring(3,4).equals(" ") ) {
                    getMore=false;
                } else if ( regexPattern.compile("\\d*").matcher(lastReply.substring(0,3)).matches()==false ) {
                    getMore=true;
                }
            }

            if ( linesEvinced>0 ) {
                if ( sbTabInstance!=null && sbTabInstance.sessionID==sessionID) {
                    if ( regexPattern.compile("\\d*").matcher(lastReply.substring(0,3)).matches() && (lastReply.substring(3,4).equals("-")||lastReply.substring(3,4).equals(" ")) ) {
                        sbTabInstance.addTextToStatListing(sessionID, newData, 1, 1, false);
                        sbTabInstance.addTextToStatListing(sessionID, "", 0, linesEvinced-1, false);
                    } else {
                        sbTabInstance.addTextToStatListing(sessionID, "... '"+stringAggregate.length()+"' bytes received.", 0, linesEvinced, true);
                    }
                }
            }

        } while ( getMore );
        
        return new Object[] {stringAggregate,socketInstance};
    }
    
    /*private String[] ~splitDirectoryData( final String rawData, final String sentinelValue ) {
        String returnArray[]={};
        int svLocation;
        final String shortcutSeparator=" -> ";
        int ssLocation;
        final String proprietarySeparator1=".:";
        int ps1Location;
                
        java.util.Vector vector=new java.util.Vector(0,1);

        int whimsicalByteLocation;

        java.util.StringTokenizer strtok;
        
        strtok=new java.util.StringTokenizer(rawData," ");

        for ( int rawDataLooper=-1; ++rawDataLooper<rawData.length(); ) {
            //if ( rawData.substring(rawDataLooper,rawDataLooper+1).equals(sentinelValue) )
            //    continue;

            svLocation=rawData.indexOf(sentinelValue,rawDataLooper);
            ssLocation=rawData.indexOf(shortcutSeparator,rawDataLooper);
            ps1Location=rawData.indexOf(proprietarySeparator1,rawDataLooper);

            if ( svLocation!=-1 && ( svLocation<(ssLocation-1) || ssLocation==-1 ) && ( svLocation<ps1Location || ps1Location==-1 ) ) {
                whimsicalByteLocation=rawData.indexOf(sentinelValue,rawDataLooper);
                if ( rawData.substring(whimsicalByteLocation-1,whimsicalByteLocation).equals(",") )
                    continue;
                vector.add(rawData.substring(rawDataLooper,whimsicalByteLocation));
            } else if ( ssLocation!=-1 && ( ssLocation<svLocation || svLocation==-1 ) && ( ssLocation<ps1Location || ps1Location==-1 ) ) {
                whimsicalByteLocation=rawData.indexOf(shortcutSeparator,rawDataLooper);
                vector.add(rawData.substring(rawDataLooper,whimsicalByteLocation));
            } else if ( ps1Location!=-1 ) { // && ( ps1Location<svLocation || svLocation==-1 ) && ( ps1Location<ssLocation || ssLocation==-1 ) ) {
                vector.add("..");
            } else {
                vector.add(rawData.substring(rawDataLooper));
                break;
            }
            rawDataLooper+=vector.get(vector.size()-1).toString().length();
            
        }

        returnArray=new String[vector.size()];
        for ( int arrayLooper=-1; ++arrayLooper<vector.size(); ) {
            returnArray[arrayLooper]=vector.get(arrayLooper).toString();
        }
        return returnArray;

    }*/

    private String[] splitDirectoryData( final String rawData, final String sentinelValue ) {
        String returnArray[]={};
        java.util.Vector vector=new java.util.Vector(0,1);
        int whimsicalByteLocation=0;
        String whimsicalString;
        
        
        for ( int byteLooper=0; byteLooper<rawData.length(); ) {
            if ( rawData.substring(byteLooper,byteLooper+sentinelValue.length()).equals(sentinelValue)==false ) {
                if ( (whimsicalByteLocation=rawData.indexOf(sentinelValue,byteLooper+1))!=-1 ) {
                    if ( (whimsicalString=rawData.substring(byteLooper,whimsicalByteLocation).trim()).equals("0,") ) {
                        byteLooper+=whimsicalString.length();
                    } else {
                        vector.add(whimsicalString);
                        byteLooper+=whimsicalString.length();
                    }
                } else {
                    vector.add(rawData.substring(byteLooper).trim());
                    break;
                }
            } else
                byteLooper++;

            /*            } else if ( inSentinelValue ) {
                if ( rawData.substring(byteLooper,byteLooper+2).equals("0,") ) {
                    byteLooper=rawData.indexOf(sentinelValue,byteLooper+2);
                    continue;
                } else {
                    if ( rawData.substring(byteLooper,byteLooper+2).equals("->") ) {
                        if ( rawData.indexOf(sentinelValue,byteLooper+3)==-1 ) {
                            vector.add(rawData.substring(startOfChunk));
                            break;
                        } else
                            vector.add(rawData.substring(startOfChunk,rawData.indexOf(sentinelValue,byteLooper+2)));
                    } else {
                        vector.add(rawData.substring(startOfChunk,byteLooper-1));
                        startOfChunk=byteLooper;

                        inSentinelValue=false;
                    }

                }
             */
        }
        
        returnArray=new String[vector.size()];
        for ( int arrayLooper=-1; ++arrayLooper<vector.size(); ) {
            returnArray[arrayLooper]=vector.get(arrayLooper).toString();
        }
        return returnArray;
    }
    
    javax.swing.table.DefaultTableModel getDirectoryContents( final ceefee.display.sb sbTabInstance, final int sessionID, final String workingDirectory, String rawDirectoryData, final String serverHeader ) {
        if ( sbTabInstance!=null )sbTabInstance.addTextToStatListing(sessionID, "... '"+rawDirectoryData.length()+"' bytes received.", 0, 1, true);
        
        String attributes;
        String subItems;
        String ftpUser;
        String size;
        String month;
        String day;
        String year;
        String hour;
        String minute;
        String name;
        //String user;
        //String owner; //a.k.a. user

        int nts;
        int ts;

        java.util.Calendar calendarInstance=java.util.Calendar.getInstance();
        
        String currentLine;
        int whimsicalByteLocation;
        String permutedPieces[]={};
        String splitChars="\n";
        String splitChars2=String.valueOf((char)0x10);
        int currentPos=0;
         
            
        javax.swing.table.DefaultTableModel resultsTableDataModelBuffer=new javax.swing.table.DefaultTableModel(0,5);
        
        
        
        if ( rawDirectoryData.length()==0 ) {
            return resultsTableDataModelBuffer;
        } else {
            if ( rawDirectoryData.indexOf(splitChars)==-1 ) {
                splitChars = splitChars2;
                if ( rawDirectoryData.indexOf(splitChars)==-1 ) {
                    //don't know how to parse...
                    if ( sbTabInstance!=null ) sbTabInstance.enableDisable(2,0);
                    if ( sbTabInstance!=null ) sbTabInstance.addTextToStatListing(sessionID,"( Invalid and/or corrupt data received from host. )",0,2,false);
                    return resultsTableDataModelBuffer;
                }
            }
            if ( rawDirectoryData.indexOf("total ")!=-1 ) {
                whimsicalByteLocation=rawDirectoryData.indexOf("total ");
                if ( whimsicalByteLocation!=-1 ) {
                    rawDirectoryData = rawDirectoryData.replaceAll(rawDirectoryData.substring(whimsicalByteLocation,rawDirectoryData.indexOf(splitChars,whimsicalByteLocation+1)+splitChars.length()),"");
                }
            }
        }

        if ( rawDirectoryData.equals("") ) {
            return resultsTableDataModelBuffer;
        }

        do {
            if ( sbTabInstance!=null && sessionID!=sbTabInstance.sessionID )
                break;
                    
            currentLine=rawDirectoryData.substring(currentPos,rawDirectoryData.indexOf(splitChars,currentPos));
            
            currentPos = rawDirectoryData.indexOf(splitChars,currentPos)+splitChars.length();

            permutedPieces = splitDirectoryData(currentLine, " ");

            /*javax.swing.JOptionPane.showMessageDialog(
            null,"permutedPieces: " + permutedPieces.length
            + "\nmainClass.lineSeparator  Attributes:  " + permutedPieces[0]
            + "\nmainClass.lineSeparator  SubItems:    " + permutedPieces[1] 
            + "\nmainClass.lineSeparator  User:        " + permutedPieces[2]
            + "\nmainClass.lineSeparator  FtpUser:     " + permutedPieces[3]
            + "\nmainClass.lineSeparator  Size:        " + permutedPieces[4]
            + "\nmainClass.lineSeparator  Month:       " + permutedPieces[5]
            + "\nmainClass.lineSeparator  Day:         " + permutedPieces[6]
            + "\nmainClass.lineSeparator  Year:        " + permutedPieces[7]
            + "\nmainClass.lineSeparator  Directory:   " + permutedPieces[8]
            + "\n" + currentLine);*/

            //user=permutedPieces[2];
            //ftpUser=permutedPieces[3];
            name="";
            attributes="";
            subItems="";
            ftpUser="";
            size="0";
            month="";
            day="";
            year="";
            hour="";//"0";
            minute="";//"0";
            //String user="";
            //owner=""; //a.k.a. user

            if ( serverHeader.indexOf("QTCP")!=-1 ) {
                if ( permutedPieces.length>3 ) {
                    nts=6;

                    for ( int nameLooper=nts-1-1; ++nameLooper<permutedPieces.length; ) {
                        if ( name.equals("") ) {
                            name=permutedPieces[nameLooper];
                        } else {
                            name=name + " " + permutedPieces[nameLooper];
                        }
                    }

                    attributes=permutedPieces[nts-2];
                    subItems=permutedPieces[1];

                    if ( attributes.equals("*MEM")==false && permutedPieces.length>1 ) {
                        size=permutedPieces[nts-5];
                        whimsicalByteLocation=permutedPieces[nts-4].indexOf("/");
                        month=permutedPieces[nts-4].substring(0,whimsicalByteLocation);
                        day=permutedPieces[nts-4].substring(whimsicalByteLocation+1,permutedPieces[nts-4].indexOf("/",whimsicalByteLocation+1));
                        whimsicalByteLocation=permutedPieces[nts-4].indexOf("/",whimsicalByteLocation+1);
                        year=permutedPieces[nts-4].substring(whimsicalByteLocation+1,permutedPieces[nts-4].length());
                        //owner=permutedPieces[0];
                    } else {
                        //not necessarily human readable
                        //size="0";
                    }
                } else if ( permutedPieces.length==2 ) {
                    name=permutedPieces[permutedPieces.length-1];
                    attributes="*FILE";
                    //owner=permutedPieces[0];
                } else if ( permutedPieces.length==3 ) {
                    name=permutedPieces[permutedPieces.length-1];
                    attributes=permutedPieces[permutedPieces.length-2];
                    //owner=permutedPieces[0];
                }
            } else if ( serverHeader.indexOf("NetPresenz v4")!=-1 ) {
                attributes=permutedPieces[0];
                if ( permutedPieces.length==7 ) {
                    //folder
                    nts=7;
                    month=permutedPieces[3];
                    day=permutedPieces[4];
                    year=permutedPieces[5];
                } else {
                    nts=8;
                    size=permutedPieces[3];
                    month=permutedPieces[4];
                    day=permutedPieces[5];
                    year=permutedPieces[6];
                }
                //owner=permutedPieces[2];

                for ( int nameLooper=nts-1-1; ++nameLooper<permutedPieces.length; ) {
                    if ( name.equals("") ) {
                        name=permutedPieces[nameLooper];
                    } else {
                        name=name + " " + permutedPieces[nameLooper];
                    }
                }

            } else if ( serverHeader.indexOf("(Version wu-2.6.1(1)")!=-1 ) {
                nts=9;

                attributes=permutedPieces[0];
                //owner=permutedPieces[3];
                size=permutedPieces[4];
                month=permutedPieces[5];
                day=permutedPieces[6];
                year=permutedPieces[7];

                for ( int nameLooper=nts-1-1; ++nameLooper<permutedPieces.length; ) {
                    if ( name.equals("") ) {
                        name=permutedPieces[nameLooper];
                    }
                    else {
                        name=name + " " + permutedPieces[nameLooper];
                    }
                }

            } else if ( serverHeader.indexOf("(Version wu-2.6.1(2)")!=-1 ) {
                nts=8;

                attributes=permutedPieces[0];
                //owner=permutedPieces[2];
                size=permutedPieces[3];
                month=permutedPieces[4];
                day=permutedPieces[5];
                year=permutedPieces[6];

                for ( int nameLooper=nts-1-1; ++nameLooper<permutedPieces.length; ) {
                    if ( name.equals("") ) {
                        name=permutedPieces[nameLooper];
                    }
                    else {
                        name=name + " " + permutedPieces[nameLooper];
                    }
                }

            } else if ( serverHeader.indexOf("Microsoft FTP Service")!=-1 ) {
                //Microsoft FTP Service (Version 3.0)
                //Microsoft FTP Service (Version 5.0)

                if ( permutedPieces[0].length()>8 ) { //new format (attributes situated first)
                    nts=9;
                    attributes=permutedPieces[0];
                    //owner=permutedPieces[2];
                    size=permutedPieces[4];
                    month=permutedPieces[5];
                    day=permutedPieces[6];
                    year=permutedPieces[7];

                    for ( int nameLooper=nts-1-1; ++nameLooper<permutedPieces.length; ) {
                        if ( name.equals("") ) {
                            name=permutedPieces[nameLooper];
                        } else {
                            name=name + " " + permutedPieces[nameLooper];
                        }
                    }                        
                } else {  //"Microsoft FTP Service (Version " //old format
                    //nominal total slots=4
                    nts=4;

                    if ( permutedPieces.length>1 ) {
                        month=permutedPieces[0].substring(0,2);
                        day=permutedPieces[0].substring(3,5);
                        year=permutedPieces[0].substring(6,8);
                        hour=permutedPieces[1].substring(0,2);
                        minute=permutedPieces[1].substring(3,5);
                        if ( permutedPieces[1].substring(5,7).equals("PM") ) {
                            hour=String.valueOf(Integer.parseInt(hour)+12);
                        }

                        for ( int nameLooper=nts-1-1; ++nameLooper<permutedPieces.length; ) {
                            if ( name.equals("") ) {
                                name=permutedPieces[nameLooper];
                            } else {
                                name=name + " " + permutedPieces[nameLooper];
                            }
                        }
 
                        if ( permutedPieces[permutedPieces.length-2].equals("<DIR>") ) {
                            attributes="d---------";
                        } else {
                            size=permutedPieces[2];
                            attributes="----------";
                        }
                    } else {
                        name=permutedPieces[0];
                        attributes="----------";
                    }
                }
            } else {
                //Akamai Content Storage FTP Server
                //BSDI Version 7.00LS
                //dcFTPD server (Windows 95)
                //Digital UNIX
                //NcFTPD
                //pftpd 0.41
                //ProFTPD 1.2.6
                //ProFTPD 1.2.8
                //ProFTPD 1.2.9
                //Pure-FTPd 1.0.14
                //SunOS 5.8
                //wu-2.4(1)
                //wu-2.5.0(1)
                //wu-2.6.0(4)
                //wu-2.6.1(1)
                //wu-2.6.1(14)
                //wu-2.6.2(1)
                //wu-2.6.2(2)
                //wu-2.4(10)
                //X2 WS_FTP Server
                

                attributes=permutedPieces[0];
                if ( permutedPieces.length<5 ) {
                    for ( int permutedPiecesLooper=0; ++permutedPiecesLooper<permutedPieces.length; ) {
                        if ( name.equals("") )
                            name=permutedPieces[permutedPiecesLooper];
                        else
                            name=name+" "+permutedPieces[permutedPiecesLooper];                                
                    }
                    month="0";
                    day="0";
                    year="0";
                    //owner="";
                } else {
                    subItems=permutedPieces[1];
                    ts=permutedPieces.length;
                    for ( int permutedPiecesLooper=7; ++permutedPiecesLooper<permutedPieces.length; ) {
                        if ( name.equals("") )
                            name=permutedPieces[permutedPiecesLooper];
                        else
                            name=name+" "+permutedPieces[permutedPiecesLooper];

                        ts-=1;
                    }
                    if ( permutedPieces.length==8 ) {
                        ts=7;
                        name=permutedPieces[ts];
                    } else if ( mainFrameRef.utilitiesClassInstance.isMonth(permutedPieces[4]) ) {
                        //group field missing
                        ts=7;
                        name=permutedPieces[ts]+" "+name;
                    } else {
                        ts=8;
                    }

                    size=permutedPieces[ts-4];
                    month=permutedPieces[ts-3];
                    day=permutedPieces[ts-2];

                    if ( permutedPieces.length==7 )
                        year="";
                    else
                        year=permutedPieces[ts-1];
                    //owner=permutedPieces[2];
                }
            }

            attributes=attributes.trim();
            subItems=subItems.trim();
            ftpUser=ftpUser.trim();
            size=size.trim();
            month=month.trim();
            day=day.trim();
            year=year.trim();
            hour=hour.trim();
            minute=minute.trim();
            name=name.trim();

            if ( attributes.substring(0,1).equals("d") || (attributes.length()>3 && ((attributes.substring(0,4).equals("*LIB") || attributes.substring(0,4).equals("*DIR") || attributes.substring(0,4).equals("*FLR")) || (attributes.length()>4 && attributes.substring(0,5).equals("*DDIR")))) ) {
                resultsTableDataModelBuffer.addRow(new String[] {"./" + name,"","",workingDirectory,size});
            } else if ( attributes.substring(0,1).equals("-") || attributes.substring(0,1).equals("r") || (attributes.length()>3 && (attributes.substring(0,4).equals("*PGM") || (attributes.length()>4 && (attributes.substring(0,5).equals("*STMF") || attributes.substring(0,5).equals("*FILE"))))) ) {   
                if ( regexPattern.compile("\\d*").matcher(size).matches() ) {
                    resultsTableDataModelBuffer.addRow(new String[] {name,mainFrameRef.utilitiesClassInstance.formatSize(Long.parseLong(size), 1),"",workingDirectory,size});
                } else {
                    resultsTableDataModelBuffer.addRow(new String[] {name,"","",workingDirectory,size});
                }
            } else if ( attributes.substring(0,1).equals("l") && permutedPieces.length!=11 ) {
                resultsTableDataModelBuffer.addRow(new String[] {name,"","",workingDirectory,size});
            } else {
                resultsTableDataModelBuffer.addRow(new String[] {name,"","",workingDirectory,size});
            }

            if ( year.indexOf(":")!=-1 ) year=String.valueOf(calendarInstance.get(Calendar.YEAR));
            if ( year.length()==2 ) year=String.valueOf(calendarInstance.get(Calendar.YEAR)).substring(0,2)+year;
            
            if ( month.equals("") || day.equals("") || year.equals("") ) {
                resultsTableDataModelBuffer.setValueAt("", resultsTableDataModelBuffer.getRowCount()-1,2);
            } else {
                calendarInstance.set(Integer.parseInt(year),mainFrameRef.utilitiesClassInstance.textMonthToNumericalMonth(month),Integer.parseInt(day));
                if ( hour!="" && minute!="" )
                    calendarInstance.set(calendarInstance.get(calendarInstance.YEAR),calendarInstance.get(calendarInstance.MONTH),calendarInstance.get(calendarInstance.DATE),Integer.parseInt(hour),Integer.parseInt(minute));
                
                resultsTableDataModelBuffer.setValueAt(calendarInstance.getTime(), resultsTableDataModelBuffer.getRowCount()-1,2);
            }

        } while ( rawDirectoryData.indexOf(splitChars,currentPos)!=-1 );
                
        return resultsTableDataModelBuffer;
    }

    public Object[] createRemoteDirectory(final ceefee.display.sb sbTabInstance, final int sessionID, java.net.Socket controlSocket, final String newDir, final String hostAddress, final String hostPort, final String hostUsername, final String hostPassword, final String dataFormat, final boolean evince) {

        Object[] retData=new Object[2];

        try {
            retData=ensureFtpControlSocketConnection(null, sessionID, controlSocket, hostAddress, hostPort, hostUsername, hostPassword, Integer.parseInt(dataFormat), false);
        } catch ( java.lang.NumberFormatException nfe ) {
            controlSocket=null;
        }
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            if ( sbTabInstance!=null ) sbTabInstance.enableDisable(-1,0);
            return retData;
        }
        controlSocket=(java.net.Socket)retData[0];

        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(null, sessionID, controlSocket, "MKD " + newDir, true, (evince?1:0));
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            if ( sbTabInstance!=null ) sbTabInstance.enableDisable(-1,0);
            return retData;
        }
        
        return new Object[] {""};
    }
    
    public Object[] deleteRemoteDirectory(final ceefee.display.sb sbTabInstance, final int sessionID, final java.net.Socket controlSocket, final String remoteDirectory, final boolean evince ) {
        // Empty Directories Only
            
  //      if folder()
//            if ( sbTabInstance!=null ) sbTabInstance.addTextToStatListing(_sessionID,"( '" + shortFolderName + "' successfully downloaded. )", 1, 2);
        
        Object[] retData=new Object[2];
        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance, sessionID, controlSocket, "RMD "+remoteDirectory, true, (evince?1:0));
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Directory deletion failed!\n(Unable to remove object '"+remoteDirectory+"')", "Uh oh...", javax.swing.JOptionPane.WARNING_MESSAGE);
            sbTabInstance.enableDisable(-1,0);
            return retData;
        }
        return new Object[] {""};
    }
 
    public void startFtpDownload(final ceefee.display.sb sbTabInstance, final int sessionID, final ceefee.display.currentstate currentstateInstance, final java.net.Socket controlSocket, final Object dataSocket, String remoteObject, final String remotePath, final boolean isFolder, final String localFileName, final String _downloadDir, final String fileLen, final String serverHeader, final String hostAddress, final String hostPort, final String hostUsername, final String hostPassword, final String dataFormat, final String connectionMethod, final boolean overrideOverwriteConfirmation, final boolean exec ) {
        int itemsRow;
        ceefee.ftp.downloadfolder downloadFolderInstance=null;
        java.lang.Thread whimsicalThread=null;
        
        if ( sbTabInstance!=null ) {
            mainFrameRef.displayClassInstance.addAccentedCellToResultsDataCellRenderer(sbTabInstance,sbTabInstance.resultsTable.getSelectedRow());
            sbTabInstance.resultsTable.repaint();
        }
        
        if ( (itemsRow=mainFrameRef.utilitiesClassInstance.findItemInTable(remoteObject, mainFrameRef.viewTab.downloadsTable))!=-1 ) {
            // File is in list ; is file active?
            if ( mainFrameRef.viewTab.downloadsTable.getValueAt(itemsRow,1).toString().equals("Annulled.")==false && mainFrameRef.viewTab.downloadsTable.getValueAt(itemsRow,1).toString().equals("(100%)  Done.")==false && mainFrameRef.viewTab.downloadsTable.getValueAt(itemsRow,1).toString().equals("Download Failed!")==false ) {
                javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The selected object is already being downloaded.", "whoops", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        
        if ( isFolder ) {
            if ( remoteObject.length()>1 && remoteObject.substring(0,2).equals("./") )
                remoteObject=remoteObject.substring(1);

            downloadFolderInstance=new ceefee.ftp.downloadfolder(mainFrameRef, sbTabInstance, sessionID, controlSocket, dataSocket, mainFrameRef.downloadDir, remoteObject, remotePath, serverHeader, hostAddress, hostPort, hostUsername, hostPassword, dataFormat, connectionMethod);
            whimsicalThread=new Thread(downloadFolderInstance);
            whimsicalThread.setPriority(Thread.MIN_PRIORITY);
            whimsicalThread.start();
        } else {
            if ( sbTabInstance!=null )
                sbTabInstance.killSockets();
        
            whimsicalThread=new Thread(new ceefee.ftp.download(mainFrameRef, sbTabInstance, sessionID, currentstateInstance, remoteObject, remotePath, localFileName, _downloadDir, Integer.parseInt(fileLen), hostAddress, hostPort, hostUsername, hostPassword, dataFormat, connectionMethod, overrideOverwriteConfirmation, true, exec));
            whimsicalThread.setPriority(Thread.MIN_PRIORITY);
            whimsicalThread.start();
        }
    
    }

    public void startFtpUpload(final ceefee.display.sb sbTabInstance, final int sessionID, final ceefee.display.currentstate currentstateInstance, final java.net.Socket controlSocket, final Object dataSocket, final String absolutePath, final boolean isFolder, final String remotePath, final String remoteFileName, final String hostAddress, final String hostPort, final String hostUsername, final String hostPassword, final String dataFormat, final String connectionMethod, final boolean move, final boolean evince ) {
        final java.io.File electedFile=new java.io.File(absolutePath);
        int itemsRow;
        ceefee.ftp.uploadfolder uploadFolderInstance;
        Thread uploadFolderInstanceThread;


        if ( (itemsRow=mainFrameRef.utilitiesClassInstance.findItemInTable(electedFile.getName(), (javax.swing.JTable)mainFrameRef.viewTab.uploadsTable))!=-1 ) {
            if ( mainFrameRef.viewTab.uploadsTable.getValueAt(itemsRow,1).toString().equals("Annulled.")==false && mainFrameRef.viewTab.uploadsTable.getValueAt(itemsRow,1).toString().equals("(100%)  Done.")==false) {
                javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The selected object is already being uploaded.\n ( Click on the [VIEW] tab for more information. )", "whoops", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        if ( isFolder ) {
            uploadFolderInstance=new ceefee.ftp.uploadfolder(mainFrameRef, sbTabInstance, sessionID, controlSocket, dataSocket, absolutePath, remotePath, hostAddress, hostPort, hostUsername, hostPassword, dataFormat, connectionMethod, move);
            uploadFolderInstanceThread=new Thread(uploadFolderInstance);
            uploadFolderInstanceThread.setPriority(Thread.MIN_PRIORITY);
            uploadFolderInstanceThread.start();
        } else {
            Thread threadFtpUpload=new Thread(new ceefee.ftp.upload(mainFrameRef,sbTabInstance,sessionID,currentstateInstance,absolutePath,remotePath,remoteFileName,hostAddress,hostPort,hostUsername,hostPassword,dataFormat,connectionMethod,move,false,true,evince));
            threadFtpUpload.setPriority(Thread.MIN_PRIORITY);
            threadFtpUpload.start();
        }
        
    }
        
    public void deleteRemoteObject( final ceefee.display.sb sbTabInstance, final int sessionID, java.net.Socket controlSocket, final java.net.Socket dataSocket, String moribundObject, final String objectPath, final String serverHeader, final String hostAddress, final String hostPort, final String hostUsername, final String hostPassword, final int dataFormat, final int connectionMethod, final String currentWD, final boolean evince ) {
               
        Object[] retData=new Object[2];
        ceefee.ftp.removefolder removeRemoteFolderInstance;
        Thread removeRemoteFolderInstanceThread;
        int folder0link1file2=0;
        String formattedMoribundObject="";
        
        
        controlSocket=(java.net.Socket)ensureFtpControlSocketConnection(sbTabInstance, sessionID, controlSocket, hostAddress, hostPort, hostUsername, hostPassword, dataFormat, false)[0];
        if ( controlSocket==null || controlSocket.isConnected()==false ) {
            if ( sbTabInstance!=null ) sbTabInstance.enableDisable(-1,0);
            return;
        }
        
        if ( mainFrameRef.utilitiesClassInstance.isLinkFormatting(moribundObject) ) {
            // Process Link
            folder0link1file2=1;
            formattedMoribundObject=mainFrameRef.utilitiesClassInstance.removeFtpObjectLinkFormatting(moribundObject,true);
        }
        if ( mainFrameRef.utilitiesClassInstance.isFolderFormatting(moribundObject) ) {
            // Process Folder
            folder0link1file2=0;
            formattedMoribundObject=moribundObject.substring(2);
            if ( formattedMoribundObject.substring(formattedMoribundObject.length()-1,formattedMoribundObject.length()).equals("/")==false )
                formattedMoribundObject=formattedMoribundObject+"/";
        }
        if ( folder0link1file2==0 || folder0link1file2==1 ) {
            removeRemoteFolderInstance=new ceefee.ftp.removefolder(mainFrameRef,sbTabInstance,sessionID,controlSocket,dataSocket,currentWD,currentWD+formattedMoribundObject,serverHeader,hostAddress,hostPort,hostUsername,hostPassword,connectionMethod);
            removeRemoteFolderInstanceThread=new Thread(removeRemoteFolderInstance);
            removeRemoteFolderInstanceThread.setPriority(Thread.MIN_PRIORITY);
            removeRemoteFolderInstanceThread.start();
            if ( removeRemoteFolderInstance.retData[0]!=null && removeRemoteFolderInstance.retData[0].toString().length()>1 && removeRemoteFolderInstance.retData[0].toString().substring(0,2).equals("-1") ) {
                if ( folder0link1file2==0 )
                    return;
                else
                    moribundObject=mainFrameRef.utilitiesClassInstance.removeFtpObjectLinkFormatting(moribundObject,false);
            }
        }
        
        // Process File or file-link
        if ( sbTabInstance!=null ) sbTabInstance.enableDisable(-1,1);

        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance, sessionID, controlSocket, "DELE "+moribundObject, true, 2);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            if ( sbTabInstance!=null ) sbTabInstance.enableDisable(-1,0);
            return;
        }

        if ( sbTabInstance!=null ) displaydir(sbTabInstance,sessionID,controlSocket,dataSocket,false,false,false);

    }

    Object[] performPasvOperation(final ceefee.display.sb sbTabInstance, final int sessionID, java.net.Socket controlSocket, Object dataSocket, final String hostAddress, final String hostPort, final String hostUsername, final String hostPassword, final String toSend, final String toSay, final boolean evince ) {
        Object[] retData=new Object[2];

        retData=ensureFtpControlSocketConnection(sbTabInstance,sessionID,controlSocket,hostAddress,hostPort,hostUsername,hostPassword,0,false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            return retData;
        }
        if ( sbTabInstance!=null ) {
            controlSocket=sbTabInstance.controlSocket=(java.net.Socket)retData[0];

            retData=connectFtpDataSocket(sbTabInstance,sessionID,sbTabInstance.controlSocket,0,evince);
            sbTabInstance.dataSocket=dataSocket=retData[0];
            
        } else {
            controlSocket=(java.net.Socket)retData[0];
            
            retData=connectFtpDataSocket(sbTabInstance,sessionID,controlSocket,0,evince);
            dataSocket=retData[0];

        }
        
        if ( sbTabInstance!=null && sbTabInstance.sessionID!=sessionID ) {
            return new Object[] {"-1"};
        }
        if ( dataSocket==null || (dataSocket.getClass().getName().equals("java.net.ServerSocket") && ((java.net.ServerSocket)dataSocket).isClosed()) ) {
            return new Object[] {"-1"};
        }
        
        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance, sessionID, controlSocket, toSend, true, (evince?1:0));
        if ( evince && toSay.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,toSay,1,1,false);
        if ( (retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1")) ) {
            if ( retData[0].toString().indexOf("Permission denied.")==-1 ) {
                try {
                    controlSocket.close();
                    if (dataSocket!=null) {
                        if (dataSocket.getClass().getName().equals("java.net.ServerSocket"))
                            ((java.net.ServerSocket)dataSocket).close();
                        else if (dataSocket.getClass().getName().equals("java.net.Socket"))
                            ((java.net.Socket)dataSocket).close();
                    }
                } catch ( java.io.IOException ioe ) {}
            }
            //sbTabInstance.addTextToStatListing("( PASV LIST command failed! )",1);
            sbTabInstance.addTextToStatListing(sessionID,retData[0].toString(),1,1,false);
            return retData;
        } else if ( sbTabInstance!=null && sbTabInstance.sessionID!=sessionID ) {
            return new Object[] {"-1"};
        }

        return new Object[] {retData[0],controlSocket,dataSocket};
    }

    Object[] performPortOperation(final ceefee.display.sb sbTabInstance, final int sessionID, java.net.Socket controlSocket, Object dataSocket, final String hostAddress, final String hostPort, final String hostUsername, final String hostPassword, final String toSend, final String toSay, final boolean evince ) {
        Object[] retData=new Object[2];
        ceefee.sockets.accept acceptSocketClassInstance=null;

        
        retData=ensureFtpControlSocketConnection(sbTabInstance,sessionID,controlSocket,hostAddress,hostPort,hostUsername,hostPassword,1,false);
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") )
            return retData;

        if ( sbTabInstance!=null ) {
            controlSocket=sbTabInstance.controlSocket=(java.net.Socket)retData[0];
            sbTabInstance.dataSocket=connectFtpDataSocket(sbTabInstance,sessionID,(java.net.Socket)sbTabInstance.controlSocket, 1, evince)[0];
            if ( sbTabInstance.dataSocket==null || (sbTabInstance!=null && (sessionID!=sbTabInstance.sessionID)) )
                return new Object[] {"-1"};
            dataSocket=sbTabInstance.dataSocket;
            
        } else {
            controlSocket=(java.net.Socket)retData[0];
            dataSocket=connectFtpDataSocket(sbTabInstance,sessionID,(java.net.Socket)controlSocket, 1, evince)[0];
            
        }
        if ( dataSocket.toString().length()>1 && dataSocket.toString().substring(0,2).equals("-1") )
            return new Object[] {"-1"};
        
        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance, sessionID, controlSocket,toSend, false, (evince?1:0));
        if ( evince && toSay.equals("")==false ) sbTabInstance.addTextToStatListing(sessionID,toSay,1,1,false);

        acceptSocketClassInstance=new ceefee.sockets.accept(mainFrameRef, (java.net.ServerSocket)dataSocket);

        retData=mainFrameRef.ftpClassInstance.ftpGetResponse(sbTabInstance, sessionID, controlSocket, mainFrameRef.connectionTimeout, (evince?1:0));
        if ( sbTabInstance!=null && sessionID!=sbTabInstance.sessionID ) {
            return retData;
        } else if ( (retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1")) ) {
            if ( retData[0].toString().indexOf("Permission denied.")==-1 ) {
                try {
                    (controlSocket).close();
                    ((java.net.ServerSocket)dataSocket).close();
                } catch ( java.io.IOException ioe ) {}
            }
            //sbTabInstance.addTextToStatListing("( PORT LIST command failed! )",1);
            return retData;
        }
    
        if ( sbTabInstance!=null ) {
            sbTabInstance.dataSocket=acceptSocketClassInstance.accept()[0];
        } else {
            dataSocket=acceptSocketClassInstance.accept()[0];
        }
        
        if ( dataSocket==null || (sbTabInstance!=null && (sessionID!=sbTabInstance.sessionID)) ) {
            if ( evince )
                sbTabInstance.addTextToStatListing(sessionID,"( " + "Data socket hand-shake failed." + " )", 1,2,false);
            return new Object[] {"-1"};
        }
        
        return new Object[] {retData[0],controlSocket,dataSocket};
    }
    
    public Object[] ftpSendCommand(final ceefee.display.sb sbTabInstance, final int sessionID, final java.net.Socket controlSocket, final String tCommand, final boolean getResponse, final int linesEvinced ) { 
        
        if ( linesEvinced>0 ) {
            if ( sbTabInstance!=null ) sbTabInstance.addTextToStatListing(sessionID,tCommand,0,1,false);
        }
        try {
            controlSocket.getOutputStream().write( (tCommand+mainFrameRef.lineSeparator).getBytes() );
        } catch ( java.io.IOException ioe ) {
            if ( linesEvinced>0 ) {
                if ( sbTabInstance!=null ) sbTabInstance.addTextToStatListing(sessionID,"( " + ioe.getMessage() + " )",1,1,false);
            }
            return new Object[] {"-1"+ioe.getMessage()};
        }

        if ( getResponse ) {
            return mainFrameRef.ftpClassInstance.ftpGetResponse(sbTabInstance,sessionID,(Object)controlSocket,mainFrameRef.connectionTimeout,linesEvinced);
        } else {
            if ( linesEvinced>0 ) {
                if ( sbTabInstance!=null )
                    sbTabInstance.addTextToStatListing(sessionID,"",0, linesEvinced-1,false);
            }
            return new Object[] {""};
        }
        
    }
    
    public String changeWorkingDirectory(final ceefee.display.sb sbTabInstance, final int sessionID, final java.net.Socket controlSocket, String newWD, final boolean correct, final boolean evince) {
        Object[] retData=new Object[2];
        int whimsicalByteLocation;


        if ( sbTabInstance!=null ) {
            if ( sbTabInstance.activityListLock ) return "-1";
        }
            
        if ( newWD.equals(".") || newWD.equals("") )
            newWD="/";
        else if ( newWD.length()>2 ) {
            if ( (whimsicalByteLocation=newWD.indexOf("../"))!=-1 ) {
                if ( whimsicalByteLocation==0 ) {
                    newWD=newWD.substring(2);
                } else {
                    if ( (whimsicalByteLocation=newWD.lastIndexOf("/",whimsicalByteLocation-2))!=-1 ) {
                        
                        if ( newWD.length()>(newWD.indexOf("../")+2) ) {
                            newWD=newWD.substring(0,whimsicalByteLocation+1)+newWD.substring(newWD.indexOf("../")+3);
                        } else {
                            newWD=newWD.substring(0,whimsicalByteLocation+1);
                        }
                    } else {
                        if ( newWD.length()>(newWD.indexOf("../")+2) ) {
                            newWD="/"+newWD.substring(newWD.indexOf("../")+3);
                        } else
                            newWD="/";
                    }
                }
            }

            newWD=newWD.replaceAll("\\/\\.\\/","/");
            
            newWD=newWD.replaceAll("\\.\\/","/");

            if ( newWD.length()>2 ) {
                if ( newWD.substring(newWD.length()-3, newWD.length()).equals("/..") )
                    newWD=mainFrameRef.utilitiesClassInstance.getMotherPathFromPath(newWD.substring(0,newWD.length()-3),"/");

                if ( newWD.substring(newWD.length()-1,newWD.length()).equals("/") )
                    newWD=newWD.substring(0,newWD.length()-1);
            }
        }
        
        retData=mainFrameRef.ftpClassInstance.ftpSendCommand(sbTabInstance,sessionID,controlSocket,"CWD "+newWD, true, (evince?1:0));
        if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
            if ( correct )
                return changeWorkingDirectory(sbTabInstance, sessionID, controlSocket, "/", false, evince);
            else
                return retData[0].toString();
        }
        
        if ( sbTabInstance!=null ) {       
            if ( newWD.equals("/")==false )
                sbTabInstance.currentWD=(newWD=newWD+"/");
            else
                sbTabInstance.currentWD=newWD;
            if ( sbTabInstance.currentWD.substring(sbTabInstance.currentWD.length()-1).equals("/")==false )
                sbTabInstance.currentWD=sbTabInstance.currentWD + "/";

            if ( retData[0].toString().indexOf("is current cwd")!=-1 && retData[0].toString().indexOf("CWD command successful")!=-1 )
                retData=mainFrameRef.ftpClassInstance.ftpGetResponse(sbTabInstance, sessionID, controlSocket, mainFrameRef.connectionTimeout, (evince?1:0));

            sbTabInstance.activityListLock=true;
            retData[0]=String.valueOf(mainFrameRef.utilitiesClassInstance.findItemInList(sbTabInstance.currentWD,sbTabInstance.activityList));
            if ( (retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1")==false) ) {
                try {
                    sbTabInstance.activityList.removeItem(sbTabInstance.activityList.getItemAt(Integer.parseInt(retData[0].toString())));
                } catch ( java.lang.NumberFormatException nfe ) {}
            }
            sbTabInstance.activityList.addItem(sbTabInstance.currentWD);        
            sbTabInstance.activityList.setSelectedItem(sbTabInstance.currentWD);

            if ( sbTabInstance.activityList.getItemCount()>(mainFrameRef.historyTrailSize + 2) ) sbTabInstance.activityList.removeItemAt(2);

            sbTabInstance.activityListLock=false;

            if ( sbTabInstance.scourMethod==mainFrameRef.BROWSE_METHOD ) // store browse trail everytime directory is changed
                mainFrameRef.storeHistoryTrail(sbTabInstance,sbTabInstance.activityList,String.valueOf(sbTabInstance.lastValidServerIndex));
        
        }
    
        return newWD;
    }
    
}