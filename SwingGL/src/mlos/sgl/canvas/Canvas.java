package mlos.sgl.canvas;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Canvas {
    
    private final Set<CanvasObject> objects = new HashSet<>();

    private final Set<CanvasListener> listeners = new CopyOnWriteArraySet<>();
    
    public boolean add(CanvasObject object) {
        boolean added = objects.add(object);
        if (added) {
            signalAddition(object);
        }
        return added;
    }

    private void signalAddition(CanvasObject object) {
        for (CanvasListener listener : listeners) {
            listener.objectAdded(object);
        }
    }
    
    public boolean remove(CanvasObject object) {
        boolean wasPresent = objects.remove(object);
        if (wasPresent) {
            signalRemoval(object);
        }
        return wasPresent;
    }

    private void signalRemoval(CanvasObject object) {
        for (CanvasListener listener : listeners) {
            listener.objectRemoved(object);
        }
    }
    
    public void clear() {
        for (CanvasObject object : objects) {
            signalRemoval(object);
        }
        objects.clear();
    }
    
    public void addAll(Iterable<? extends CanvasObject> iterable) {
        for (CanvasObject object : iterable) {
            add(object);
        }
    }
    
    public Set<CanvasObject> getObjects() {
        return Collections.unmodifiableSet(objects);
    }
    
    public void addListener(CanvasListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(CanvasListener listener) {
        listeners.remove(listener);
    }
    
}
