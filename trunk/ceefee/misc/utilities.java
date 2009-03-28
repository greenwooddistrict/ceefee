
package ceefee.misc;


import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JTable;

import java.net.InetAddress;

import java.util.StringTokenizer;


public class utilities {

    private ceefee.main mainFrameRef;


    private java.util.StringTokenizer fileTypeExtensions;

    
    public utilities(final ceefee.main _mainFrameRef) {
        mainFrameRef=_mainFrameRef;        
    }
    
    public int abs ( double floatingPointValue ) {
        String buffer=String.valueOf(floatingPointValue);
        if ( buffer.indexOf("E-")!=-1 ) // <1
            return 0;
        else { // >1
            buffer=buffer.substring(0,buffer.indexOf("."));
            return Integer.parseInt(buffer);
        }
    }
    
    public byte l1ittleEndianByteToBigEndianByte( byte littleEndianByte ) {
        if ( littleEndianByte==0 )
            return (byte)littleEndianByte;
        else {
            return (byte)(~((byte)littleEndianByte));
        }
    }
        
    public int findItemInList ( final String item, final javax.swing.JComboBox list ) {
       
        for ( int listLooper=-1; ++listLooper<list.getItemCount(); ) {
          if ( list.getItemAt(listLooper).toString().toLowerCase().equals(item.toLowerCase()) ) {
              //list.setSelectedIndex(listLooper);
              //return list.getItemAt(listLooper).toString();
              return listLooper;
          }
        }
        return -1;
    }
    
    public int findItemInTable( final String item, final javax.swing.JTable table ) {
       
        for ( int tableRowLooper=-1; ++tableRowLooper<table.getRowCount() ; ) {
          if ( table.getValueAt(tableRowLooper, 0).toString().equals(item) ) {
              return tableRowLooper;
          }
        }
        return -1;
        
    }

    public static Object[] getClipboardData() {
        Transferable t;
        try {
            t=java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

            if ( t!=null && t.isDataFlavorSupported(DataFlavor.stringFlavor) )
                return new Object[] {t.getTransferData(DataFlavor.stringFlavor)};
            else
                return new Object[] {""};
        } catch ( java.awt.datatransfer.UnsupportedFlavorException ufe ) {
            return new Object[] {"-1"+ufe.getMessage()};
        } catch ( java.io.IOException ioe ) {
            return new Object[] {"-1"+ioe.getMessage()};
        } catch ( java.lang.IllegalStateException ise ) {
            return new Object[] {"-1"+ise.getMessage()};
        }
    }
    public static String setClipboardData( final String newData ) {
        return setClipboardData(new StringSelection(newData));
    }
    public static String setClipboardData( final Transferable newData ) {
        try {
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(newData,null);
            return "";
        } catch ( java.lang.IllegalStateException ise ) {
            return "-1";
        }
    }
    
    public static String removeFtpObjectLinkFormatting( final String objectLink, final boolean returnNewDestination ) {
        if ( returnNewDestination )
            return objectLink.substring(objectLink.indexOf(" -> ")+4).trim();
        else
            return objectLink.substring(0,objectLink.indexOf(" -> ")).trim();
        
    }
        
    public static boolean isLinkFormatting( final String path ) {
        if ( path==null ) return false;
        if ( path.indexOf(" -> ")!=-1 )// {
            return true;
        else 
            return false;
    }
        
    public boolean isFolderFormatting( final String path ) {
        if ( path==null ) return false;

        if ( path.length()>1 && path.substring(0,2).equals("./") )
            return true;
        else
            return false;
    }
    
    public String setFileAttributes( final String filePath ) {
        // setFileAttributes to Normal
        
        try {
            Process chmodProcess=Runtime.getRuntime().exec(new String[] {"_cfe.exe", "SetFileAttributes", filePath});
            try {
                return String.valueOf(chmodProcess.waitFor());
            } catch ( java.lang.InterruptedException ie ) {
                return "-1"+ie.getMessage();
            }
        } catch ( java.io.IOException ioe ) {
            return "-1"+ioe.getMessage();
        }
    }
    
