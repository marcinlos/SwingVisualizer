package mlos.sgl.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Iterables;

public class Canvas {
    
    private final Set<CanvasObject> objects = new HashSet<>();

    
    public void add(CanvasObject object) {
        objects.add(object);
    }
    
    public void remove(CanvasObject object) {
        objects.remove(object);
    }
    
    public void clear() {
        objects.clear();
    }
    
    public void addAll(Iterable<? extends CanvasObject> items) {
        Iterables.addAll(objects, items);
    }
    
    public Set<CanvasObject> getObjects() {
        return Collections.unmodifiableSet(objects);
    }
    
}
