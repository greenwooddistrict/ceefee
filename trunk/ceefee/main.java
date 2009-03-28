
package ceefee;


import java.awt.Toolkit;

import java.math.BigInteger;

import javax.swing.table.DefaultTableModel;

import java.awt.dnd.DnDConstants;
import java.awt.datatransfer.DataFlavor;

import java.awt.Cursor;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;

import java.awt.Dimension;
import java.awt.event.WindowEvent;

import java.util.Date;

import java.util.StringTokenizer;

import java.util.Vector;


public class main extends javax.swing.JFrame {

    final boolean DEBUGGING_MODE                        =true;
    
    final String programName                            ="CeeFee";
    final String programVersion                         ="0.1";
    final String programRelease                         ="b";
    
    final static java.util.Random rng=new java.util.Random();
            
    final static int MAX_BYTE                           =0xFF;
    
    final static int MAX_UINT32                         =0xFFFFFFFF;
    
    public final static int SEARCH_METHOD                      =0;
    public final static int BROWSE_METHOD                      =1;
    
    public static ceefee.ftp.general ftpClassInstance;
            
    public ceefee.misc.fileio fileioClassInstance;
                       
    public static ceefee.display.general displayClassInstance;
                
    public static ceefee.misc.utilities utilitiesClassInstance;
    
    public static ceefee.display.currentstate currentstateInstance;

    public static ceefee.misc.sound soundClassInstance=new ceefee.misc.sound();
        
    public String downloadDir;
    
    private int scourMethod0;
        
    public int connectionMTU;
                        
    public int connectionAttempts;
    
    public int connectionTimeout;
    
    public ceefee.sockets.general socketClassInstance;
        
    public ceefee.display.sb searchTab[];
    public ceefee.display.sb browseTab[];
    
    public ceefee.display.view viewTab;
    
    public ceefee.display.schedule scheduleTab;
    
    
    // Configuration
    public final String lineSeparator=System.getProperty("line.separator");
            
    private JFileChooser JFileChooserInstance=new JFileChooser();
    
    public int graphicsMode;
    
    public Cursor defaultCursor=new Cursor(Cursor.DEFAULT_CURSOR);
    public Cursor waitCursor=new Cursor(Cursor.WAIT_CURSOR);
    
    public java.awt.Color default1=new java.awt.Color(153,153,204);
    public java.awt.Color default2=new java.awt.Color(182,183,228);
    public java.awt.Color default3=new java.awt.Color(212,212,255);
    public java.awt.Color default4=new java.awt.Color(245,245,255);
    public java.awt.Color darkGray3=new java.awt.Color(75,75,75);
    //public Color darkGray4=new Color(100,100,100);
    
    private ImageIcon searchIcon;
    private ImageIcon browseIcon;
    private ImageIcon viewIcon;
    private ImageIcon scheduleIcon;
    public ImageIcon configureIcon;
    public  Icon fileIcon=JFileChooserInstance.getIcon(new java.io.File("file.ext"));
    public ImageIcon downArrow=new ImageIcon(this.getClass().getClassLoader().getResource("ceefee/_downArrow12.png"));
    public ImageIcon rightArrow=new ImageIcon(this.getClass().getClassLoader().getResource("ceefee/_rightArrow12.png"));
    public Icon folderIcon=new ImageIcon(this.getClass().getClassLoader().getResource("ceefee/_closedFolder16.png"));
    public ImageIcon logoIcon16=new ImageIcon(this.getClass().getClassLoader().getResource("ceefee/_dd16.gif"));
    public ImageIcon lockIcon;
    public ImageIcon uploadingIcon=new ImageIcon(this.getClass().getClassLoader().getResource("ceefee/_uploading16.png"));
    public ImageIcon downloadingIcon=new ImageIcon(getClass().getClassLoader().getResource("ceefee/_downloading16.png"));
    
    public ImageIcon clockIcon=new javax.swing.ImageIcon(this.getClass().getClassLoader().getSystemResource("ceefee/_clock.png"));