    public String system( final String object ) {
        String errMsg="";
        Object[] retData=new Object[2];
        
        
        try {
            Process osf=Runtime.getRuntime().exec(new String[] {"_cfe.exe", "system", object});
        
            try {
                if ( ((String)(retData[0]=String.valueOf(osf.waitFor()))).length()>1 && retData[0].toString().substring(0,2).equals("-1") )
                    javax.swing.JOptionPane.showMessageDialog(mainFrameRef, errMsg,"whoops",javax.swing.JOptionPane.WARNING_MESSAGE);

            } catch ( java.lang.InterruptedException ie ) {
                errMsg=ie.getMessage();
                return errMsg;
            }
        } catch ( java.io.IOException ioe ) {
            errMsg=ioe.getMessage();
            return errMsg;
        }
        
        return retData[0].toString();
    }
    
    public String getObjectFromPath( String path, final String separator ) {

        if ( path.substring(path.length()-separator.length()).equals(separator) )
            return "";
        
        return path.substring(path.lastIndexOf(separator)+separator.length());
    }
    public String getRootPathFromPath( String path, final String separator ) {
        return path.substring(0,path.indexOf(separator)+1);
    }
    public String getMotherPathFromPath( String currentFolder, final String separator ) {
        
        if ( currentFolder.equals("") || currentFolder.equals(separator) ) {
            return separator;
        } else if ( currentFolder.lastIndexOf(separator)==-1 ) {
            return separator;
        }
         
        if ( currentFolder.substring(currentFolder.length()-separator.length()).equals(separator) )
            currentFolder=currentFolder.substring(0,currentFolder.length()-separator.length());
        
        return currentFolder.substring(0,currentFolder.lastIndexOf(separator)+1);

    }
    
    public Object getToken( final String string, final String separator, final int tokenIndex ) {
        int currentSeparator=-1;
        for ( int separatorLooper=-1; ++separatorLooper<tokenIndex; ) {
            currentSeparator=string.indexOf(separator,currentSeparator+1);

            if ( currentSeparator==-1 ) {
                if ( tokenIndex==(separatorLooper+1) ) {
                    return string.substring(string.lastIndexOf(separator)+1);
                } else {
                    return "-1";
                }
            }
        }
        if ( string.indexOf(separator,currentSeparator+1)==-1 )
            return string.substring(currentSeparator+1);
        else
            return string.substring(currentSeparator+1,string.indexOf(separator,currentSeparator+1));
    }
    
    public String getHighestConnectionIP() {
        InetAddress[] localAddresses;
                
        
        try {
            localAddresses=InetAddress.getLocalHost().getAllByName(InetAddress.getLocalHost().getHostName());
            return localAddresses[localAddresses.length-1].getHostAddress();
        } catch ( java.net.UnknownHostException uhe ) {
            return "-1" + uhe.getMessage();
        }
   
    }
    
    public String formatSize( final double byteSize, final int returnSizePrecision) {
        if ( returnSizePrecision==0 )
            return String.valueOf(Math.round((byteSize*Math.pow(10,returnSizePrecision)) / 1024))+" KB";
        else
            return String.valueOf(Math.round((byteSize*Math.pow(10,returnSizePrecision)) / 1024)/Math.pow(10,returnSizePrecision))+" KB";
    }

    public boolean isInternalIP( final String ip ) {
        InetAddress[] localAddresses;
   
        
        try {
            localAddresses=InetAddress.getLocalHost().getAllByName(InetAddress.getLocalHost().getHostName());
            for ( int localAddressesLooper=-1; ++localAddressesLooper<localAddresses.length; ) {
                if ( ip.equals(localAddresses[localAddressesLooper]) ) {
                    return true;
                }
            }
        } catch ( java.net.UnknownHostException uhe ) {}
        return false;
    }
    
