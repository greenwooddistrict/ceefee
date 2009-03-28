package ceefee.display;


public class schedule_dataentry extends javax.swing.JFrame {
    
    private ceefee.main mainFrameRef;
    private ceefee.display.schedule scheduleInstance;
    
    final int HEIGHT            =390+25;
    final int WIDTH             =245+15;
    
    final java.util.Calendar ci=java.util.Calendar.getInstance();

    int oldItemIndex;
    
    long timeFormOpened;
    
    
    public schedule_dataentry( final ceefee.main _mainFrameRef, final ceefee.display.schedule _scheduleInstance, final int _oldItemIndex, final String _taskDescription, final String _hostAddress, final String _hostPort, final String _hostUsername, final String _hostPassword, final String _protocol, final String _dataFormat, final String _connectionMethod, final String _remoteObjectPath, final String _localObjectPath, final String _operation, final long _date, final String _frequency ) {
        mainFrameRef=_mainFrameRef;
        scheduleInstance=_scheduleInstance;
        oldItemIndex=_oldItemIndex;
        java.util.Calendar _ci=java.util.Calendar.getInstance();
        timeFormOpened=_ci.getTimeInMillis();
        String am_pm;
        String hour;
        String minute=String.valueOf(java.util.Calendar.getInstance().get(ci.MINUTE));
        if ( minute.length()==1 )
            minute="0"+minute;

        initComponents();
        setSize(WIDTH, HEIGHT);
        repaint();
        setLocation(((mainFrameRef.getContentPane().getWidth()-getWidth())/2)+mainFrameRef.getX(), (int)((mainFrameRef.getContentPane().getHeight()-getHeight())/4)+mainFrameRef.getY());

        setIconImage(mainFrameRef.getIconImage());
        
        cruxPanel.setSize(getWidth(), getHeight());
        cruxPanel.setBackground(mainFrameRef.default4);
        
        scheduleItLabel.setCursor(mainFrameRef.handCursor);
        scheduleItLabel.setForeground(mainFrameRef.default1);
        
        cancelLabel.setCursor(mainFrameRef.handCursor);
        cancelLabel.setForeground(mainFrameRef.default1);
       
        dataFormat.setBackground(java.awt.Color.white);
        protocol.setBackground(java.awt.Color.white);
        scheduleItemFrequency.setBackground(java.awt.Color.white);
        scheduleItemOperation.setBackground(java.awt.Color.white);
        
        scheduleItemDescription.setText(_taskDescription);
        hostAddress.setText(_hostAddress);
        hostPort.setText(_hostPort);
        hostUsername.setText(_hostUsername);
        hostPassword.setText(_hostPassword);
        protocol.setSelectedItem(_protocol);
        dataFormat.setSelectedItem(_dataFormat);
        //connectionMethod.setSelectedItem(_connectionMethod);
        scheduleItemRemotePath.setText(_remoteObjectPath);
        scheduleItemLocalPath.setText(_localObjectPath);
        scheduleItemOperation.setSelectedItem(_operation);
        if ( _date==0 ) {
            scheduleItemDate1.setText(java.util.Calendar.getInstance().get(ci.MONTH)+"/"+java.util.Calendar.getInstance().get(ci.DAY_OF_MONTH)+"/"+java.util.Calendar.getInstance().get(ci.YEAR));
            
        } else {
            _ci.setTimeInMillis(_date);
            scheduleItemDate1.setText(_ci.get(ci.MONTH)+"/"+_ci.get(ci.DAY_OF_MONTH)+"/"+_ci.get(ci.YEAR));
            
            minute=String.valueOf(_ci.get(ci.MINUTE));
            if ( minute.length()==1 )
                minute="0"+minute;
                        
        }
        if ( java.util.Calendar.getInstance().get(ci.AM_PM)==ci.PM )
            am_pm="PM";
        else
            am_pm="AM";

        hour=String.valueOf(java.util.Calendar.getInstance().get(ci.HOUR));
        if ( hour.length()==1 )
            hour="0"+hour;
        if ( hour.equals("00") )
            hour="12";
            
        scheduleItemTime1.setText(hour+":"+minute+" "+am_pm);
        
        scheduleItemFrequency.setSelectedItem(_frequency);

        repaint();
        
    }

