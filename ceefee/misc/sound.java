
package ceefee.misc;


import java.io.File;


public class sound extends Thread {
                
    public void playSound( final String _filePath ) {
        class play extends Thread {
            public void run() {
                if ( this.getClass().getClassLoader().getResource("ceefee/"+_filePath)!=null ) java.applet.Applet.newAudioClip(this.getClass().getClassLoader().getResource("ceefee/"+_filePath)).play();
            }
        }
        new Thread(new play()).start();
    }
}
