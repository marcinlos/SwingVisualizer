package mlos.sgl;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import mlos.sgl.model.Canvas;
import mlos.sgl.view.CanvasPainter;
import mlos.sgl.view.CanvasPanel;
import mlos.sgl.view.Painter;

public class MainWindow extends JFrame {
    
    public static final int WIDTH = 600;
    public static final int HEIGHT = 500;
    
    private final CanvasPanel canvasPanel;
    
    private final Canvas canvas = new Canvas();
    
    private Painter painter = new CanvasPainter(canvas);

    public MainWindow(double width, double height) {
        super("Visualizer");
        
        canvasPanel = new CanvasPanel(painter, width, height);
        setupUI();
        
        
    }

    private void setupUI() {
        setupPosition();
        setLayout(new BorderLayout());
        add(canvasPanel.swingPanel());
    }
    
    private void setupPosition() {
        setLocationByPlatform(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
    
    public CanvasPanel getCanvasPanel() {
        return canvasPanel;
    }
    
}
