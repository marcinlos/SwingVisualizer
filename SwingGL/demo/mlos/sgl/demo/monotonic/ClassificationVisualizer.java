package mlos.sgl.demo.monotonic;

import java.awt.Color;

import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.core.Vec2d;
import mlos.sgl.demo.AbstractVisualizer;

public class ClassificationVisualizer extends AbstractVisualizer implements
        ClassifyVertices.EventListener {
    
    private final CanvasPoint focus = new CanvasPoint();

    public ClassificationVisualizer(Scene scene) {
        super(scene);
    }
    
    protected void signalPoint(Vec2d p, Color c) {
        focus.setPoint(p);
        focus.setColor(c);
        focus.setBorderColor(Color.black);
        focus.setBorderSize(1);
        focus.setSize(18);
        focus.setZ(0.2);
        showFocusPoint();
        focus.signalUpdate();
        delay(200);
        focus.setSize(12);
        focus.signalUpdate();
    }
    
    protected void hideFocusPoint() {
        scene.removeObject(focus);
    }

    protected void showFocusPoint() {
        scene.addObject(focus);
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void examinig(Vec2d v, Vec2d prev, Vec2d next) {
        signalPoint(prev, Color.blue);
        signalPoint(v, Color.red);
        signalPoint(next, Color.blue);
    }
    
    private Color typeToColor(VertexType type) {
        switch (type) {
        case FINAL: return Color.red;
        case INITIAL: return Color.green;
        case JOIN: return Color.blue;
        case SPLIT: return Color.cyan;
        case NORMAL: return Color.darkGray;
        default:
            throw new IllegalArgumentException("Invalid type");
        }
    }

    @Override
    public void classified(Vec2d v, VertexType type) {
        Color col = typeToColor(type);
        signalPoint(v, col);
        delay(800);
        
        CanvasPoint p = new CanvasPoint(v, 0.4);
        p.setColor(col);
        scene.addObject(p);
    }

    @Override
    public void finished() {
        hideFocusPoint();
    }

}
