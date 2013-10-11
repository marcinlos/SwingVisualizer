package mlos.sgl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainWindow extends JFrame {
    
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    
    private JTabbedPane panel;
    
    private final Collection<Scene> scenes = new HashSet<>();
    
    
    public MainWindow() {
        this(WIDTH, HEIGHT);
    }

    public MainWindow(int width, int height) {
        super("Visualizer");
        
        setupUI();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setupUI() {
        setupPosition();
        setLayout(new BorderLayout());
        
        panel = new JTabbedPane();
        add(panel);
        pack();
    }
    
    private void setupPosition() {
        setLocationByPlatform(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
    
    public void addScene(Scene scene) {
        String title = scene.getName();
        scenes.add(scene);
        panel.addTab(title, scene.getUI());
    }
    
    public void removeScene(Scene scene) {
        scenes.remove(scene);
        panel.remove(scene.getUI());
    }
    
}
