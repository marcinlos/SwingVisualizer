package mlos.sgl.demo.hull;

import java.awt.Color;

import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;

public class JarvisVisualizer extends ConvexHullVisualizer implements
        Jarvis.EventsListener {
    
    private final CanvasSegment toBestCandidate = new CanvasSegment();
    private final CanvasSegment toNewCandidate = new CanvasSegment();
    
    private final CanvasPoint bestCandidateMark = new CanvasPoint();

    public JarvisVisualizer(Scene scene) {
        super(scene);
        toBestCandidate.setDashed(true);
        toNewCandidate.setDashed(true);
        
        bestCandidateMark.setColor(Color.blue);
        bestCandidateMark.setSize(10);
        bestCandidateMark.setZ(0.4);
        
        setSpeed(5);
    }

    @Override
    public void foundBase(Vec2d v) {
        points.push(v);
        signalPoint(v);
    }
    
    @Override
    public void beforeIter() {
        delay(1000);
    }
    
    @Override
    public void firstCandidate(Vec2d v) {
        newCandidate(v);
        
        bestCandidateMark.setPoint(v);
        scene.addObject(bestCandidateMark);
        
        toBestCandidate.setSegment(new Segment(points.peek(), v));
        scene.addObject(toBestCandidate);
        refresh();
    }

    @Override
    public void newCandidate(Vec2d v) {
        signalPoint(v);
        
        Segment seg = new Segment(points.peek(), v);
        toNewCandidate.setSegment(seg);
        toNewCandidate.setColor(Color.black);
        scene.addObject(toNewCandidate);
        delay(200);
    }

    @Override
    public void wasWorse() {
        toNewCandidate.setColor(Color.red);
        delay(300);
        scene.removeObject(toNewCandidate);
    }

    @Override
    public void wasBetter() {
        toNewCandidate.setColor(Color.green);
        delay(300);
        scene.removeObject(toNewCandidate);
        
        Segment seg = toNewCandidate.getSegment();
        bestCandidateMark.setPoint(seg.b);
        
        toBestCandidate.setSegment(seg);
    }

    @Override
    public void foundNextPoint(Vec2d v) {
        points.push(v);
    }

    @Override
    public void afterIter() {
        redraw();
    }
    
    @Override
    public void finished() {
        super.finished();
        scene.removeObject(bestCandidateMark);
        scene.removeObject(toBestCandidate);
        scene.removeObject(toNewCandidate);
    }

}
