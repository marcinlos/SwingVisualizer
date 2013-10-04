package mlos.sgl.model;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.google.common.collect.Iterables;

public abstract class CanvasObject {
    
    public static final double DEFAULT_Z = 0.0f;
    
    private final Set<CanvasObjectListener> listeners = new CopyOnWriteArraySet<>();

    private double z;
    
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

    protected void notifyListeners() {
        for (CanvasObjectListener listener : listeners) {
            listener.updated(this);
        }
    }
    
    public final double getZ() {
        return z;
    }

    public abstract void accept(CanvasVisitor visitor);

}
