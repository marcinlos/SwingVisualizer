package mlos.sgl.ui.modes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasPolygon;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;
import mlos.sgl.view.CanvasView;
import mlos.sgl.view.Drawer;
import mlos.sgl.view.Painter;

public class PolyCreation extends AbstractMode {

    private JPanel optionsPanel = new JPanel();

    private Vec2d currentPos;
    
    private boolean duringCreation = false;
    
    private List<Vec2d> points = new ArrayList<>();
    
    private final Painter currentPainter = new Painter() {

        @Override
        public void paint(Transform toScreen, Graphics2D ctx) {
            Vec2d last = points.get(points.size() - 1);
            
            Drawer d = new Drawer(ctx, toScreen).color(Color.black);
            
            if (points.size() > 1) {
                d.polyLine(points);
            }
            d.dashed(1, 3f, 5f)
                .line(last, currentPos)
                .restore();
        }
    };


    public PolyCreation(Scene scene, CanvasView view) {
        super("Polygons", scene, view);
    }

    @Override
    public Component getOptionPanel() {
        return optionsPanel;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) { 
        int button = e.getButton();
        if (button == MouseEvent.BUTTON1) {
            Vec2d p = getPlanePos(e);
            points.add(p);
            e.consume();
            
            if (!duringCreation) {
                duringCreation = true;
                view.addPostPainter(currentPainter);
            }
        } else if (button == MouseEvent.BUTTON3) {
            CanvasPolygon poly = new CanvasPolygon(points);
            scene.addObject(poly);
            points.clear();
            duringCreation = false;
            
            view.removePostPainter(currentPainter);
            view.refresh();
            e.consume();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (duringCreation) {
            currentPos = getPlanePos(e);
            view.refresh();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (leftButtonDown(e)) {
            if (!duringCreation) {
                duringCreation = true;
                
            }

        }
    }


    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

}
