package mlos.sgl.demo;

import mlos.sgl.Scene;
import mlos.sgl.core.Vec2d;

public class JarvisVisualization extends ConvexHullVisualizer implements
        Jarvis.EventsListener {

    public JarvisVisualization(Scene scene) {
        super(scene);
    }

    @Override
    public void foundBase(Vec2d v) {
        scene.addObject(focus);
        signalPoint(v);
    }

    @Override
    public void beforeIter() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void foundNextPoint(Vec2d v) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterIter() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void finished() {
        // TODO Auto-generated method stub
        
    }

}
