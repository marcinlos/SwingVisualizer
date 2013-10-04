package mlos.sgl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Component;

import javax.swing.JPanel;

import mlos.sgl.model.Canvas;
import mlos.sgl.view.CanvasPainter;
import mlos.sgl.view.CanvasPanel;
import mlos.sgl.view.Painter;

public abstract class Scene {
    
    private String name;
    
    private final Canvas canvas;
    
    private final Painter painter;
    
    private final CanvasPanel canvasPanel;

    public Scene(String name) {
        this(name, 1, 1);
    }
    
    public Scene(String name, double width, double height) {
        this.name = checkNotNull(name);
        this.canvas = new Canvas();
        this.painter = new CanvasPainter(canvas);
        this.canvasPanel = new CanvasPanel(painter, width, height);
    }
    
    public String getName() {
        return name;
    }
    
    protected Canvas canvas() {
        return canvas;
    }
    
    protected CanvasPanel canvasPanel() {
        return canvasPanel;
    }
    
    protected JPanel panel() {
        return canvasPanel.swingPanel();
    }
    
    protected void refresh() {
        canvasPanel.refresh();
    }
    
    protected Component getUI() {
        return panel();
    }

}
