package mlos.sgl;

import javax.swing.SwingUtilities;

public class App {

    private App() {
        // non-instantiable
    }
    
    public static void create(final Scene... scenes) {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                MainWindow window = new MainWindow();
                for (Scene scene : scenes) {
                    window.addScene(scene);
                }
            }
        });
    }

}