    public boolean isMonth( String stringValue ) {
        stringValue=stringValue.toLowerCase();
        
        if ( stringValue.equals("jan") || stringValue.equals("january") ) {
            return true;            
        }
        else if ( stringValue.equals("feb") || stringValue.equals("february") ) {
            return true;            
        }
        else if ( stringValue.equals("mar") || stringValue.equals("march") ) {
            return true;        
        }
        else if ( stringValue.equals("apr") || stringValue.equals("april") ) {
            return true;        
        }
        else if ( stringValue.equals("may") || stringValue.equals("may") ) {
            return true;        
        }
        else if ( stringValue.equals("jun") || stringValue.equals("june") ) {
            return true;        
        }
        else if ( stringValue.equals("jul") || stringValue.equals("july") ) {
            return true;        
        }
        else if ( stringValue.equals("aug") || stringValue.equals("august") ) {
            return true;        
        }
        else if ( stringValue.equals("sep") || stringValue.equals("september") ) {
            return true;
        }
        else if ( stringValue.equals("oct") || stringValue.equals("october") ) {
            return true;
        }
        else if ( stringValue.equals("nov") || stringValue.equals("november") ) {
            return true;
        }
        else if ( stringValue.equals("dec") || stringValue.equals("december") ) {
            return true;
        }
        else {
            return false;
        }
        
    }
    
    public int textMonthToNumericalMonth( final String _month ) {
           
        if ( _month.length()<3 )
            return 0;
        
        if ( _month.substring(0,3).toLowerCase().equals("jan") ) {
            return 0;
        }
        else if ( _month.substring(0,3).toLowerCase().equals("feb") ) {
            return 1;
        }
        else if ( _month.substring(0,3).toLowerCase().equals("mar") ) {
            return 2;
        }
        else if ( _month.substring(0,3).toLowerCase().equals("apr") ) {
            return 3;
        }
        else if ( _month.substring(0,3).toLowerCase().equals("may") ) {
            return 4;
        }
        else if ( _month.substring(0,3).toLowerCase().equals("jun") ) {
            return 5;
        }
        else if ( _month.substring(0,3).toLowerCase().equals("jul") ) {
            return 6;
        }
        else if ( _month.substring(0,3).toLowerCase().equals("aug") ) {
            return 7;
        }
        else if ( _month.substring(0,3).toLowerCase().equals("sep") ) {
            return 8;
        }
        else if ( _month.substring(0,3).toLowerCase().equals("oct") ) {
            return 9;
        }
        else if ( _month.substring(0,3).toLowerCase().equals("nov") ) {
            return 10;
        }
        else if ( _month.substring(0,3).toLowerCase().equals("dec") ) {
            return 11;
        }
        else
            return 0;
    }
    
    public String getNextFileName( final String fileName ) {
        String numBuffer;
        String fileNamePortion1;
        String fileNamePortion2;
        java.util.regex.Pattern regexPattern=null;
        
        
        if ( fileName.indexOf(".")==-1 ) {
            fileNamePortion1=fileName;
            fileNamePortion2="";
        }
        else {
            fileNamePortion1=fileName.substring(0,fileName.lastIndexOf("."));
            fileNamePortion2=fileName.substring(fileName.lastIndexOf("."));
        }    

        if ( fileNamePortion1.indexOf("(")!=-1 && fileNamePortion1.indexOf(")")!=-1 ) {
            //see if file is currently numbered

            numBuffer=fileNamePortion1.substring(fileNamePortion1.lastIndexOf("(")+1,fileNamePortion1.lastIndexOf(")"));
            if ( regexPattern.compile("\\d*").matcher(numBuffer).matches() ) {
                fileNamePortion1=fileNamePortion1.substring(0,fileNamePortion1.lastIndexOf("(")+1) + (Integer.parseInt(numBuffer)+1) + ")";
            }
            else {
                fileNamePortion1=fileNamePortion1 + "(1)";
            }
        }
        else {
            //file is not numbered
            fileNamePortion1=fileNamePortion1 + "(1)";
        }
        
        return (fileNamePortion1 + fileNamePortion2);
        
    }
    
    public String getFreeFileName( final String directory, final String fileName ) {
        
        java.io.File _tempFile=new java.io.File(directory + fileName);
        String newFileName;
        

        if ( _tempFile.exists()==false ) {
            return fileName;
        }
        else {
            newFileName=fileName;
            while( (new java.io.File(directory + newFileName)).exists() ) {
                newFileName=getNextFileName(newFileName);
            }

            return newFileName;

        }
    }

        
}