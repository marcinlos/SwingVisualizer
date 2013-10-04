package mlos.sgl.model;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.google.common.collect.Iterables;

public abstract class CanvasObject {
    
    private final Set<CanvasObjectListener> listeners = new CopyOnWriteArraySet<>();
    
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
    
    public abstract void accept(CanvasVisitor visitor);

}
