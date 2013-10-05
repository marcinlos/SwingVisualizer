package mlos.sgl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import mlos.sgl.canvas.Canvas;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.ui.CanvasController;
import mlos.sgl.ui.DefaultObjectGeometryFactory;
import mlos.sgl.ui.ObjectGeometryFactory;
import mlos.sgl.view.CanvasPanel;
import mlos.sgl.view.CanvasView;
import mlos.sgl.view.DefaultObjectViewFactory;
import mlos.sgl.view.ObjectViewFactory;
import mlos.sgl.view.Painter;
import mlos.sgl.view.ScreenPoint;

public abstract class Scene {
    
    private String name;
    
    private final Canvas canvas;
    
    private final CanvasPanel canvasPanel;

    private final CanvasView view;
    
    private final CanvasController controller;
    
    
    
    private ScreenPoint cursor = new ScreenPoint(0, 0);
    
    private class ScenePainter implements Painter {
        
        private final Painter next;
        
        public ScenePainter(Painter next) {
            this.next = checkNotNull(next);
        }

        @Override
        public void paint(CanvasPanel canvas, Graphics2D ctx) {
            String text = "Pos: " +canvas.toVirtual(cursor);
            
            ctx.setColor(Color.black);
            ctx.drawString(text, 8, 15);
            next.paint(canvas, ctx);
        }
        
    }
    

    public Scene(String name) {
        this(name, 1, 1);
    }
    
    public Scene(String name, double width, double height) {
        this.name = checkNotNull(name);
        this.canvas = new Canvas();
        this.view = new CanvasView(canvas, createViewFactory());
        
        Painter painter = createPainter(this.view);
        this.canvasPanel = new CanvasPanel(painter, width, height);
        this.controller = new CanvasController(canvas, createGeometryFactory());
        
        canvasPanel.addMouseMotionListener(new MouseMotionListener() {
            
            @Override
            public void mouseMoved(MouseEvent e) {
                Point cur = e.getPoint();
                cursor = new ScreenPoint(cur.x, cur.y);
                canvasPanel.refresh();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                
            }
        });
    }

    public void addObject(CanvasObject object) {
        canvas.add(object);
    }

    protected Painter createPainter(Painter base) {
        Painter painter = new ScenePainter(base);
        return painter;
    }
    
    protected ObjectViewFactory createViewFactory() {
        return new DefaultObjectViewFactory();
    }
    
    protected ObjectGeometryFactory createGeometryFactory() {
        return new DefaultObjectGeometryFactory();
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
    
    protected Painter getPainter(Painter painter) {
        return canvasPanel.getPainter();
    }
    
    protected void setPainter(Painter painter) {
        canvasPanel.setPainter(painter);
    }
    

}
