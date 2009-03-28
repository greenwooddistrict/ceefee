
package ceefee.display;


public class currentstate extends javax.swing.JFrame {
    private ceefee.main mainFrameRef;

    final private int staticWidth               =225;
    final private int staticHeight              =100;

    final private int titleBarHeight            =0x14;
    
    final private int fociPanelCellPadding      =0x05;
    
    private int statusFontSize;
    
    
    public currentstate(final ceefee.main _mainFrameRef, final boolean setUndecorated, final int xOffset, final int yOffset, final java.awt.Dimension parent, final String statusText, final int _statusFontSize, final java.awt.Color statusFontColor, final int statusAlignment, final String titleText, final String actionClickerText) {
        mainFrameRef=_mainFrameRef;
        
        setUndecorated(setUndecorated);
        
        setTitle(titleText);
        
        initComponents();
        
        if ( mainFrameRef!=null ) 
            setIconImage(mainFrameRef.getIconImage());

        if ( setUndecorated==false ) {
            fociPanel.setBorder(null);
            fociPanel.setBackground(new java.awt.Color(212,212,255));
        }
        
        if ( mainFrameRef!=null )
            setIconImage(mainFrameRef.logoIcon16.getImage());
        
        actionClicker.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        setSize(staticWidth, staticHeight);

        setLocation((int)((parent.getWidth()-getWidth())/2)+xOffset, (int)((parent.getHeight()-getHeight())/2.5)+yOffset);

        fociPanel.setBounds(0, 0, getWidth(), getHeight());

        statusLabel.setBounds(10, statusLabel.getY(), getWidth()-20, statusLabel.getHeight());
        statusLabel.setText(statusText);
        statusLabel.setHorizontalAlignment(statusAlignment);
        statusFontSize=_statusFontSize;
        if ( setUndecorated==false )
            statusLabel.setFont(new java.awt.Font("Dialog",java.awt.Font.PLAIN, statusFontSize));
        else
            statusLabel.setFont(new java.awt.Font("Dialog",java.awt.Font.BOLD|java.awt.Font.ITALIC, statusFontSize));
        statusLabel.setForeground(statusFontColor);

        actionClicker.setText("<HTML><U><I>"+actionClickerText+"</I></U></HTML>");
        actionClicker.setLocation(getWidth()-(actionClicker.getWidth()+15), getHeight()-(actionClicker.getHeight()+30));
        
        repaint();
        
    }

    public void update( final String newStatusText, final String newActionText ) {
        if ( newStatusText.equals("")==false )
            statusLabel.setText(newStatusText);
        
        if ( newActionText.equals("")==false )
            actionClicker.setText("<HTML><U>"+newActionText+"</U></HTML>");        
        
    }

    private void initComponents() {//GEN-BEGIN:initComponents
        fociPanel = new javax.swing.JPanel();
        actionClicker = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        fociPanel.setLayout(null);

        fociPanel.setBackground(new java.awt.Color(182, 183, 228));
        fociPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3));
        actionClicker.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        actionClicker.setIcon(new javax.swing.ImageIcon(""));
        actionClicker.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                actionClickerMouseClicked(evt);
            }
        });

        fociPanel.add(actionClicker);
        actionClicker.setBounds(160, 70, 50, 20);

        statusLabel.setFont(new java.awt.Font("Dialog", 3, 24));
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fociPanel.add(statusLabel);
        statusLabel.setBounds(30, 5, 70, 30);

        getContentPane().add(fociPanel);
        fociPanel.setBounds(0, 0, 225, 100);

        pack();
    }//GEN-END:initComponents

    private void actionClickerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_actionClickerMouseClicked
        close();
    }//GEN-LAST:event_actionClickerMouseClicked
    
    /** Closes the dialog */
    public void close() {
        setVisible(false);
        dispose();
    }
    
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        close();
    }//GEN-LAST:event_closeDialog
    
    public static void main(String args[]) {
        final ceefee.display.currentstate cs=new ceefee.display.currentstate(null,true,0,0,java.awt.Toolkit.getDefaultToolkit().getScreenSize(),"...fits like a glove.",16,java.awt.Color.white,javax.swing.SwingConstants.RIGHT,"","");
        cs.setVisible(true);
        new ceefee.main(cs);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel actionClicker;
    private javax.swing.JPanel fociPanel;
    public javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
    
}
