package mlos.sgl.ui.modes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;
import mlos.sgl.ui.InputHandler;
import mlos.sgl.view.CanvasView;
import mlos.sgl.view.Painter;

public class SegmentCreation implements Mode, InputHandler {
    
    private final CanvasView view;


    private JPanel optionsPanel = new JPanel();
    
    private final Scene scene;

    private Vec2d startPos;
    private Vec2d currentPos;
    private boolean dragging = false;
    
    private final Painter selectionPainter = new Painter() {

        @Override
        public void paint(Transform toScreen, Graphics2D ctx) {
            Vec2d start = toScreen.apply(startPos);
            Vec2d current = toScreen.apply(currentPos);

            int x1 = (int) start.x;
            int y1 = (int) start.y;
            int x2 = (int) current.x;
            int y2 = (int) current.y;
            
            ctx.setColor(Color.black);

            Stroke old = ctx.getStroke();
            
            BasicStroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, 
                    BasicStroke.JOIN_BEVEL, 1, new float[]{3f, 5f}, 0);
            
            ctx.setStroke(dashed);
            ctx.drawLine(x1, y1, x2, y2);
            ctx.setStroke(old);
        }
    };


    public SegmentCreation(Scene scene, CanvasView view) {
        this.scene = scene;
        this.view = view;
    }

    @Override
    public String getName() {
        return "Create segments";
    }

    @Override
    public InputHandler getHandler() {
        return this;
    }

    @Override
    public Component getOptionPanel() {
        return optionsPanel;
    }
    
    private Vec2d getScreenPos(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        return new Vec2d(p.x, p.y);
    }

    private Vec2d getPlanePos(MouseEvent e) {
        Vec2d screenPos = getScreenPos(e);
        return view.planeToScreen().invert(screenPos);
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            startPos = getPlanePos(e);
            e.consume();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Vec2d endScreen = getScreenPos(e);
            Vec2d end = view.planeToScreen().invert(endScreen);
            
            Segment s = new Segment(startPos, end);
            CanvasSegment segment = new CanvasSegment(s);
            scene.addObject(segment);
            
            startPos = null;
            dragging = false;
            view.removePostPainter(selectionPainter);
            view.refresh();
            e.consume();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (leftButtonDown(e)) {
            if (!dragging) {
                dragging = true;
                view.addPostPainter(selectionPainter);
            }
            currentPos = getPlanePos(e);
            view.refresh();
            e.consume();
        }
    }

    private boolean leftButtonDown(MouseEvent e) {
        return (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0;
    }
    
    private boolean rightButtonDown(MouseEvent e) {
        return (e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) != 0;
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
