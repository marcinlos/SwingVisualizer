package mlos.sgl.demo;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import mlos.sgl.Scene;

public class AbstractVisualizer {
    
    protected final Scene scene;
    private double speedFactor = 1.0;


    public AbstractVisualizer(Scene scene) {
        this.scene = scene;
    }


    protected void delay(long ms) {
        long actual = (long) (ms / speedFactor);
        try {
            TimeUnit.MILLISECONDS.sleep(actual);
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }
    
    public double getSpeed() {
        return speedFactor;
    }
    
    public void setSpeed(double speed) {
        this.speedFactor = speed;
    }
    

    protected void sync(Runnable action) {
        try {
            SwingUtilities.invokeAndWait(action);
        } catch (InvocationTargetException | InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }
    
}
