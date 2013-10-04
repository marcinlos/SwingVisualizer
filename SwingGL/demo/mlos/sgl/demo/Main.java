package mlos.sgl.demo;

import javax.swing.SwingUtilities;

import mlos.sgl.MainWindow;
import mlos.sgl.Scene;

public class Main {
    
    public static void setup() {
        MainWindow window = new MainWindow(1, 1);
        window.addScene(new Scene("Test") {
            
        });
        
        window.addScene(new Scene("Demo") {
            
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setup();
            }
        });
    }

}
