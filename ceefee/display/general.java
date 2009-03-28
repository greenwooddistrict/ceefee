
package ceefee.display;


public class general {
        
    private ceefee.main mainFrameRef;
    
    
    public general(final ceefee.main _mainFrameRef) {
        mainFrameRef=_mainFrameRef;
    }      

    public void addAccentedCellToResultsDataCellRenderer(final ceefee.display.sb sbTabInstance, final int newIndex ) {
        
        java.util.Vector _tempResultsTableAccentedCells=(java.util.Vector)sbTabInstance.resultsTableAccentedCells;
        if ( _tempResultsTableAccentedCells==null ) {
             _tempResultsTableAccentedCells=new java.util.Vector(0,1);
             sbTabInstance.resultsTableAccentedCells=new java.util.Vector(0,1);
        }

        _tempResultsTableAccentedCells.add(String.valueOf(newIndex));
        
        sbTabInstance.resultsTableAccentedCells.add(sbTabInstance.tabIndex, _tempResultsTableAccentedCells);
    }
    
    private void setWindowParameters(final String formName, final javax.swing.JInternalFrame refFrame) {
        
        String tLeft;
        String tTop;
        String tWidth;
        String tHeight;


        tLeft = mainFrameRef.fileioClassInstance.getDataFromFile(formName + "_.pos", "left", false)[0].toString();
        if ( tLeft.equals("") )
            return;
        tTop = mainFrameRef.fileioClassInstance.getDataFromFile(formName + "_.pos", "top", false)[0].toString();
        if ( tTop.equals("") )
            return;
        tHeight = mainFrameRef.fileioClassInstance.getDataFromFile(formName + "_.pos", "height", false)[0].toString();
        if ( tHeight.equals("") )
            return;
        tWidth = mainFrameRef.fileioClassInstance.getDataFromFile(formName + "_.pos", "width", false)[0].toString();
        if ( tWidth.equals("") )
            return;
        
        refFrame.setBounds(Integer.parseInt(tLeft),Integer.parseInt(tTop),Integer.parseInt(tWidth),Integer.parseInt(tHeight));
    }
    
}
