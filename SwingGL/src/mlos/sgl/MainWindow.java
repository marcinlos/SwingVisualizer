package mlos.sgl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import mlos.sgl.view.CanvasPanel;
import mlos.sgl.view.Painter;
import mlos.sgl.view.Point;

public class MainWindow extends JFrame {
    
    public static final int WIDTH = 600;
    public static final int HEIGHT = 500;
    
    private final CanvasPanel canvasPanel;

    public MainWindow(double width, double height) {
        super("Visualizer");
        
        final double w = width;
        final double h = height;
        this.canvasPanel = new CanvasPanel(new Painter() {
            @Override
            public void paint(CanvasPanel canvas, Graphics2D ctx) {
                double x = w / 2;
                double y = h / 2;
                Point p = canvas.toScreen(x, y);
                ctx.drawString("Test", p.x, p.y);
            }
        }, width, height);
        
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
