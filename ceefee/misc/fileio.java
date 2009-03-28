
package ceefee.misc;


import java.io.InputStream;
import java.io.OutputStream;

import java.io.RandomAccessFile;
import java.io.File;

import java.io.FileNotFoundException;



public class fileio {
    
    private ceefee.main mainFrameRef;
        
    final int MAX_FILE_SIZE_SUGGESTED                             =32767;
     
    
    /** Creates a new instance of class_fileData */
    public fileio(final ceefee.main _mainFrameRef) {
        mainFrameRef=_mainFrameRef;
    }
          
    public synchronized int storeDataToFile( final String filePath, final byte[] data, final long offSet, final int chunkLength, final boolean _problemCorrected ) {
        try {
            java.io.RandomAccessFile raf=new java.io.RandomAccessFile(filePath, "rw");
            
            if ( offSet==0 )
                raf.setLength(0);
            else
                raf.seek(offSet);
                
            raf.write(data, 0, chunkLength);
            
            raf.close();
            
            return 0;
        } catch ( java.io.IOException ioe ) {
            Object[] retData=new Object[2];
            
            if ( ioe.getMessage().indexOf("(Access is denied)")!=-1 && _problemCorrected==false ) {
                retData[0]=mainFrameRef.utilitiesClassInstance.setFileAttributes(filePath);
                if ( retData[0].toString().equals("1") ) {
                    return storeDataToFile(filePath, data, offSet, chunkLength, true);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "storeDataToFile(0:1) returned:\n "+retData[0].toString(),"Uh oh...",javax.swing.JOptionPane.ERROR_MESSAGE);
                    logError(getClass().getName()+"(0x01)\t"+retData[0].toString());
                    System.exit(-1);
                    return -1;
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "storeDataToFile(0:2) returned:\n "+ioe.getMessage(),"Uh oh...",javax.swing.JOptionPane.ERROR_MESSAGE);
                logError(getClass().getName()+"(0x02)\t"+ioe.getMessage());
                System.exit(-1);
                return -1;
            }            
        }
    }
       
    public void storeDataToFile( final String filePath, final String dataClass, final String dataSpecification, final String dataValue, final int itemNumber, final int totalItems, final String groupNumber, final boolean _problemCorrected ) {
        // only use with light-weight files: files 32767 bytes (31.9 KB) or below
        // bigger file size = slower access !

        Object[] retData=new Object[2];
        
        byte[] oldData;
        String oldDataString="";
        String newDataString="";
              
        String buffer="";
                
        int whimsicalByteLocation1;
        int whimsicalByteLocation2;
        int whimsicalByteLocation3;
                
        String dataFields="";
        
        try {
            java.io.RandomAccessFile raf=new java.io.RandomAccessFile(filePath,"rw");
            
            final long fileSize=raf.length();
            
            
            if ( fileSize>MAX_FILE_SIZE_SUGGESTED ) {
                javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"error storeDataToFile(3):\n","Uh oh...",javax.swing.JOptionPane.ERROR_MESSAGE);
                logError(getClass().getName()+"(0x03)\t"+"Maximum allowed file size exceeded.");
                System.exit(-1);
            }

            oldData=new byte[(int)fileSize];
            raf.read(oldData);
            oldDataString=new String(oldData);

            buffer=mainFrameRef.lineSeparator + dataClass + groupNumber + dataSpecification + "=";
            if ( (whimsicalByteLocation1=oldDataString.indexOf(buffer))!=-1 ) {
                whimsicalByteLocation1+=buffer.length();
                whimsicalByteLocation2=oldDataString.indexOf(mainFrameRef.lineSeparator,whimsicalByteLocation1);
                
                if ( groupNumber.equals("") ) {                                   
                    newDataString=oldDataString.substring(0,whimsicalByteLocation1) + dataValue + oldDataString.substring(whimsicalByteLocation2);
                } else {
                    whimsicalByteLocation3=whimsicalByteLocation1;
                            
                    for (int charLooper=1;charLooper<itemNumber;charLooper++) {
                        whimsicalByteLocation3=oldDataString.indexOf("|",whimsicalByteLocation3)+1;
                    }                    

                    if ( whimsicalByteLocation3!=0 ) {
                        if ( oldDataString.indexOf("|",whimsicalByteLocation3)!=-1 && oldDataString.indexOf("|",whimsicalByteLocation3)<oldDataString.indexOf(mainFrameRef.lineSeparator,whimsicalByteLocation3) ) {
                            newDataString=oldDataString.substring(0,whimsicalByteLocation3) + dataValue + oldDataString.substring(oldDataString.indexOf("|",whimsicalByteLocation3));
                        } else {
                            newDataString=oldDataString.substring(0,whimsicalByteLocation3) + dataValue + oldDataString.substring(whimsicalByteLocation2);;
                        }
                    } else {
                        newDataString=newDataString.substring(0,whimsicalByteLocation2) + dataValue;
                    }
                }
            } else { //data not in file
                if ( itemNumber==1 && totalItems>1 ) {
                    for (int charLooper=0;++charLooper<totalItems ; ) {
                        dataFields = dataFields + Character.toString((char)124);
                    }
                }
    
                newDataString=mainFrameRef.lineSeparator + dataClass + groupNumber + dataSpecification + "=" + dataValue + dataFields;

                if ( groupNumber.equals("") ) {
                    if ( oldDataString.equals("")==false ) {
                        newDataString=oldDataString.substring(0,oldDataString.length()-2)+newDataString+oldDataString.substring(oldDataString.length()-2,oldDataString.length());
                    } else {
                        newDataString=System.getProperty("line.separator").getBytes()+newDataString;
                    }
                } else {
                    if ( fileSize!=0 ) {
                        if ( dataSpecification.equals("") ) {
                            whimsicalByteLocation1=oldDataString.indexOf(mainFrameRef.lineSeparator + dataClass + (Integer.parseInt(groupNumber)-1) + dataSpecification + "=");
                            if ( whimsicalByteLocation1==-1 ) {
                                whimsicalByteLocation1=oldDataString.indexOf(mainFrameRef.lineSeparator + dataClass + (Integer.parseInt(groupNumber)-1) + "=");
                            }
                        } else {
                            whimsicalByteLocation1=oldDataString.indexOf(mainFrameRef.lineSeparator + dataClass + groupNumber + "=");
                        }

                        if ( whimsicalByteLocation1!=-1 ) {
                            int a0=oldDataString.indexOf(mainFrameRef.lineSeparator,whimsicalByteLocation1+1);
                            newDataString=oldDataString.substring(0,a0) + newDataString + oldDataString.substring(a0);
                        } else {
                            newDataString=oldDataString.substring(0,oldDataString.length()-2)+newDataString+System.getProperty("line.separator");
                        }
                    }
                }
            }

            raf.setLength(0);
            
            //write new data to file
            if ( fileSize==0 )
                raf.write(String.valueOf(newDataString+System.getProperty("line.separator")).getBytes());
            else
                raf.write(newDataString.getBytes());
            
            raf.close();
            
        } catch ( java.io.IOException ioe ) {
            if ( ioe.getMessage().equals(filePath + " (Access is denied)") && _problemCorrected==false ) {
                retData[0]=mainFrameRef.utilitiesClassInstance.setFileAttributes(filePath);
                if ( retData[0]==null || retData[0].toString().length()>1 && retData[0].toString().substring(0,2).equals("-1") ) {
                    javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"error storeDataToFile(1:1):\n"+retData[0].toString().substring(2),"Uh oh...",javax.swing.JOptionPane.ERROR_MESSAGE);
                } else //file was probably read-only
                    storeDataToFile(filePath, dataClass, dataSpecification, dataValue, itemNumber, totalItems, groupNumber, true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"error storeDataToFile(1:2):\n"+ioe.getMessage(),"Uh oh...",javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            logError(getClass().getName()+"(0x04)\t"+ioe.getMessage());
            System.exit(-1);
        }
    }

    public void removeDataFromFile( final String filePath, final String dataTitle, final String groupNumber, final boolean reassignIndexes ) {
        
        long fileSize;
        
        byte[] oldData;
        String oldDataString="";
        String newDataString="";

        String dataRegister1;
        
        int intDataPoint1;
        
        try {    
            java.io.RandomAccessFile raf=new java.io.RandomAccessFile(filePath,"rw");
           
            
            fileSize=raf.length();
            
            if ( fileSize>MAX_FILE_SIZE_SUGGESTED )
                throw (new Throwable("File size exceeds maximum limit."));

            oldData=new byte[(int)fileSize];
            raf.read(oldData);
            oldDataString=new String(oldData);
                
            intDataPoint1=oldDataString.indexOf(mainFrameRef.lineSeparator + dataTitle + groupNumber + "=");
            if ( intDataPoint1!=-1 ) {
                newDataString=oldDataString.substring(0,intDataPoint1) + oldDataString.substring(oldDataString.indexOf(mainFrameRef.lineSeparator,intDataPoint1+1));
                
                if ( newDataString.equals(mainFrameRef.lineSeparator) )
                    newDataString="";
                
                if ( groupNumber.equals("")==false && reassignIndexes ) {
                    //reassign other groupNumbers
                    for ( int iterator=Integer.parseInt(groupNumber); ++iterator>0; ) {
                        dataRegister1=mainFrameRef.lineSeparator + dataTitle + iterator + "=";
                        if ( newDataString.indexOf(dataRegister1)!=-1 ) {   
                            newDataString=newDataString.replaceAll(dataRegister1, (mainFrameRef.lineSeparator + dataTitle + (iterator-1) + "="));
                        } else
                            break;
                    }
                }

                raf.setLength(0);
                
                raf.write(newDataString.getBytes());
                
                raf.close();
                
                return ;
            }
        } catch ( java.io.IOException ioe ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"error removeDataFromFile(5): \n\t" + ioe.getMessage() + " '" + filePath + "'.","Uh oh...",javax.swing.JOptionPane.ERROR_MESSAGE);
            logError(getClass().getName()+"(0x05)\t"+ioe.getMessage());
            System.exit(-1);
        } catch ( Throwable t ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"error removeDataFromFile(6): \n\t" + t.getMessage(),"Uh oh...",javax.swing.JOptionPane.ERROR_MESSAGE);
            logError(getClass().getName()+"(0x06)\t"+t.getMessage());
            System.exit(-1);
        }
    }
    
    public synchronized void logError( final String errMsg ) {
        
        String currentLine="";
        String newFileData="";
        byte[] b=null;
        int whimsicalByteLocation=-1;
        
        
        try {
            java.io.RandomAccessFile raf=new java.io.RandomAccessFile("_error.log","rw");
            
            while ( ((currentLine=raf.readLine())!=null) && ((newFileData.length()+currentLine.length())<MAX_FILE_SIZE_SUGGESTED) ) {
                newFileData=newFileData+currentLine+System.getProperty("line.separator");
            }

            newFileData=newFileData+(java.util.Calendar.getInstance().getTime())+"\t"+errMsg+System.getProperty("line.separator");

            try {
                raf.setLength(0);
                raf.seek(0);
                raf.write(newFileData.getBytes());
                raf.close();
            } catch ( java.io.IOException ioe ) {
                javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "logError(0): "+ioe.getMessage(),"Uh oh...",javax.swing.JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        } catch ( java.io.FileNotFoundException fnfe ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "logError(1): " + fnfe.getMessage(),"Uh oh...",javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch ( java.io.IOException ioe ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "logError(2): " + ioe.getMessage(),"Uh oh...",javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    public byte[] getDataFromFile( final String filePath, final int offset, final int chunkSize ) {

        long fileLen=0;
        byte[] fileData=null;
        

        try {
            java.io.RandomAccessFile raf=new java.io.RandomAccessFile(filePath,"r");
            
            fileLen=raf.length();

            if ( offset>fileLen ) {
                //done
            } else {
                if ( offset+chunkSize>fileLen ) {
                    //last chunk
                    raf.seek(offset);                   
                    fileData=new byte[((int)fileLen-offset)];
                    raf.read(fileData,0,((int)fileLen-offset));
                } else {
                    raf.seek(offset);
                    fileData=new byte[chunkSize];
                    raf.read(fileData,0,chunkSize);
                }
            }

            raf.close();
            
            return fileData;
            
        } catch ( java.io.FileNotFoundException fnfe ) {
            System.err.println("error getDataFromFile(3): " + fnfe.getMessage());
            return "".getBytes();
        } catch ( java.io.IOException ioe ) {
            System.err.println("error getDataFromFile(4): " + ioe.getMessage());
            return "".getBytes();
        }

    }
    
    public Object[] getDataFromFile( final String filePath, final String infoTitle, final boolean _problemCorrected ) {

        String currentLine;


        try {
            java.io.RandomAccessFile raf=new java.io.RandomAccessFile(filePath,"r");

            try {
                while ( (currentLine=raf.readLine())!=null ) {
                    if ( infoTitle.length()<=currentLine.length() ) {
                        if ( currentLine.substring(0,infoTitle.length()).equals(infoTitle) ) {
                            raf.close();
                            return new Object[] {currentLine.substring((infoTitle.length()+1),currentLine.length())};
                        }
                    }
                }
                raf.close();
                return new Object[] {""};
            } catch ( java.io.IOException ioe ) {
                System.err.println("error getDataFromFile(0): " + ioe.getMessage() );
                return new Object[] {"-1"+ioe.getMessage()};
                
            }
        } catch ( java.io.FileNotFoundException fnfe ) {
            return new Object[] {"-1"+fnfe.getMessage()};
        }
                
    }
        
}