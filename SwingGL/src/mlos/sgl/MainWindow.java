package mlos.sgl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindow extends JFrame {
    
    public static final int WIDTH = 900;
    public static final int HEIGHT = 700;
    
    private JTabbedPane panel;
    private JPanel sidePanel;
    
    private final List<Scene> scenes = new ArrayList<>();
    
    
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
        setLayout(new BorderLayout(5, 4));
        
        panel = new JTabbedPane();
        panel.setBorder(BorderFactory.createEmptyBorder(3, 1, 3, 3));
        panel.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                int n = panel.getSelectedIndex();
                Scene scene = scenes.get(n);
                sidePanel.removeAll();
                sidePanel.add(scene.getSideGui());
                sidePanel.invalidate();
            }
        });
        
        sidePanel = new JPanel(new BorderLayout());
        add(panel);
        add(sidePanel, BorderLayout.LINE_START);
        pack();
    }
    
    private void setupPosition() {
        setLocationByPlatform(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
    
    public void addScene(Scene scene) {
        String title = scene.getName();
        scenes.add(scene);
        panel.addTab(title, scene.getCanvasGui());
    }
    
    public void removeScene(Scene scene) {
        scenes.remove(scene);
        panel.remove(scene.getCanvasGui());
    }
    
}
