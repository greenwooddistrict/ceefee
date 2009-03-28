
package ceefee.display;


import java.util.Calendar;

import java.awt.Color;
import java.awt.Point;


public class datagraph extends java.awt.Component {
    private ceefee.main mainFrameRef;
    
    public boolean active3;
    
    private int xOffset;
    private int yOffset;
    private Color baseColor;
    private Color textColor;
    private Color highlightColor;
    private String measure;
    private java.awt.Point[] liveData;
    private long startTime=java.util.Calendar.getInstance().getTimeInMillis();
    
    
    /** Creates a new instance of dataGraph */
    public datagraph(final ceefee.main _mainFrameRef, final int _xOffset, final int _yOffset, final Color _baseColor, final Color _textColor, final Color _highlightColor, final String _measure) {
        super();
        
        mainFrameRef=_mainFrameRef;
        
        xOffset=_xOffset;
        yOffset=_yOffset;
        baseColor=_baseColor;
        textColor=_textColor;
        highlightColor=_highlightColor;
        measure=_measure;
    }

    public void post( final int magnitude ) {
//        if ( active==false ) return;
        if ( isVisible()==false ) return;
        final java.awt.Point[] tempLiveDataBuffer=liveData;
        
        
        if ( liveData==null ) {
            liveData=new Point[1];
        } else {
            liveData=new Point[liveData.length+1];
            for ( int liveDataLooper=-1; ++liveDataLooper<tempLiveDataBuffer.length; ) {
                liveData[liveDataLooper]=tempLiveDataBuffer[liveDataLooper];
            }
        }
        liveData[liveData.length-1]=new Point((int)((java.util.Calendar.getInstance().getTimeInMillis()-startTime)/1000), magnitude);
        
        repaint();
        
    }
    
    public void paint( final java.awt.Graphics g ) {
        //String timeScale="";
        double highestMagnitude=0;
        final int textWidth=25;
        final int textHeight=5;
        final int numOfVirtualRows=8;
        final int numOfVirtualColumns=8;
        final int gridVerticalSpacing=(getHeight()-(yOffset*2))/numOfVirtualRows;
        final int gridHorizontalSpacing=(getWidth()-(xOffset*2))/numOfVirtualColumns;
        final int xAxisY=yOffset+(getHeight()-(yOffset*2))/2; //X position of Y axis
        final int yAxisX=xOffset+(getWidth()-(xOffset*2))/2; //Y position of X axis
        int verticalDivisionValue;
        

        g.setColor(baseColor);
        g.drawLine(yAxisX, yOffset, yAxisX, getHeight()-(yOffset));
        g.drawLine(xOffset, xAxisY, getWidth()-(xOffset), xAxisY);
        
        // aesthetics
        g.drawLine((gridHorizontalSpacing*3)+xOffset, (gridVerticalSpacing*8)+yOffset, (gridHorizontalSpacing*5)+xOffset, (gridVerticalSpacing*0)+yOffset);
        g.drawLine((gridHorizontalSpacing*2)+xOffset, (gridVerticalSpacing*8)+yOffset, (gridHorizontalSpacing*6)+xOffset, (gridVerticalSpacing*0)+yOffset);
        g.drawLine((gridHorizontalSpacing*1)+xOffset, (gridVerticalSpacing*8)+yOffset, (gridHorizontalSpacing*7)+xOffset, (gridVerticalSpacing*0)+yOffset);
        g.drawLine((gridHorizontalSpacing*0)+xOffset, (gridVerticalSpacing*8)+yOffset, (gridHorizontalSpacing*8)+xOffset, (gridVerticalSpacing*0)+yOffset);
        g.drawLine((gridHorizontalSpacing*0)+xOffset, (gridVerticalSpacing*7)+yOffset, (gridHorizontalSpacing*8)+xOffset, (gridVerticalSpacing*1)+yOffset);
        g.drawLine((gridHorizontalSpacing*0)+xOffset, (gridVerticalSpacing*6)+yOffset, (gridHorizontalSpacing*8)+xOffset, (gridVerticalSpacing*2)+yOffset);
        g.drawLine((gridHorizontalSpacing*0)+xOffset, (gridVerticalSpacing*5)+yOffset, (gridHorizontalSpacing*8)+xOffset, (gridVerticalSpacing*3)+yOffset);

        // columns
        for ( int columnLooper=-1; ++columnLooper<=numOfVirtualColumns; ) {
            for ( int rowLooper=-1; ++rowLooper<=numOfVirtualRows; ) {
                g.drawLine((gridHorizontalSpacing*columnLooper)+xOffset, (gridVerticalSpacing*rowLooper)+yOffset, (gridHorizontalSpacing*columnLooper)+xOffset, (gridVerticalSpacing*rowLooper)+yOffset+1);
            }
        }

        if ( isVisible() ) {
            if ( liveData!=null ) {
                for ( int liveDataLooper=-1; ++liveDataLooper<liveData.length; ) {
                    if ( liveData[liveDataLooper].getY()>highestMagnitude ) {
                        highestMagnitude=liveData[liveDataLooper].getY();
                    }
                }
            }

            g.drawString(String.valueOf(mainFrameRef.utilitiesClassInstance.formatSize(highestMagnitude,0)), yAxisX-(textWidth*3), yOffset+(textHeight*2));
            g.drawString(String.valueOf("0"), yAxisX+textWidth, getHeight()-(yOffset)-textHeight);

            verticalDivisionValue=(int)(highestMagnitude/numOfVirtualRows);

            g.setColor(highlightColor);

            if ( liveData!=null ) {
                for ( int liveDataLooper=-1; ++liveDataLooper<liveData.length; ) {
                    if ( (int)liveData[liveDataLooper].getX() > yAxisX ) {
                        startTime=java.util.Calendar.getInstance().getTimeInMillis();
                        liveData=null;
                        break;
                    }
                    else {
                        g.drawLine((int)liveData[liveDataLooper].getX(), getHeight()-(yOffset*2)-(int)((liveData[liveDataLooper].getY()/verticalDivisionValue)*gridVerticalSpacing), (int)liveData[liveDataLooper].getX(), getHeight()-(yOffset*2)-(int)((liveData[liveDataLooper].getY()/verticalDivisionValue)*gridVerticalSpacing));
                    }
                }
            }
        }
        
        g.setColor(textColor);
        g.drawString("( Y )", getWidth()-(xOffset*2)-(getWidth()/16)-textWidth, yOffset+(getHeight()/8));
        g.drawString("( X )", (getWidth()/16), getHeight()-(getHeight()/8));

    }
}
