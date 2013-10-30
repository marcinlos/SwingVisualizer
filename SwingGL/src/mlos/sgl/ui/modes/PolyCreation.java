package mlos.sgl.ui.modes;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.round;
import static mlos.sgl.core.Geometry.diff;
import static mlos.sgl.core.Geometry.mul;
import static mlos.sgl.core.Geometry.norm;
import static mlos.sgl.core.Geometry.sum;

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
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();
        if (button == MouseEvent.BUTTON1) {
            Vec2d p = getCursorPos(getPlanePos(e), e.isControlDown());
            points.add(p);
            e.consume();
            
            if (!duringCreation) {
                duringCreation = true;
                view.addPostPainter(currentPainter);
            }
        } else if (button == MouseEvent.BUTTON3) {
            if (duringCreation) {
                CanvasPolygon poly = new CanvasPolygon(points);
                scene.addObject(poly);
                points.clear();
                duringCreation = false;
                
                view.removePostPainter(currentPainter);
                view.refresh();
                e.consume();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (duringCreation) {
            Vec2d pos = getPlanePos(e);
            currentPos = getCursorPos(pos, e.isControlDown());
            view.refresh();
        }
    }

    public Vec2d getCursorPos(Vec2d pos, boolean snap) {
        if (snap && !points.isEmpty()) {
            Vec2d prev = points.get(points.size() - 1);
            Vec2d d = diff(pos, prev);
            double len = norm(d);
            double theta = atan2(d.y, d.x);
            if (theta < 0) {
                theta += 2 * PI;
            }
            int dir = (int) round(4 * theta / PI);
            dir = dir % 8;

            Vec2d unit = null;
            if (dir % 2 == 0) {
                int i = dir / 2;
                switch (i) {
                case 0: unit = new Vec2d(1, 0); break;
                case 1: unit = new Vec2d(0, 1); break;
                case 2: unit = new Vec2d(-1, 0); break;
                case 3: unit = new Vec2d(0, -1); break;
                }
            } else {
                double a = Math.sqrt(0.5);
                int i = (dir - 1) / 2;
                switch (i) {
                case 0: unit = new Vec2d(a, a); break;
                case 1: unit = new Vec2d(-a, a); break;
                case 2: unit = new Vec2d(-a, -a); break;
                case 3: unit = new Vec2d(a, -a); break;
                }
            }
            return sum(prev, mul(len, unit));
        }
        return pos;
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
