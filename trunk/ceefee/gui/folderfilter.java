
package ceefee.gui;


import java.io.File;


public class folderfilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File f) {
        return f.isDirectory();
    }
    public String getDescription() {
        return "n/a";
    }
};
