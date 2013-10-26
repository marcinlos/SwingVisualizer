package mlos.sgl.demo.hull;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;
import mlos.sgl.demo.AbstractVisualizer;

public class ConvexHullVisualizer extends AbstractVisualizer {

    protected final Deque<CanvasObject> graphics = new ArrayDeque<>();
    protected final Deque<Vec2d> points = new ArrayDeque<>();
    protected final CanvasPoint focus = new CanvasPoint();

    public ConvexHullVisualizer(Scene scene) {
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

    private void removeAll() {
        for (CanvasObject o : graphics) {
            scene.removeObject(o);
        }
    }

    private void addAll() {
        for (CanvasObject o : graphics) {
            scene.addObject(o);
        }
    }

    private CanvasPoint makeCanvasPoint(Vec2d q) {
        CanvasPoint canvasPoint = new CanvasPoint(q);
        canvasPoint.setColor(Color.blue);
        canvasPoint.setZ(0.2);
        return canvasPoint;
    }
    

    private Deque<CanvasObject> create() {
        Deque<CanvasObject> objects = new ArrayDeque<>();
        Iterator<Vec2d> it = points.iterator();

        Vec2d p = it.next();
        objects.add(makeCanvasPoint(p));

        while (it.hasNext()) {
            Vec2d q = it.next();

            objects.add(makeCanvasPoint(q));

            Segment seg = new Segment(p, q);
            CanvasSegment canvasSegment = new CanvasSegment(seg);
            canvasSegment.setZ(0.3);
            objects.add(canvasSegment);
            p = q;
        }
        return objects;
    }
    

    protected void redraw() {
        sync(new Runnable() {

            @Override
            public void run() {
                removeAll();
                graphics.clear();
                if (!points.isEmpty()) {
                    graphics.addAll(create());
                    addAll();
                }
                refresh();
            }

        });
    }
    
    public void finished() {
        sync(new Runnable() {

            @Override
            public void run() {
                removeAll();
                hideFocusPoint();
                refresh();
            }

        });
    }

    protected void hideFocusPoint() {
        scene.removeObject(focus);
    }

    protected void showFocusPoint() {
        scene.addObject(focus);
    }

}