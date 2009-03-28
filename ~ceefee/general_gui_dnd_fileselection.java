
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;


public class general_gui_dnd_fileselection extends java.util.Vector implements Transferable {
    
    DataFlavor[] supportedFlavors={ DataFlavor.javaFileListFlavor };
    
    
    public general_gui_dnd_fileselection( java.io.File f ) {
        addElement(f);
    }
    
    public Object getTransferData(java.awt.datatransfer.DataFlavor flavor) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
        return this;
    }
    
    public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
        return supportedFlavors;
    }
    
    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
        if ( flavor.equals(DataFlavor.javaFileListFlavor) )
            return true;
        else
            return false;
    }
    
}
