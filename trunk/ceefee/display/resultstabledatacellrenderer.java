
package ceefee.display;


import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;


public class resultstabledatacellrenderer extends javax.swing.table.DefaultTableCellRenderer {

    private ceefee.main mainFrameRef;
    
    private ceefee.display.sb sbTabInstance;
    
    private Color highLightColor;
    private Color standardColor;
    

    public resultstabledatacellrenderer( final ceefee.main _mainFrameRef, final ceefee.display.sb _sbTabInstance, final Color _standardColor, final Color _highLightColor ) {
        mainFrameRef=_mainFrameRef;
        sbTabInstance=_sbTabInstance;
        highLightColor=_highLightColor;
        standardColor=_standardColor;
    }

    public Component getTableCellRendererComponent(final javax.swing.JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        
        Component cell = ((JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column));


        if ( mainFrameRef.utilitiesClassInstance.isFolderFormatting((String)value) ) {
            ((JLabel)cell).setIcon(mainFrameRef.folderIcon);
        }
        else {
            ((JLabel)cell).setIcon(mainFrameRef.fileIcon);
        }

        if ( sbTabInstance.resultsTableAccentedCells!=null ) {
            if ( sbTabInstance.resultsTableAccentedCells.indexOf(String.valueOf(row))!=-1 ) {
                ((JLabel)cell).setFont(mainFrameRef.boldAndItalicFont);
            }
        }

        return cell;
    }
}