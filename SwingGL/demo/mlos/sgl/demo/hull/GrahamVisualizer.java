package mlos.sgl.demo.hull;

import java.awt.Color;
import java.util.Collections;

import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;

class GrahamVisualizer extends ConvexHullVisualizer implements
        Graham.EventsListener {

    private final CanvasSegment pendingSegment = new CanvasSegment();

    private boolean afterPop = false;

    public GrahamVisualizer(Scene scene) {
        super(scene);
        setSpeed(1);
        pendingSegment.setDashed(true);
    }

    @Override
    public void foundBase(Vec2d v) {
        signalPoint(v);
    }

    @Override
    public void sorted(Vec2d[] points, int n) {

    }

    @Override
    public void initStack(Vec2d p0, Vec2d p1, Vec2d p2) {
        Collections.addAll(points, p2, p1, p0);
        redraw();
        delay(1000);
    }

    @Override
    public void nextPoint(Vec2d p) {
        showFocusPoint();
        if (!afterPop) {
            signalPoint(p);
        }

        Segment seg = new Segment(points.peek(), p);

        pendingSegment.setSegment(seg);
        pendingSegment.setColor(Color.black);
        scene.addObject(pendingSegment);
        delay(300);
    }
    
    @Override
    public void beforeIter() {
        if (!afterPop) {
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
        refresh();
    }

    @Override
    public void afterIter() {
        redraw();
    }

}