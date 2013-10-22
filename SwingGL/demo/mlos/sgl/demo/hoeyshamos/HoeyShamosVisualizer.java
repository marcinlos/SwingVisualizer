package mlos.sgl.demo.hoeyshamos;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;
import mlos.sgl.demo.AbstractVisualizer;

public class HoeyShamosVisualizer extends AbstractVisualizer implements
        HoeyShamos.EventListener {

    private final Set<CanvasObject> found = new HashSet<>();

    private final CanvasPoint focus = new CanvasPoint();

    public HoeyShamosVisualizer(Scene scene) {
        super(scene);
    }

    protected void signalPoint(Vec2d p) {
        focus.setPoint(p);
        focus.setColor(Color.green);
        focus.setBorderColor(Color.black);
        focus.setBorderSize(1);
        focus.setSize(18);
        focus.setZ(0.2);
        showFocusPoint();
        focus.signalUpdate();
        delay(200);
        focus.setColor(Color.blue);
        focus.setBorderColor(Color.green);
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
    public void intersection(Vec2d point, Segment a, Segment b) {
        signalPoint(point);
        CanvasPoint p = new CanvasPoint(point, 0.4);
        scene.addObject(p);
        found.add(p);
        delay(500);
    }

    @Override
    public void finished() {
        delay(1000);
//        for (CanvasObject o : found) {
//            scene.removeObject(o);
//        }
        hideFocusPoint();
    }

}
