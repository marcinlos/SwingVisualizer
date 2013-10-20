package mlos.sgl.canvas;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.google.common.collect.Iterables;

public abstract class CanvasObject {
    
    public static final double DEFAULT_Z = 0.5f;
    
    private final Set<CanvasObjectListener> listeners = new CopyOnWriteArraySet<>();

    
    private double z;
    
    private boolean hover;
    
    private boolean selected;
    
    protected CanvasObject(double z) {
        this.z = z;
    }
    
    protected CanvasObject() {
        this(DEFAULT_Z);
    }

    public void addListener(CanvasObjectListener listener) {
        listeners.add(listener);
    }

    public void addAllListeners(Iterable<? extends CanvasObjectListener> iter) {
        Iterables.addAll(listeners, iter);
    }

    public void signalUpdate() {
        for (CanvasObjectListener listener : listeners) {
            listener.updated(this);
        }
    }
    
    public void setZ(double z) {
        this.z = z;
        signalUpdate();
    }
    
    public double getZ() {
        return z;
    }
    
    public boolean isHover() {
        return hover;
    }
    
    public void setHover(boolean hover) {
        this.hover = hover;
        signalUpdate();
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
        signalUpdate();
    }

//    public abstract void accept(CanvasVisitor visitor);
    
}
