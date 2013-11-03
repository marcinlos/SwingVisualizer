package mlos.sgl.ui.modes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;
import mlos.sgl.view.Drawer;
import mlos.sgl.view.Painter;

public class SegmentCreation extends AbstractMode {

    private JPanel optionsPanel = new JPanel();

    private Vec2d prevPoint;
    private Vec2d currentPos;
    private boolean dragging = false;
    
    private final Painter currentPainter = new Painter() {

        @Override
        public void paint(Transform toScreen, Graphics2D ctx) {
            new Drawer(ctx, toScreen)
                .color(Color.black)
                .dashed(1, 3f, 5f)
                .line(prevPoint, currentPos)
                .restore();
        }
    };


    public SegmentCreation(Scene scene) {
        super("Create segments", scene);
    }

    @Override
    public Component getOptionPanel() {
        return optionsPanel;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) { 
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            prevPoint = getPlanePos(e);
            e.consume();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Vec2d endScreen = getScreenPos(e);
            Vec2d end = view.planeToScreen().invert(endScreen);
            
            Segment s = new Segment(prevPoint, end);
            CanvasSegment segment = new CanvasSegment(s);
            scene.addObject(segment);
            
            prevPoint = null;
            dragging = false;
            view.removePostPainter(currentPainter);
            view.refresh();
            e.consume();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (leftButtonDown(e)) {
            if (!dragging) {
                dragging = true;
                view.addPostPainter(currentPainter);
            }
            currentPos = getPlanePos(e);
            view.refresh();
            e.consume();
        }
    }
    
}
