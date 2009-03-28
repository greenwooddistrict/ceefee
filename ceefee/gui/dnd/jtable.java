
package ceefee.gui.dnd;


import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.datatransfer.*;


public class jtable extends javax.swing.JTable implements DropTargetListener {
    
    private ceefee.main mainFrameRef;

    private DropTarget dt=new DropTarget(this,DnDConstants.ACTION_COPY_OR_MOVE,this);
    
    private String filePath;

    
    public jtable(final ceefee.main _mainFrameRef, final ceefee.gui.TableSorter ts) {
        mainFrameRef=_mainFrameRef;
        setModel(ts);
    }

    
    public void dragExit(final DragSourceEvent dse) {
    }
    public void dragExit(final DropTargetEvent dte) {
    }
    
    public void dropActionChanged(final DragSourceDragEvent dsde) {
    }
    public void dropActionChanged(final DropTargetDragEvent dtde) {
    }

    public void dragOver(final DragSourceDragEvent dsde) {
    }
    public void dragOver(final DropTargetDragEvent dtde) {
        if ( isEnabled()==false ) {
            dtde.rejectDrag();
            return;
        }
    }
    
    public void dragEnter(final DragSourceDragEvent dsde) {
    }
    public void dragEnter(final DropTargetDragEvent dtde) {
    }
    
    public void dragDropEnd(final DragSourceDropEvent dsde) {
    }
    

    public void drop(final DropTargetDropEvent dtde) {
        if ( isEnabled()==false ) {
            dtde.rejectDrop();
            return;
        }
        mainFrameRef.performGuiDropOperation(dtde);
    }

}