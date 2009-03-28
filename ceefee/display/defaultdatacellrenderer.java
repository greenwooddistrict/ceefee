
package ceefee.display;


public class defaultdatacellrenderer extends javax.swing.table.DefaultTableCellRenderer {

    private ceefee.main mainFrameRef;
    
    private java.awt.Color highLightColor;
        
        
    public defaultdatacellrenderer(final ceefee.main _mainFrameRef, final java.awt.Color _highLightColor) {
        mainFrameRef=_mainFrameRef;
        highLightColor=_highLightColor;
    }

    public java.awt.Component getTableCellRendererComponent(final javax.swing.JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        java.awt.Component cell = ((javax.swing.JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column));
            
            
        if ( mainFrameRef.utilitiesClassInstance.isFolderFormatting((String)value) ) {
            ((javax.swing.JLabel)cell).setIcon(mainFrameRef.folderIcon);
        } else {
            ((javax.swing.JLabel)cell).setIcon(mainFrameRef.fileIcon);
        }
            
        if ( highLightColor!=java.awt.Color.white ) {
            if ( (row%2)==0 ) {
                ((javax.swing.JLabel)cell).setBackground(highLightColor);
            }
        }
            

        return cell;
    }
}