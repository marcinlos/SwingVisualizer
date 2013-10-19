package mlos.sgl.demo;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;
import mlos.sgl.demo.Graham.EventsListener;

class GrahamVisualizer implements EventsListener {
    
    private final Scene scene;
    
    private final Deque<CanvasObject> segments = new ArrayDeque<>();
    private final Deque<Vec2d> points = new ArrayDeque<>();
    
    private final CanvasPoint focus = new CanvasPoint();
    private final CanvasSegment pendingSegment = new CanvasSegment();
    
    private boolean afterPop = false;
    
    public GrahamVisualizer(Scene scene) {
        this.scene = scene;
        
        pendingSegment.setDashed(true);
    }

    @Override
    public void foundBase(Vec2d v) {
        scene.addObject(focus);
        signalPoint(v);
    }

    @Override
    public void sorted(Vec2d[] points) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void initStack(Vec2d p0, Vec2d p1, Vec2d p2) {
        Collections.addAll(points, p2, p1, p0);
        CanvasPoint[] ps = { 
            makeCanvasPoint(p0), 
            makeCanvasPoint(p1), 
            makeCanvasPoint(p2) 
        };
        for (CanvasPoint p : ps) {
            scene.addObject(p);
        }
        draw();
        delay(1000);
    }

    @Override
    public void nextPoint(Vec2d p) {
        scene.addObject(focus);
        if (! afterPop) signalPoint(p);
        
        Segment seg = new Segment(points.peek(), p);
        
        pendingSegment.setSegment(seg);
        pendingSegment.setColor(Color.black);
        scene.addObject(pendingSegment);
        delay(300);
    }

    private void signalPoint(Vec2d p) {
        focus.setPoint(p);
        focus.setColor(Color.green);
        focus.setSize(16);
        focus.setZ(0.2);
        scene.addObject(focus);
        delay(200);
        focus.setColor(Color.blue);
        focus.setSize(12);
    }
    

    @Override
    public void beforeIter() {
        if (! afterPop) {
            delay(400);
        }
    }

    @Override
    public void push(Vec2d p) {
        points.push(p);
        afterPop = false;
        pendingSegment.setColor(Color.green);
        afterPointDecision();
    }

    @Override
    public void pop() {
        points.pop();
        afterPop = true;
        pendingSegment.setColor(Color.red);
        afterPointDecision();
    }

    private void afterPointDecision() {
        delay(500);
        scene.removeObject(pendingSegment);
    }

    @Override
    public void afterIter() {
        draw();

        scene.removeObject(focus);
        scene.getView().refresh();
    }

    @Override
    public void finished() {
        removeAll();
    }
    
    void removeAll() {
        for (CanvasObject o : segments) {
            scene.removeObject(o);
        }
    }
    
    void addAll() {
        for (CanvasObject o : segments) {
            scene.addObject(o);
        }
    }
    
    private void delay(long ms) {
        long actual = (long) (1 * ms);
        try {
            TimeUnit.MILLISECONDS.sleep(actual);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    Deque<CanvasObject> create() {
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
        System.out.println(objects.size());
        return objects;
    }

    private CanvasPoint makeCanvasPoint(Vec2d q) {
        CanvasPoint canvasPoint = new CanvasPoint(q);
        canvasPoint.setColor(Color.blue);
        canvasPoint.setZ(0.2);
        return canvasPoint;
    }
    
    private static void sync(Runnable task) {
        try {
            SwingUtilities.invokeAndWait(task);
        } catch (InvocationTargetException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static void async(Runnable task) {
        SwingUtilities.invokeLater(task);
    }
    
    private void draw() {
        async(new Runnable() {
            
            @Override
            public void run() {
                removeAll();
                segments.clear();
                if (!points.isEmpty()) {
                    segments.addAll(create());
                    addAll();
                }
            }
        });
    }
    
}