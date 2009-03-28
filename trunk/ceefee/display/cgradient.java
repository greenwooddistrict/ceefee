package ceefee.display;

public class cgradient extends javax.swing.JLabel {
        
    private java.awt.Color fromColor;
    private java.awt.Color toColor;
        
    private int x1;
    private int y1;
        
        
    public cgradient(final int _x1, final int _y1, final java.awt.Color _fromColor, final java.awt.Color _toColor) {
        x1=_x1;
        y1=_y1;
        fromColor=_fromColor;
        toColor=_toColor;
    }
        
    public void paintComponent(final java.awt.Graphics graphics) {
        java.awt.Graphics2D g2=(java.awt.Graphics2D)graphics;

        g2.setPaint(new java.awt.GradientPaint(x1, y1, fromColor, getWidth(), getHeight(), toColor));
        g2.fillRect(x1, y1, getWidth(), getHeight());
    }
    
}
