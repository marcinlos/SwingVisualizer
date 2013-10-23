package mlos.sgl.demo.hoeyshamos;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;
import mlos.sgl.demo.AbstractVisualizer;

public class HoeyShamosVisualizer extends AbstractVisualizer implements
        HoeyShamos.EventListener {

    private final Set<CanvasObject> found = new HashSet<>();

    private final CanvasPoint focus = new CanvasPoint();
    
    private final CanvasSegment line = new CanvasSegment();

    public HoeyShamosVisualizer(Scene scene) {
        super(scene);
        setSpeed(150.3);
        line.setDashed(true);
        scene.addObject(line);
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
        focus.setColor(Color.blue);
        focus.setBorderColor(c);
        focus.setBorderSize(3);
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
    public void finished() {
        delay(1000);
//        for (CanvasObject o : found) {
//            scene.removeObject(o);
//        }
        scene.removeObject(line);
        hideFocusPoint();
    }

    @Override
    public void beginEvent(Event e) {
        signalPoint(e.point, Color.green);
        CanvasPoint p = new CanvasPoint(e.point, 0.4);
        scene.addObject(p);
        found.add(p);
        delay(500);
    }

    @Override
    public void newEvent(Event e) {
        signalPoint(e.point, Color.pink);
        CanvasPoint p = new CanvasPoint(e.point, 0.4);
        scene.addObject(p);
        found.add(p);
        delay(500);
    }

    @Override
    public void lineMoved(double x) {
        Segment s = new Segment(new Vec2d(x, -10), new Vec2d(x, 10));
        line.setSegment(s);
        delay(500);
        refresh();
    }

    @Override
    public void checking(Segment p, Segment q) {
        CanvasSegment first = new CanvasSegment(p);
        first.setZ(0.3);
        first.setColor(Color.cyan);
        first.setThickness(4);
        
        CanvasSegment second = new CanvasSegment(q);
        second.setZ(0.3);
        second.setColor(Color.magenta);
        
        scene.addObject(first);
        scene.addObject(second);
        refresh();
        
        delay(1000);
        
        scene.removeObject(first);
        scene.removeObject(second);
        refresh();
    }

}
