package mlos.sgl.demo;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.SwingUtilities;

import mlos.sgl.Scene;

public class AbstractVisualizer {

    public final Scene scene;
    private double speedFactor = 1.0;

    private final Lock lock = new ReentrantLock();
    private final Condition stateChange = lock.newCondition();
    private final Condition changeNoticed = lock.newCondition();
    private boolean running;
    private boolean noticed;

    public AbstractVisualizer(Scene scene) {
        this.scene = scene;
    }

    public void delay(long ms) {
        long actual = (long) (ms / speedFactor);
        try {
            TimeUnit.MILLISECONDS.sleep(actual);
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }

    protected void breakpoint() {
        lock.lock();
        try {
            while (!running) {
                stateChange.await();
            }
            noticed = true;
            changeNoticed.signalAll();
        } catch (InterruptedException e) {
            System.err.println("Interrupted wait");
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void start() {
        lock.lock();
        try {
            running = true;
            stateChange.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void pause() {
        lock.lock();
        try {
            running = false;
            stateChange.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void toggle() {
        lock.lock();
        try {
            running = !running;
            stateChange.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void step() {
        lock.lock();
        try {
            running = true;
            stateChange.signal();
            System.out.println("Waiting for notice");
            while (!noticed) {
                changeNoticed.await();
            }
            System.out.println("Noticed");
            noticed = false;
            running = false;
            stateChange.signalAll();
        } catch (InterruptedException e) {
            System.err.println("Interrupted wait");
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
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

    protected void refresh() {
        scene.getView().refresh();
    }

}
