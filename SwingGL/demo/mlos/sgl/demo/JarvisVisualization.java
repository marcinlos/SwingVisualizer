package mlos.sgl.demo;

import mlos.sgl.Scene;
import mlos.sgl.core.Vec2d;

public class JarvisVisualization extends ConvexHullVisualizer implements
        Jarvis.EventsListener {

    public JarvisVisualization(Scene scene) {
        super(scene);
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
    public void foundNextPoint(Vec2d v) {
        points.push(v);
    }

    @Override
    public void afterIter() {
        redraw();
    }

}
