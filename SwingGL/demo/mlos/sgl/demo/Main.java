package mlos.sgl.demo;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import mlos.sgl.MainWindow;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow w = new MainWindow(1, 1);
                w.pack();
                w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                w.setVisible(true);
            }
        });
    }

}