    private void close() {
        mainFrameRef.setVisible(true);
        hide();
        dispose();
    }
    
    private void initComponents() {//GEN-BEGIN:initComponents
        cruxPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        scheduleItemDescription = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        scheduleItemTime1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        scheduleItemOperation = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        scheduleItemDate1 = new javax.swing.JTextField();
        localPathLabel = new javax.swing.JLabel();
        scheduleItemRemotePath = new javax.swing.JTextField();
        cancelLabel = new javax.swing.JLabel();
        scheduleItLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        scheduleItemFrequency = new javax.swing.JComboBox();
        hostAddress = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        hostPort = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        hostPassword = new javax.swing.JPasswordField();
        jLabel10 = new javax.swing.JLabel();
        hostUsername = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        protocol = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        dataFormat = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        scheduleItemLocalPath = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CeeFee - Scheduler");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        cruxPanel.setLayout(null);

        jLabel5.setText("Description");
        cruxPanel.add(jLabel5);
        jLabel5.setBounds(15, 5, 100, 20);

        cruxPanel.add(scheduleItemDescription);
        scheduleItemDescription.setBounds(15, 25, 210, 21);

        jLabel7.setText("Run Date");
        cruxPanel.add(jLabel7);
        jLabel7.setBounds(15, 290, 80, 15);

        cruxPanel.add(scheduleItemTime1);
        scheduleItemTime1.setBounds(90, 305, 70, 20);

        jLabel6.setText("Frequency");
        cruxPanel.add(jLabel6);
        jLabel6.setBounds(165, 290, 70, 15);

        scheduleItemOperation.setFont(new java.awt.Font("Dialog", 0, 12));
        scheduleItemOperation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Download and Execute", "Download", "Upload" }));
        scheduleItemOperation.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 0));
        scheduleItemOperation.setFocusable(false);
        cruxPanel.add(scheduleItemOperation);
        scheduleItemOperation.setBounds(15, 265, 170, 20);

        jLabel4.setText("Operation");
        cruxPanel.add(jLabel4);
        jLabel4.setBounds(15, 250, 100, 15);

        cruxPanel.add(scheduleItemDate1);
        scheduleItemDate1.setBounds(15, 305, 70, 20);

        localPathLabel.setText("Local Path");
        cruxPanel.add(localPathLabel);
        localPathLabel.setBounds(15, 210, 100, 15);

        cruxPanel.add(scheduleItemRemotePath);
        scheduleItemRemotePath.setBounds(15, 185, 170, 20);

        cancelLabel.setText("<HTML><U>Cancel</U></HTML>");
        cancelLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelLabelMouseClicked(evt);
            }
        });

        cruxPanel.add(cancelLabel);
        cancelLabel.setBounds(180, 360, 40, 15);

        scheduleItLabel.setText("<HTML><U>Schedule It</U></HTML>");
        scheduleItLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scheduleItLabelMouseClicked(evt);
            }
        });

        cruxPanel.add(scheduleItLabel);
        scheduleItLabel.setBounds(100, 360, 70, 15);

        jLabel9.setText("Run Time");
        cruxPanel.add(jLabel9);
        jLabel9.setBounds(90, 290, 70, 15);

        scheduleItemFrequency.setFont(new java.awt.Font("Dialog", 0, 12));
        scheduleItemFrequency.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Once", "Daily", "Weekly", "Monthly" }));
        scheduleItemFrequency.setFocusable(false);
        scheduleItemFrequency.setOpaque(false);
        cruxPanel.add(scheduleItemFrequency);
        scheduleItemFrequency.setBounds(165, 305, 70, 20);

        cruxPanel.add(hostAddress);
        hostAddress.setBounds(15, 65, 140, 21);

        jLabel3.setText("Host Address");
        cruxPanel.add(jLabel3);
        jLabel3.setBounds(15, 50, 100, 15);

        cruxPanel.add(hostPort);
        hostPort.setBounds(160, 65, 65, 21);

        jLabel8.setText("Host Port");
        cruxPanel.add(jLabel8);
        jLabel8.setBounds(160, 50, 60, 15);

        cruxPanel.add(hostPassword);
        hostPassword.setBounds(125, 105, 100, 22);

        jLabel10.setText("Password");
        cruxPanel.add(jLabel10);
        jLabel10.setBounds(125, 90, 110, 15);

        cruxPanel.add(hostUsername);
        hostUsername.setBounds(15, 105, 105, 22);

        jLabel11.setText("Username");
        cruxPanel.add(jLabel11);
        jLabel11.setBounds(15, 90, 110, 15);

        jLabel12.setText("Protocol");
        cruxPanel.add(jLabel12);
        jLabel12.setBounds(15, 130, 110, 15);

        protocol.setFont(new java.awt.Font("Dialog", 0, 12));
        protocol.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Standard FTP" }));
        protocol.setFocusable(false);
        cruxPanel.add(protocol);
        protocol.setBounds(15, 145, 105, 22);

        jLabel13.setText("Data Format");
        cruxPanel.add(jLabel13);
        jLabel13.setBounds(125, 130, 110, 15);

        dataFormat.setFont(new java.awt.Font("Dialog", 0, 12));
        dataFormat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Binary", "ASCII" }));
        dataFormat.setFocusable(false);
        cruxPanel.add(dataFormat);
        dataFormat.setBounds(125, 145, 105, 22);

        jLabel15.setText("Remote Path");
        cruxPanel.add(jLabel15);
        jLabel15.setBounds(15, 170, 100, 15);

        cruxPanel.add(scheduleItemLocalPath);
        scheduleItemLocalPath.setBounds(15, 225, 170, 20);

        getContentPane().add(cruxPanel);
        cruxPanel.setBounds(0, 0, 245, 390);

    }//GEN-END:initComponents

    private void scheduleItLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scheduleItLabelMouseClicked
        
        Object[] scheduledItemData=null;
        int whimsicalByteLocation;

        int minuteDifference;
        int hourDifference;
        int timeOfDayDifference;
        int dayOfMonthDifference;
        int monthDifference;
        int yearDifference;
        
        String rawDate;
        String rawTime;
        int month;
        int dayOfMonth;
        int year;
        int hour;
        int minute;
        String timeOfDay;

        long millisecondDifference=0;
        
        String selectedFrequency;

        java.util.Calendar _ci=java.util.Calendar.getInstance();
        
        if ( mainFrameRef.utilitiesClassInstance.findItemInTable(scheduleItemDescription.getText(),scheduleInstance.scheduleTable)!=-1 ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef,"This item already exists in the scheduler.\n If you would like to add an aditional item, you must use a unique description.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if ( scheduleItemDescription.getText().equals("") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "No description for the scheduled task was specified.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        scheduleItemDescription.setText(scheduleItemDescription.getText().replaceAll("[|]"," "));
        scheduleItemDescription.setText(scheduleItemDescription.getText().replaceAll("[:]"," "));

        if ( hostAddress.getText().equals("") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The host address field was left blank.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        hostAddress.setText(hostAddress.getText().replaceAll("[|]"," "));
        hostAddress.setText(hostAddress.getText().replaceAll("[:]"," "));

        if ( hostPort.getText().equals("") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The host port field was left blank.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        } else {
            try {
                Integer.parseInt(hostPort.getText());
            } catch ( java.lang.NumberFormatException nfe ) {
                javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "Invalid value specified for the host port. The host port must be an integer value between: '0' and '65536'.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        if ( hostUsername.getText().equals("") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The host username field was left blank.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        hostUsername.setText(hostUsername.getText().replaceAll("[|]"," "));
        hostUsername.setText(hostUsername.getText().replaceAll("[:]"," "));

        if ( new String(hostPassword.getPassword()).equals("") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The host password field was left blank.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        hostPassword.setText(new String(hostPassword.getPassword()).replaceAll("[|]"," "));
        hostPassword.setText(new String(hostPassword.getPassword()).replaceAll("[:]"," "));
        
        if ( scheduleItemRemotePath.getText().equals("") ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The remote object path field was left blank.","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        scheduleItemRemotePath.setText(scheduleItemRemotePath.getText().replaceAll("[|]"," "));
        scheduleItemRemotePath.setText(scheduleItemRemotePath.getText().replaceAll("[:]"," "));

        // Date
        if ( scheduleItemDate1.getText().length()>14 || scheduleItemDate1.getText().length()<6 ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The specified date format is incorrect.\nValid date syntax is of the form: MM/DD/YY","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if ( scheduleItemDate1.getText().substring(1,2).equals("/")==false && scheduleItemDate1.getText().substring(2,3).equals("/")==false ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The specified date format is incorrect.\nValid date syntax is of the form: MM/DD/YY","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        whimsicalByteLocation=scheduleItemDate1.getText().indexOf("/")+1;
        if ( scheduleItemDate1.getText().substring(whimsicalByteLocation+1,whimsicalByteLocation+2).equals("/")==false && scheduleItemDate1.getText().substring(whimsicalByteLocation+2,whimsicalByteLocation+3).equals("/")==false ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The specified date format is incorrect.\nValid date syntax is of the form: MM/DD/YY","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            rawDate=scheduleItemDate1.getText();
            whimsicalByteLocation=rawDate.indexOf("/");
            month=Integer.parseInt(rawDate.substring(0,whimsicalByteLocation));
            dayOfMonth=Integer.parseInt(rawDate.substring(whimsicalByteLocation+1,rawDate.indexOf("/",whimsicalByteLocation+1)));
            year=Integer.parseInt(rawDate.substring(rawDate.lastIndexOf("/")+1));
        } catch ( java.lang.NumberFormatException nfe ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The specified date format is incorrect.\nValid date syntax is of the form: MM/DD/YY","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Time
        if ( scheduleItemTime1.getText().length()>10 || scheduleItemTime1.getText().length()<6 ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The specified time format is incorrect.\nValid time syntax is of the form: 'HH:MM AM/PM' [e.g. '12:00 A.M.']","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if ( scheduleItemTime1.getText().substring(1,2).equals(":")==false && scheduleItemTime1.getText().substring(2,3).equals(":")==false ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The specified time format is incorrect.\nValid time syntax is of the form: 'HH:MM AM/PM' [e.g. '12:00 A.M.']","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if ( scheduleItemTime1.getText().substring(4,5).equals(" ")==false && scheduleItemTime1.getText().substring(5,6).equals(" ")==false ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The specified time format is incorrect.\nValid time syntax is of the form: 'HH:MM AM/PM' [e.g. '12:00 A.M.']","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            rawTime=scheduleItemTime1.getText();
            whimsicalByteLocation=rawTime.indexOf(":");
            hour=Integer.parseInt(rawTime.substring(0,whimsicalByteLocation));
            if ( hour==12 )
                hour=00; //revert back from human-readable format
            minute=Integer.parseInt(rawTime.substring(whimsicalByteLocation+1,rawTime.indexOf(" ")));
            timeOfDay=rawTime.substring(rawTime.indexOf(" ")+1).trim();
        } catch ( java.lang.NumberFormatException nfe ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The specified time format is incorrect.\nValid time syntax is of the form: 'HH:MM AM/PM' [e.g. '12:00 A.M.']","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if ( timeOfDay.equals("A.M.") )
            timeOfDay="AM";
        else if ( timeOfDay.equals("P.M.") )
            timeOfDay="PM";
        else if ( timeOfDay.equals("AM")==false && timeOfDay.equals("PM")==false ) {
            javax.swing.JOptionPane.showMessageDialog(mainFrameRef, "The specified time format is incorrect.\nValid time syntax is of the form: 'HH:MM AM/PM' [e.g. '12:00 A.M.']","hey!",javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if ( (java.util.Calendar.getInstance().get(ci.AM_PM)==ci.PM && timeOfDay.equals("AM")) || (java.util.Calendar.getInstance().get(ci.AM_PM)==ci.AM && timeOfDay.equals("PM")) )
            hour+=12;

        millisecondDifference=
        ((year-java.util.Calendar.getInstance().get(ci.YEAR))*365*24*60*60*1000)+
        ((dayOfMonth-java.util.Calendar.getInstance().get(ci.DAY_OF_MONTH))*24*60*60*1000)+
        ((hour-java.util.Calendar.getInstance().get(ci.HOUR))*60*60*1000)+
        ((minute-java.util.Calendar.getInstance().get(ci.MINUTE))*60*1000)
        ;
        
        _ci.setTimeInMillis(java.util.Calendar.getInstance().getTimeInMillis()+(java.util.Calendar.getInstance().getTimeInMillis()-timeFormOpened)+millisecondDifference);

        scheduledItemData=new Object[] {
            scheduleItemDescription.getText(),
            hostAddress.getText(),
            hostPort.getText(),
            hostUsername.getText(),
            new String(hostPassword.getPassword()),
            String.valueOf(protocol.getSelectedIndex()),
            String.valueOf(dataFormat.getSelectedIndex()),
            "0", //String.valueOf(connectionMethod.getSelectedIndex()),
            scheduleItemRemotePath.getText().substring(scheduleItemRemotePath.getText().length()-1,scheduleItemRemotePath.getText().length()).equals("/")?scheduleItemRemotePath.getText().substring(0,scheduleItemRemotePath.getText().length()-1):scheduleItemRemotePath.getText(),
            scheduleItemLocalPath.getText().substring(scheduleItemLocalPath.getText().length()-1,scheduleItemLocalPath.getText().length()).equals("\\")?scheduleItemLocalPath.getText().substring(0,scheduleItemLocalPath.getText().length()-1):scheduleItemLocalPath.getText(),
            scheduleItemOperation.getSelectedItem(),
            String.valueOf(_ci.getTimeInMillis()),
            scheduleItemFrequency.getSelectedItem()
        };

        if ( oldItemIndex!=-1 ) {
            ((java.util.Timer)scheduleInstance.timers.get(oldItemIndex)).cancel();
            scheduleInstance.timers.remove(oldItemIndex);
            scheduleInstance.scheduledItems.remove(oldItemIndex);
            scheduleInstance.scheduleTableDataModel.removeRow(oldItemIndex);
        }

        scheduleInstance.addNewTask(scheduledItemData);
            
        close();

    }//GEN-LAST:event_scheduleItLabelMouseClicked

    private void cancelLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelLabelMouseClicked
        close();
    }//GEN-LAST:event_cancelLabelMouseClicked

    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        close();
    }//GEN-LAST:event_exitForm
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cancelLabel;
    private javax.swing.JPanel cruxPanel;
    public javax.swing.JComboBox dataFormat;
    private javax.swing.JTextField hostAddress;
    public javax.swing.JPasswordField hostPassword;
    private javax.swing.JTextField hostPort;
    public javax.swing.JTextField hostUsername;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel localPathLabel;
    public javax.swing.JComboBox protocol;
    private javax.swing.JLabel scheduleItLabel;
    private javax.swing.JTextField scheduleItemDate1;
    private javax.swing.JTextField scheduleItemDescription;
    private javax.swing.JComboBox scheduleItemFrequency;
    private javax.swing.JTextField scheduleItemLocalPath;
    private javax.swing.JComboBox scheduleItemOperation;
    private javax.swing.JTextField scheduleItemRemotePath;
    private javax.swing.JTextField scheduleItemTime1;
    // End of variables declaration//GEN-END:variables
    
}