    public final java.awt.Cursor handCursor=new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR);
    public final java.awt.Cursor northResizeCursor=new java.awt.Cursor(java.awt.Cursor.N_RESIZE_CURSOR);
    public final java.awt.Cursor westResizeCursor=new java.awt.Cursor(java.awt.Cursor.W_RESIZE_CURSOR);
    
    final private int MINIMUM_WIDTH                                  =680;
    final private int MINIMUM_HEIGHT                                 =540;
    
    final public int sweetSpot                                       =3;

    public final int sideMargin                                      =10;
    public final int topMargin                                       =10;

    public String showsbTabAdvancedView;
    
    final int Preferred                                              =23;
    
    public final Dimension zeroDimension=new java.awt.Dimension(0,0);
    
    public final java.awt.Font standardFont=new java.awt.Font(null, 0,  12);
    public final java.awt.Font boldAndItalicFont=new java.awt.Font(null, java.awt.Font.BOLD|java.awt.Font.ITALIC,  11);

    public final ceefee.gui.folderfilter folderFilterInstance=new ceefee.gui.folderfilter();
    
    // Display
    public final double firstColumnPercentage                  =.6;
    public final double secondColumnPercentage                 =.15;
    public final double thirdColumnPercentage                  =.25;

    public javax.swing.table.TableCellRenderer defaultDataCellRendererInstance;

    public final int SEARCH_PANEL                              =0;
    public final int BROWSE_PANEL                              =1;
    public final int VIEW_PANEL                                =2;
    public final int SCHEDULE_PANEL                            =3;
    public final int CONFIGURE_PANEL                            =4;
    

    // Dnd
    boolean dndOperationActive;


    // Flags
    public boolean displayDeleteConfirmations;
    public boolean displayOverwriteConfirmations;
    
    private boolean viewTabMainSplitPaneLock;
    private boolean viewTabTablesUpdated;
    
    private boolean lockOrganize;
    
    // MISC
    public int historyTrailSize;
    public String clipboardData=null;
    
    // Frame Display
    private ceefee.display.cgradient cgradientClassInstance;            
   
    
    // SSH2
    public int serverKeyPublicExponentBitLength;
    int serverKeyPublicExponentByteLength;

    java.io.BufferedInputStream sshInputStream;
    java.io.BufferedOutputStream sshOutputStream;
    Thread ssh2ConnectClassInstanceThread;

    byte[] SSH2_CLIENT_VERSION_STRING;
    byte[] SSH2_SERVER_VERSION_STRING;

    byte[] SSH2_CLIENT_KEXINIT_PAYLOAD;
    byte[] SSH2_SERVER_KEXINIT_PAYLOAD;

    byte[] H;

    BigInteger K;

    BigInteger e;

    final BigInteger g=new BigInteger("2"); // Generator

    final BigInteger p=new BigInteger("179769313486231590770839156793787453197860296048756011706444423684197180216158519368947833795864925541502180565485980503646440548199239100050792877003355816639229553136239076508735759914822574862575007425302077447712589550957937778424442426617334727629299387668709205606050270810842907692932019128194467627007");

    BigInteger x;

    byte[] hashHASH;
    
    
    // FTP
    public final String[] securityLayer={"Standard FTP"};//, "SSL FTP", "SecureFTP (SSH2)"};
    public final String[] securityLayerDefaultPort={"21"};//, "21", "22"};


    public main( final ceefee.display.currentstate workingDisplayInstance ) {
        final int topPanelBuffer            =25;
        String buffer;

        //SSH2_CLIENT_VERSION_STRING=("SSH-"+mainFrameRef.ssh2ClassInstance.sshMajorVersion+"."+mainFrameRef.ssh2ClassInstance.sshMinorVersion+"-"+mainFrameRef.programName+"-"+mainFrameRef.programVersion).getBytes();
        fileioClassInstance=new ceefee.misc.fileio(this);        
        displayClassInstance=new ceefee.display.general(this);
        utilitiesClassInstance=new ceefee.misc.utilities(this);
        ftpClassInstance=new ceefee.ftp.general(this);
        socketClassInstance=new ceefee.sockets.general(this);
        
        downloadDir=fileioClassInstance.getDataFromFile("_user.dat","downloaddir", false)[0].toString();
        if ( downloadDir.equals("") ) {
            downloadDir=utilitiesClassInstance.getRootPathFromPath(System.getProperty("user.dir")+System.getProperty("file.separator"),System.getProperty("file.separator"))+"My Downloads"+System.getProperty("file.separator");
            fileioClassInstance.storeDataToFile("_user.dat","downloaddir","",downloadDir, 1, 1, "", false);
        } else if ( downloadDir.substring(downloadDir.length()-1,downloadDir.length()).equals(System.getProperty("file.separator"))==false ) {
            downloadDir=downloadDir+System.getProperty("file.separator");
            fileioClassInstance.storeDataToFile("_user.dat","downloaddir","",downloadDir, 1, 1, "", false);
        }

        if ( new java.io.File(downloadDir).exists()==false ) {
            if ( new java.io.File(downloadDir).mkdir()==false ) {
                downloadDir=System.getProperty("user.dir")+System.getProperty("file.separator");
            }
            fileioClassInstance.storeDataToFile("_user.dat","downloaddir","",downloadDir, 1, 1, "", false);
        }

        graphicsMode=Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat","graphicsmode", false)[0].toString());
        
        if ( graphicsMode==1 ) {
            searchIcon=new ImageIcon(this.getClass().getClassLoader().getSystemResource("ceefee/_search16.png"));
            browseIcon=new ImageIcon(this.getClass().getClassLoader().getResource("ceefee/_browse16.png"));
            viewIcon=new ImageIcon(this.getClass().getClassLoader().getResource("ceefee/_activity16.png"));
            scheduleIcon=new ImageIcon(this.getClass().getClassLoader().getResource("ceefee/_schedule16.png"));
            configureIcon=new ImageIcon(this.getClass().getClassLoader().getResource("ceefee/_configure16.png"));
            lockIcon=new ImageIcon(this.getClass().getClassLoader().getResource("ceefee/_lock16.png"));
        }
        
        if ( fileioClassInstance.getDataFromFile("_user.dat","displaydeleteconfirmations", false)[0].toString().toLowerCase().equals("true") )
            displayDeleteConfirmations=true;
        else
            displayDeleteConfirmations=false;
        if ( fileioClassInstance.getDataFromFile("_user.dat","displayoverwriteconfirmations", false)[0].toString().toLowerCase().equals("true") )
            displayOverwriteConfirmations=true;
        else
            displayOverwriteConfirmations=false;

        connectionMTU=Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat","connectiondefaultmtu", false)[0].toString());
        connectionTimeout=Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat","connectiontimeout", false)[0].toString());
        connectionAttempts=Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat","connectionattempts", false)[0].toString());

        defaultDataCellRendererInstance=new ceefee.display.defaultdatacellrenderer(this, java.awt.Color.white);

        lockOrganize=true;

        initComponents();

        if ( graphicsMode==1 ) {
            logoLabel.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("ceefee/_dd32.png")));
            logoLabel.setLocation(20,logoLabel.getY());
            logoLabel.setForeground(java.awt.Color.white);
            logoLabel.setText(programName+" FTP");
            
        } else {
            gradientRim.setSize(gradientRim.getWidth(), 35);
            gradientRim.setBackground(default1);

            logoLabel.setLocation(20,logoLabel.getY());
            logoLabel.setForeground(java.awt.Color.white);
            logoLabel.setText(programName+" FTP");
            
            topPanel.setSize(0,topPanelBuffer+40);
            
            bottomRim.setSize(bottomRim.getWidth(), 0);
        }
        searchIconLabel.setLocation(searchIconLabel.getX(), topPanel.getHeight()-25);
        browseIconLabel.setLocation(browseIconLabel.getX(), topPanel.getHeight()-25);
        viewIconLabel.setLocation(viewIconLabel.getX(), topPanel.getHeight()-25);
        scheduleIconLabel.setLocation(scheduleIconLabel.getX(), topPanel.getHeight()-25);
        configureIconLabel.setLocation(configureIconLabel.getX(), topPanel.getHeight()-25);
        
        setTitle(programName+" v"+programVersion+programRelease);

        mainPanel.setBackground(default4);
        
        securityLayerLabel.setVisible(false);
        
        setExtendedState(Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat","extendedstate", false)[0].toString()));
        setSize(Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat","width", false)[0].toString()),Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat","height", false)[0].toString()));
        setLocation(Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat","x-position", false)[0].toString()),Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat","y-position", false)[0].toString()));

        addNewConnectionTab(SEARCH_METHOD);
        addNewConnectionTab(BROWSE_METHOD);
        
        soundClassInstance.playSound("_start.wav");

        setIconImage(logoIcon16.getImage());

        showsbTabAdvancedView=fileioClassInstance.getDataFromFile("_user.dat","showsbtabadvancedview", false)[0].toString();
        if ( showsbTabAdvancedView.toLowerCase().equals("false") ) 
            getSelectedSbTab().hideUnhide(true);

        bottomRim.setBackground(default1);
        
        mainPanel.setLocation(0,0);
        topPanel.setLocation(0,0);
                                  
        historyTrailSize=Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat", "historytrailsize", false)[0].toString());

        scheduleTab=new ceefee.display.schedule(this);
        mainPanel.add(scheduleTab,0);
        scheduleTab.setLocation(-2, topPanel.getHeight());

        viewTab=new ceefee.display.view(this,Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat","viewtabhorizontalsplitpanedividerlocation", false)[0].toString()));
        mainPanel.add(viewTab,0);
        viewTab.setLocation(-2, topPanel.getHeight());
        viewTab.setBackground(default4);
                    
        scourMethod0=Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat", "lastmethod", false)[0].toString());
        if ( scourMethod0==SEARCH_METHOD ) {
            browseIconLabelMouseClicked(null);
            searchIconLabelMouseClicked(null);
        } else {
            searchIconLabelMouseClicked(null);
            browseIconLabelMouseClicked(null);
        }

        searchTabPane.setLocation(-2, topPanel.getHeight());
        if ( graphicsMode==1 ) searchTabPane.setBackground(default4);
        browseTabPane.setLocation(-2, topPanel.getHeight());
        if ( graphicsMode==1 ) browseTabPane.setBackground(default4);

        securityLayerLabel.setForeground(default4);

        lockOrganize=false;
        
        viewTab.resumeUploads();
        viewTab.resumeDownloads();
        
        scheduleTab.resumeTasks();
        
        organize(SEARCH_METHOD, searchTabPane.getTabCount()-1, true);
        organize(BROWSE_METHOD, browseTabPane.getTabCount()-1, true);

        if ( workingDisplayInstance!=null )
            workingDisplayInstance.close();
        
        setVisible(true);
        
    }

    public ceefee.display.sb getSelectedSbTab() {
        int selectedIndex=0;
        
        if ( scourMethod0==SEARCH_METHOD ) {
            if ( (selectedIndex=searchTabPane.getSelectedIndex())==-1 )
                selectedIndex=0;
            
            return searchTab[selectedIndex];
            
        } else if ( scourMethod0==BROWSE_METHOD ) {
            if ( (selectedIndex=browseTabPane.getSelectedIndex())==-1 )
                selectedIndex=0;
            
            return browseTab[selectedIndex];
            
        }
        
        if ( scourMethod0==SEARCH_METHOD )
            return searchTab[selectedIndex];
        else //if ( scourMethod==BROWSE_METHOD )
            return browseTab[selectedIndex];
        
    }
    
    public void reload() {
        closeAll(false);
        try {
            Process osf=Runtime.getRuntime().exec(new String[] {"_cfe.exe", "load"});
        } catch ( java.io.IOException ioe ) {}
        System.exit(0);
    }
    
    public void removeTabPaneTab( final int scourMethod, final int itemIndex ) {
        ceefee.display.sb[] sbTabBuffer1=null;
        ceefee.display.sb[] sbTabBuffer2=null;
        
        int sbTabBufferlooper=-1;
        
        boolean indexSkipped=false;
        
        
        if ( (scourMethod==SEARCH_METHOD && searchTab==null) || (scourMethod==BROWSE_METHOD && browseTab==null) ) {
            return;
            
        } else {
            if ( scourMethod==SEARCH_METHOD )
                sbTabBuffer1=searchTab;
            else if ( scourMethod==BROWSE_METHOD )
                sbTabBuffer1=browseTab;
            
            sbTabBuffer2=new ceefee.display.sb[sbTabBuffer1.length-1];
            for ( int sbTabBuffer2looper=-1; ++sbTabBuffer2looper<(sbTabBuffer1.length-1); ) {

                if ( sbTabBuffer2looper==itemIndex ) {
                    sbTabBufferlooper+=2;
                    indexSkipped=true;
                } else
                    ++sbTabBufferlooper;

                sbTabBuffer2[sbTabBuffer2looper]=sbTabBuffer1[sbTabBufferlooper];

            }
            
        }

        if ( scourMethod==SEARCH_METHOD ) {
            searchTab=sbTabBuffer2;
            searchTabPane.removeTabAt(itemIndex);
            searchTabPane.repaint();
        } else if ( scourMethod==BROWSE_METHOD ) {
            browseTab=sbTabBuffer2;
            browseTabPane.removeTabAt(itemIndex);
            browseTabPane.repaint();
        }

    }

    public void addNewConnectionTab( final int searchTab0browseTab1 ) {
        ceefee.display.sb[] sbTabBuffer1;
        if ( searchTab0browseTab1==0 )
            sbTabBuffer1=searchTab;
        else
            sbTabBuffer1=browseTab;
        ceefee.display.sb[] sbTabBuffer2;

        int sbTabBufferLooper=-1;
        

        if ( sbTabBuffer1==null ) {
            sbTabBuffer2=new ceefee.display.sb[1];
        } else {
            sbTabBuffer2=new ceefee.display.sb[sbTabBuffer1.length+1];
            for ( int sbDefaultTabBbufferLooper=-1; ++sbTabBufferLooper<sbTabBuffer1.length; ) {
                sbTabBuffer2[sbTabBufferLooper]=sbTabBuffer1[sbTabBufferLooper];
            }
        }
        if ( searchTab0browseTab1==0 ) {
            sbTabBuffer2[sbTabBuffer2.length-1]=new ceefee.display.sb(this,searchTab0browseTab1,searchTabPane.getTabCount());
            searchTab=sbTabBuffer2;
            if ( searchTabPane.indexOfTab("< New >")==-1 ) {
                searchTabPane.addTab("< New >", searchTab[searchTab.length-1]);
                searchTabPane.repaint();
            }
            searchTabPane.setBackgroundAt(searchTabPane.getTabCount()-1, default3);
            searchTabPane.setFocusable(false);
            searchTabPaneMouseClicked(new java.awt.event.MouseEvent(searchTabPane, -1, -1, -1, -1, -1, 1, false, java.awt.event.MouseEvent.BUTTON1));
            searchTab[searchTabPane.getTabCount()-1].mainSplitPane.setDividerLocation(Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat","sbtabmaindividerlocation", false)[0].toString()));
            searchTab[searchTabPane.getTabCount()-1].enableDisable(0,-1);
            organize(searchTab0browseTab1,searchTabPane.getTabCount()-1,true);
        } else if ( searchTab0browseTab1==1 ) {
            sbTabBuffer2[sbTabBuffer2.length-1]=new ceefee.display.sb(this,searchTab0browseTab1,browseTabPane.getTabCount());
            browseTab=sbTabBuffer2;
            if ( browseTabPane.indexOfTab("< New >")==-1 ) {
                browseTabPane.addTab("< New >", browseTab[browseTab.length-1]);
                browseTabPane.repaint();
            }
            browseTabPane.setBackgroundAt(browseTabPane.getTabCount()-1, default3);
            browseTabPane.setFocusable(false);
            browseTabPaneMouseClicked(new java.awt.event.MouseEvent(browseTabPane, -1, -1, -1, -1, -1, 1, false, java.awt.event.MouseEvent.BUTTON1));
            browseTab[browseTabPane.getTabCount()-1].mainSplitPane.setDividerLocation(Integer.parseInt(fileioClassInstance.getDataFromFile("_user.dat","sbtabmaindividerlocation", false)[0].toString()));
            browseTab[browseTabPane.getTabCount()-1].enableDisable(0,-1);
            organize(searchTab0browseTab1,browseTabPane.getTabCount()-1,true);
        }
    }

    public void performGuiDropOperation( final java.awt.dnd.DropTargetDropEvent dtde ) {
        final ceefee.display.sb sbTabInstance=getSelectedSbTab();
        if ( sbTabInstance.busy ) {
            dtde.rejectDrop();
            return;
        }

       
        String filePath;
        java.awt.datatransfer.DataFlavor[] dfs;
        java.awt.datatransfer.Transferable tr = dtde.getTransferable();
        java.util.List fileList;
        java.util.Iterator fileListIterator;
        
        
        dfs=tr.getTransferDataFlavors();
        if ( tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ) {
            dtde.acceptDrop(DnDConstants.ACTION_COPY);            
        } else {
            dtde.rejectDrop();
            return;
        }

        try {
            fileList=(java.util.List)tr.getTransferData(DataFlavor.javaFileListFlavor);
        } catch ( java.awt.datatransfer.UnsupportedFlavorException ufe ) {
            dtde.rejectDrop();
            return;
        } catch ( java.io.IOException ioe ) {
            dtde.rejectDrop();
            return;
        }
        
        fileListIterator=fileList.iterator();
        
        while ( fileListIterator.hasNext() ) {
            filePath=fileListIterator.next().toString();
            ftpClassInstance.startFtpUpload(sbTabInstance, sbTabInstance.sessionID, currentstateInstance, null, null, filePath, new java.io.File(filePath).isDirectory(), sbTabInstance.currentWD, "", sbTabInstance.hostAddress.getText(), sbTabInstance.hostPort.getText(), sbTabInstance.hostUsername.getText(), new String(sbTabInstance.hostPassword.getPassword()), String.valueOf(sbTabInstance.getDataFormat()), String.valueOf(sbTabInstance.getConnectionMethod()), false, false);
            showPanel(2);
        }
        
        dtde.getDropTargetContext().dropComplete(true);
    } 

    private void vanguardIconLabel( final int labelIndex ) {
        final String htmlHeader="<HTML><U>";
        final String htmlTrailer="</U></HTML>";

                

        if ( labelIndex==0 ) {
            if ( searchIconLabel.getText().length()>=htmlHeader.length() && searchIconLabel.getText().substring(0,htmlHeader.length()).equals(htmlHeader) )
                searchIconLabel.setText(searchIconLabel.getText().substring(htmlHeader.length(),searchIconLabel.getText().length()-htmlTrailer.length()));
            searchIconLabel.setCursor(null);
        } else {
            if ( searchIconLabel.getText().length()<htmlHeader.length() || searchIconLabel.getText().substring(0,htmlHeader.length()).equals(htmlHeader)==false )
                searchIconLabel.setText(htmlHeader+searchIconLabel.getText()+htmlTrailer);
            searchIconLabel.setCursor(handCursor);
        }
        
        if ( labelIndex==1 ) {
            if ( browseIconLabel.getText().length()>=htmlHeader.length() && browseIconLabel.getText().substring(0,htmlHeader.length()).equals(htmlHeader) )
                browseIconLabel.setText(browseIconLabel.getText().substring(htmlHeader.length(),browseIconLabel.getText().length()-htmlTrailer.length()));
            browseIconLabel.setCursor(null);
        } else {
            if ( browseIconLabel.getText().length()<htmlHeader.length() || browseIconLabel.getText().substring(0,htmlHeader.length()).equals(htmlHeader)==false )
                browseIconLabel.setText(htmlHeader+browseIconLabel.getText()+htmlTrailer);
            browseIconLabel.setCursor(handCursor);
        }
        
        if ( labelIndex==2 ) {
            if ( viewIconLabel.getText().length()>=htmlHeader.length() && viewIconLabel.getText().substring(0,htmlHeader.length()).equals(htmlHeader) )
                viewIconLabel.setText(viewIconLabel.getText().substring(htmlHeader.length(),viewIconLabel.getText().length()-htmlTrailer.length()));
            viewIconLabel.setCursor(null);
        } else {
            if ( viewIconLabel.getText().length()<htmlHeader.length() || viewIconLabel.getText().substring(0,htmlHeader.length()).equals(htmlHeader)==false )
                viewIconLabel.setText(htmlHeader+viewIconLabel.getText()+htmlTrailer);
            viewIconLabel.setCursor(handCursor);
        }
        
        if ( labelIndex==3 ) {
            if ( scheduleIconLabel.getText().length()>=htmlHeader.length() && scheduleIconLabel.getText().substring(0,htmlHeader.length()).equals(htmlHeader) )
                scheduleIconLabel.setText(scheduleIconLabel.getText().substring(htmlHeader.length(),scheduleIconLabel.getText().length()-htmlTrailer.length()));
            scheduleIconLabel.setCursor(null);
        } else {
            if ( scheduleIconLabel.getText().length()<htmlHeader.length() || scheduleIconLabel.getText().substring(0,htmlHeader.length()).equals(htmlHeader)==false )
                scheduleIconLabel.setText(htmlHeader+scheduleIconLabel.getText()+htmlTrailer);
            scheduleIconLabel.setCursor(handCursor);
        }
            
        if ( labelIndex==3 ) {
            if ( configureIconLabel.getText().length()>=htmlHeader.length() && configureIconLabel.getText().substring(0,htmlHeader.length()).equals(htmlHeader) )
                configureIconLabel.setText(configureIconLabel.getText().substring(htmlHeader.length(),configureIconLabel.getText().length()-htmlTrailer.length()));
            configureIconLabel.setCursor(null);
        } else {
            if ( configureIconLabel.getText().length()<htmlHeader.length() || configureIconLabel.getText().substring(0,htmlHeader.length()).equals(htmlHeader)==false )
                configureIconLabel.setText(htmlHeader+configureIconLabel.getText()+htmlTrailer);
            configureIconLabel.setCursor(handCursor);
        }

    }
    public boolean showPanel( final int panel ) {

        if ( panel==SEARCH_PANEL ) {
            scourMethod0=0;
            searchTabPane.setVisible(true);
            searchTabPane.setEnabled(true);
            browseTabPane.setVisible(false);
            browseTabPane.setEnabled(false);
            viewTab.setVisible(false);
            viewTab.setEnabled(false);
            scheduleTab.setVisible(false);
            scheduleTab.setEnabled(false);

            vanguardIconLabel(0);
            
            organize(scourMethod0,searchTabPane.getSelectedIndex(),false);

            return true;
            
        } else if ( panel==BROWSE_PANEL ) {
            scourMethod0=1;
            searchTabPane.setVisible(false);
            searchTabPane.setEnabled(false);
            browseTabPane.setVisible(true);
            browseTabPane.setEnabled(true);
            viewTab.setVisible(false);
            viewTab.setEnabled(false);
            scheduleTab.setVisible(false);
            scheduleTab.setEnabled(false);

            vanguardIconLabel(1);
            
            organize(scourMethod0,browseTabPane.getSelectedIndex(),false);

            return true;
            
        } else if ( panel==VIEW_PANEL ) {
            searchTabPane.setVisible(false);
            searchTabPane.setEnabled(false);
            browseTabPane.setVisible(false);
            browseTabPane.setEnabled(false);
            viewTab.setVisible(true);
            viewTab.setEnabled(true);
            scheduleTab.setVisible(false);
            scheduleTab.setEnabled(false);

            vanguardIconLabel(2);
            
            organize(scourMethod0,-1,false);

            return true;
            
        } else if ( panel==SCHEDULE_PANEL ) {
            searchTabPane.setVisible(false);
            searchTabPane.setEnabled(false);
            browseTabPane.setVisible(false);
            browseTabPane.setEnabled(false);
            viewTab.setVisible(false);
            viewTab.setEnabled(false);
            scheduleTab.setVisible(true);
            scheduleTab.setEnabled(true);
        
            vanguardIconLabel(3);
            
            organize(scourMethod0,-1,false);
            
            return true;
            
        } else if ( panel==CONFIGURE_PANEL ) {
            //sbTabPane.setVisible(false);
            //sbTabPane.setEnabled(false);
            //viewTab.setVisible(false);
            //viewTab.setEnabled(false);
            //dataGraphInstance.active=false;
            //scheduleTab.setVisible(true);
            //scheduleTab.setEnabled(true);
            new ceefee.display.configure(this, getX(),getY(),getWidth(),getHeight()).show();

            //vanguardIconLabel(4);
            
            //organize(sbTabInstance.tabIndex);
            
            return false;
            
        } else
            return false;   

    }
    
    public void storeHistoryTrail( final ceefee.display.sb sbTabInstance, final javax.swing.JComboBox trailSource, final String groupNumber ) {
        String historyTrailBuffer="";
        int trailLooper=0;
        
        
        if ( sbTabInstance.scourMethod==SEARCH_METHOD )
            trailLooper=-1;
        else if ( sbTabInstance.scourMethod==BROWSE_METHOD )
            trailLooper=1;
        
        for ( ; ++trailLooper<trailSource.getItemCount(); ) {
            if ( historyTrailBuffer.equals("") )
                historyTrailBuffer=trailSource.getItemAt(trailLooper).toString();
            else
                historyTrailBuffer=historyTrailBuffer+"|"+trailSource.getItemAt(trailLooper).toString();            
        }
        if ( sbTabInstance.scourMethod==SEARCH_METHOD ) {
            fileioClassInstance.storeDataToFile("_hosts.dat", "searchlisthistory"+String.valueOf(groupNumber), "", historyTrailBuffer,1,1,"",false);
            fileioClassInstance.storeDataToFile("_hosts.dat", "searchstartlocationhistory"+String.valueOf(groupNumber), "", sbTabInstance.startLocation.getText(),1,1,"",false);
            
        } else if ( sbTabInstance.scourMethod==BROWSE_METHOD )
            fileioClassInstance.storeDataToFile("_hosts.dat", "activitylisthistorytrail"+String.valueOf(groupNumber), "", historyTrailBuffer,1,1,"",false);
        
    }     
   
    public final void closeAll( final boolean die ) {
        final ceefee.display.sb sbTabInstance=getSelectedSbTab();;
        
        fileioClassInstance.storeDataToFile("_user.dat", "lastmethod","",String.valueOf(scourMethod0), 1, 1, "", false);

        // Activity Tab
        fileioClassInstance.storeDataToFile("_user.dat", "viewtabhorizontalsplitpanedividerlocation","",String.valueOf(viewTab.horizontalSplitPane.getDividerLocation()), 1, 1, "", false);
        //
        
        // Browse Tab
        fileioClassInstance.storeDataToFile("_user.dat", "sbtabmaindividerlocation","",String.valueOf(sbTabInstance.mainSplitPane.getDividerLocation()), 1, 1, "", false);
        fileioClassInstance.storeDataToFile("_user.dat", "showsbtabadvancedview","",sbTabInstance.isBrowseAdvancedViewShown(), 1, 1, "", false);
        //
        
        fileioClassInstance.storeDataToFile("_user.dat", "x-position","",String.valueOf(getX()), 1, 1, "", false);
        fileioClassInstance.storeDataToFile("_user.dat", "y-position","",String.valueOf(getY()), 1, 1, "", false);
        fileioClassInstance.storeDataToFile("_user.dat", "width","",String.valueOf(getWidth()), 1, 1, "", false);
        fileioClassInstance.storeDataToFile("_user.dat", "height","",String.valueOf(getHeight()), 1, 1, "", false);
        fileioClassInstance.storeDataToFile("_user.dat", "extendedstate","",String.valueOf(getExtendedState()), 1, 1, "", false);
        
        if ( die ) System.exit(0);
    }
    
    public void organize( final int scourMethod, int _sbTabIndex, final boolean overrideLock ) {

        if ( isEnabled()==false ) {
            return;
        }
        if ( lockOrganize )
            return;
        else
            lockOrganize=true;

        int halfOfTopRimWidth;

        
        

        if ( getWidth()<MINIMUM_WIDTH ) {
            setSize(MINIMUM_WIDTH, getHeight());
            validate();
        }
        if ( getHeight()<MINIMUM_HEIGHT ) {
            setSize(getWidth(), MINIMUM_HEIGHT);
            validate();
        }

        mainPanel.setSize(getContentPane().getWidth(),getContentPane().getHeight());

        if ( getContentPane().getWidth()>479 )
            topPanel.setSize(getContentPane().getWidth(),topPanel.getHeight());
        else
            topPanel.setSize(479,topPanel.getHeight());
      
        gradientRim.setSize(topPanel.getWidth(),gradientRim.getHeight());
        
        halfOfTopRimWidth=((int)(gradientRim.getWidth()/2));

        if ( graphicsMode==1 ) {
            cgradientClassInstance=new ceefee.display.cgradient(0,0,default1,default3);
            cgradientClassInstance.setSize(halfOfTopRimWidth,gradientRim.getHeight());
            gradientRim.removeAll();
            gradientRim.add(cgradientClassInstance);

            cgradientClassInstance=new ceefee.display.cgradient(halfOfTopRimWidth,0,default3,default1);//topRimGradientClassInstance=new topRimGradientClass(halfOfTopRimWidth,0,blue1,blue2);
            cgradientClassInstance.setSize(gradientRim.getWidth(),gradientRim.getHeight());
            gradientRim.add(cgradientClassInstance);
        }
        
        bottomRim.setLocation(0,getContentPane().getHeight()-bottomRim.getHeight());
        bottomRim.setSize(getContentPane().getWidth(), bottomRim.getHeight());
        bottomRim.validate();
        bottomRim.repaint();
        
        if ( viewTab.isVisible() ) {
            securityLayerLabel.setVisible(false);
            
            viewTab.setSize(getContentPane().getWidth()+2, getContentPane().getHeight()-viewTab.getY()-bottomRim.getHeight());
            viewTab.organize();
            
        } else if ( scheduleTab.isVisible() ) {
            securityLayerLabel.setVisible(false);
            
            scheduleTab.setSize(getContentPane().getWidth()+4, getContentPane().getHeight()-scheduleTab.getY()-bottomRim.getHeight());
            scheduleTab.organize();
            
        } else if ( scourMethod==SEARCH_METHOD ) {
            if ( graphicsMode==1 ) securityLayerLabel.setVisible(true);
            
            if ( _sbTabIndex>searchTabPane.getTabCount() )
                _sbTabIndex=searchTabPane.getTabCount();

            if ( searchTab!=null && searchTab.length>=_sbTabIndex ) {
                securityLayerLabel.setLocation(getContentPane().getWidth()-securityLayerLabel.getWidth(), topMargin);

                searchTabPane.setSize(getContentPane().getWidth()+5, bottomRim.getY()-searchTabPane.getY()+2);
            }
            
        } else if ( scourMethod==BROWSE_METHOD ) {
            if ( graphicsMode==1 ) securityLayerLabel.setVisible(true);
            
            if ( _sbTabIndex>browseTabPane.getTabCount() )
                _sbTabIndex=browseTabPane.getTabCount();

            if ( browseTab!=null && browseTab.length>=_sbTabIndex ) {
                securityLayerLabel.setLocation(getContentPane().getWidth()-securityLayerLabel.getWidth(), topMargin);

                browseTabPane.setSize(getContentPane().getWidth()+5, bottomRim.getY()-browseTabPane.getY()+2);
            }
            
        }

        lockOrganize=false;

    }

    private void initComponents() {//GEN-BEGIN:initComponents
        tabMenu = new javax.swing.JPopupMenu();
        closeTabMenuItem = new javax.swing.JMenuItem();
        mainPanel = new javax.swing.JPanel();
        topPanel = new javax.swing.JPanel();
        logoLabel = new javax.swing.JLabel();
        securityLayerLabel = new javax.swing.JLabel();
        searchIconLabel = new javax.swing.JLabel();
        browseIconLabel = new javax.swing.JLabel();
        viewIconLabel = new javax.swing.JLabel();
        scheduleIconLabel = new javax.swing.JLabel();
        configureIconLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        gradientRim = new javax.swing.JLabel();
        bottomRim = new javax.swing.JLabel();
        searchTabPane = new javax.swing.JTabbedPane();
        browseTabPane = new javax.swing.JTabbedPane();

        closeTabMenuItem.setText("Close Tab");
        closeTabMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeTabMenuItemActionPerformed(evt);
            }
        });

        tabMenu.add(closeTabMenuItem);

        getContentPane().setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        mainPanel.setLayout(null);

        mainPanel.setDoubleBuffered(false);
        topPanel.setLayout(null);

        topPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        topPanel.setDoubleBuffered(false);
        topPanel.setOpaque(false);
        logoLabel.setFont(new java.awt.Font("Lucida Bright", 1, 16));
        logoLabel.setForeground(new java.awt.Color(255, 255, 255));
        logoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoLabelMouseClicked(evt);
            }
        });

        topPanel.add(logoLabel);
        logoLabel.setBounds(7, 3, 270, 30);

        topPanel.add(securityLayerLabel);
        securityLayerLabel.setBounds(580, 10, 150, 16);

        searchIconLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
        searchIconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        searchIconLabel.setText("Search");
        searchIconLabel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        searchIconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchIconLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchIconLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchIconLabelMouseExited(evt);
            }
        });

        topPanel.add(searchIconLabel);
        searchIconLabel.setBounds(35, 45, 90, 20);

        browseIconLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
        browseIconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        browseIconLabel.setText("Browse");
        browseIconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                browseIconLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                browseIconLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                browseIconLabelMouseExited(evt);
            }
        });

        topPanel.add(browseIconLabel);
        browseIconLabel.setBounds(165, 45, 90, 20);

        viewIconLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
        viewIconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        viewIconLabel.setText("View");
        viewIconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewIconLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewIconLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewIconLabelMouseExited(evt);
            }
        });

        topPanel.add(viewIconLabel);
        viewIconLabel.setBounds(295, 45, 90, 20);

        scheduleIconLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
        scheduleIconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        scheduleIconLabel.setText("Schedule");
        scheduleIconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scheduleIconLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                scheduleIconLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                scheduleIconLabelMouseExited(evt);
            }
        });

        topPanel.add(scheduleIconLabel);
        scheduleIconLabel.setBounds(425, 45, 90, 20);

        configureIconLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
        configureIconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        configureIconLabel.setText("Configure");
        configureIconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                configureIconLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                configureIconLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                configureIconLabelMouseExited(evt);
            }
        });

        topPanel.add(configureIconLabel);
        configureIconLabel.setBounds(555, 45, 90, 20);

        jLabel1.setIcon(new javax.swing.ImageIcon(""));
        topPanel.add(jLabel1);
        jLabel1.setBounds(3, 49, 30, 0);

        gradientRim.setOpaque(true);
        topPanel.add(gradientRim);
        gradientRim.setBounds(0, 0, 530, 40);

        mainPanel.add(topPanel);
        topPanel.setBounds(0, 0, 800, 70);

        bottomRim.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        bottomRim.setOpaque(true);
        mainPanel.add(bottomRim);
        bottomRim.setBounds(0, 710, 580, 2);

        searchTabPane.setAlignmentX(0.0F);
        searchTabPane.setAlignmentY(0.0F);
        searchTabPane.setFocusable(false);
        searchTabPane.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                searchTabPaneComponentAdded(evt);
            }
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                searchTabPaneComponentRemoved(evt);
            }
        });
        searchTabPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchTabPaneMouseClicked(evt);
            }
        });

        mainPanel.add(searchTabPane);
        searchTabPane.setBounds(-70, 100, 640, 600);

        browseTabPane.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                browseTabPaneComponentAdded(evt);
            }
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                browseTabPaneComponentRemoved(evt);
            }
        });
        browseTabPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                browseTabPaneMouseClicked(evt);
            }
        });

        mainPanel.add(browseTabPane);
        browseTabPane.setBounds(-70, 100, 500, 500);

        getContentPane().add(mainPanel);
        mainPanel.setBounds(0, 0, 740, 830);

        pack();
    }//GEN-END:initComponents

    private void browseTabPaneComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_browseTabPaneComponentRemoved
        final ceefee.display.sb sbTabInstance=getSelectedSbTab();
        if ( (sbTabInstance.tabIndex=browseTabPane.getSelectedIndex())==-1 )
            sbTabInstance.tabIndex=1;
        organize(sbTabInstance.scourMethod,sbTabInstance.tabIndex,false);
        
    }//GEN-LAST:event_browseTabPaneComponentRemoved

    private void browseTabPaneComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_browseTabPaneComponentAdded
        browseTab[browseTabPane.getTabCount()-1].refreshFtpSettings();
    }//GEN-LAST:event_browseTabPaneComponentAdded

    private void browseTabPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_browseTabPaneMouseClicked
        final ceefee.display.sb sbTabInstance=browseTab[browseTabPane.getSelectedIndex()];
        
        if ( evt.getButton()==evt.BUTTON3 && sbTabInstance.tabIndex>0 ) {
            if ( sbTabInstance.busy==true )
                closeTabMenuItem.setEnabled(false);
            else
                closeTabMenuItem.setEnabled(true);
            tabMenu.show(browseTabPane,evt.getX(),evt.getY());
            tabMenu.repaint();
        } else {
            organize(sbTabInstance.scourMethod,sbTabInstance.tabIndex,false);
            if ( sbTabInstance.hostDescription.isEnabled() ) {
               sbTabInstance.refreshFtpSettings();
            }
        }
    }//GEN-LAST:event_browseTabPaneMouseClicked

    private void viewIconLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewIconLabelMouseExited
        viewIconLabel.setIcon(null);
    }//GEN-LAST:event_viewIconLabelMouseExited

    private void configureIconLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_configureIconLabelMouseExited
        configureIconLabel.setIcon(null);
    }//GEN-LAST:event_configureIconLabelMouseExited

    private void scheduleIconLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scheduleIconLabelMouseExited
        scheduleIconLabel.setIcon(null);
    }//GEN-LAST:event_scheduleIconLabelMouseExited

    private void browseIconLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_browseIconLabelMouseExited
        browseIconLabel.setIcon(null);
    }//GEN-LAST:event_browseIconLabelMouseExited

    private void searchIconLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchIconLabelMouseExited
        searchIconLabel.setIcon(null);
    }//GEN-LAST:event_searchIconLabelMouseExited

    private void configureIconLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_configureIconLabelMouseClicked
        showPanel(CONFIGURE_PANEL);
    }//GEN-LAST:event_configureIconLabelMouseClicked

    private void configureIconLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_configureIconLabelMouseEntered
        if ( graphicsMode==1 ) configureIconLabel.setIcon(configureIcon);
    }//GEN-LAST:event_configureIconLabelMouseEntered

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        new Thread(new ceefee.misc.processclipboarddata(this,getSelectedSbTab())).start();
        
    }//GEN-LAST:event_formWindowActivated

    private void scheduleIconLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scheduleIconLabelMouseClicked
        showPanel(SCHEDULE_PANEL);
    }//GEN-LAST:event_scheduleIconLabelMouseClicked

    private void searchTabPaneComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_searchTabPaneComponentAdded
        searchTab[searchTabPane.getTabCount()-1].refreshFtpSettings();
    }//GEN-LAST:event_searchTabPaneComponentAdded

    private void searchTabPaneComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_searchTabPaneComponentRemoved
        final ceefee.display.sb sbTabInstance=getSelectedSbTab();
        if ( (sbTabInstance.tabIndex=searchTabPane.getSelectedIndex())==-1 )
            sbTabInstance.tabIndex=1;
            organize(sbTabInstance.scourMethod,sbTabInstance.tabIndex,false);
        
    }//GEN-LAST:event_searchTabPaneComponentRemoved

    private void closeTabMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeTabMenuItemActionPerformed
        if ( closeTabMenuItem.isEnabled()==false ) return;

        removeTabPaneTab(scourMethod0,searchTabPane.getSelectedIndex());
        
    }//GEN-LAST:event_closeTabMenuItemActionPerformed

    private void searchTabPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchTabPaneMouseClicked
        final ceefee.display.sb sbTabInstance=getSelectedSbTab();;

        if ( evt.getButton()==evt.BUTTON3 && sbTabInstance.tabIndex>0 ) {
            if ( sbTabInstance.busy==true )
                closeTabMenuItem.setEnabled(false);
            else
                closeTabMenuItem.setEnabled(true);
            tabMenu.show(searchTabPane,evt.getX(),evt.getY());
            tabMenu.repaint();
        } else {
            organize(sbTabInstance.scourMethod,sbTabInstance.tabIndex,false);
            if ( sbTabInstance.hostDescription.isEnabled() ) {
               sbTabInstance.refreshFtpSettings();
            }
        }
        
    }//GEN-LAST:event_searchTabPaneMouseClicked

    private void searchIconLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchIconLabelMouseClicked
        showPanel(SEARCH_PANEL);
    }//GEN-LAST:event_searchIconLabelMouseClicked

    private void searchIconLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchIconLabelMouseEntered
        if ( graphicsMode==1 ) searchIconLabel.setIcon(searchIcon);
    }//GEN-LAST:event_searchIconLabelMouseEntered

    private void logoLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoLabelMouseClicked
    }//GEN-LAST:event_logoLabelMouseClicked

    private void viewIconLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewIconLabelMouseClicked
        showPanel(VIEW_PANEL);
    }//GEN-LAST:event_viewIconLabelMouseClicked

    private void scheduleIconLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scheduleIconLabelMouseEntered
        if ( graphicsMode==1 ) scheduleIconLabel.setIcon(scheduleIcon);
    }//GEN-LAST:event_scheduleIconLabelMouseEntered

    private void viewIconLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewIconLabelMouseEntered
        if ( graphicsMode==1 ) viewIconLabel.setIcon(viewIcon);
    }//GEN-LAST:event_viewIconLabelMouseEntered

    private void browseIconLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_browseIconLabelMouseEntered
        if ( graphicsMode==1 ) browseIconLabel.setIcon(browseIcon);
    }//GEN-LAST:event_browseIconLabelMouseEntered

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if ( evt.getComponent().getName().equals("frame0")==false ) return;
        
        organize(0,searchTabPane.getSelectedIndex(),false);
        organize(1,browseTabPane.getSelectedIndex(),false);
        
    }//GEN-LAST:event_formComponentResized
     
    private void browseIconLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_browseIconLabelMouseClicked
        showPanel(BROWSE_PANEL);
    }//GEN-LAST:event_browseIconLabelMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        closeAll(true);
        
    }//GEN-LAST:event_formWindowClosing
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bottomRim;
    public javax.swing.JLabel browseIconLabel;
    public javax.swing.JTabbedPane browseTabPane;
    private javax.swing.JMenuItem closeTabMenuItem;
    public javax.swing.JLabel configureIconLabel;
    private javax.swing.JLabel gradientRim;
    private javax.swing.JLabel jLabel1;
    public javax.swing.JLabel logoLabel;
    public javax.swing.JPanel mainPanel;
    public javax.swing.JLabel scheduleIconLabel;
    public javax.swing.JLabel searchIconLabel;
    public javax.swing.JTabbedPane searchTabPane;
    public javax.swing.JLabel securityLayerLabel;
    private javax.swing.JPopupMenu tabMenu;
    private javax.swing.JPanel topPanel;
    public javax.swing.JLabel viewIconLabel;
    // End of variables declaration//GEN-END:variables
    
}