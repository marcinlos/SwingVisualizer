package mlos.sgl.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class PropertyMap {
    
    private final Map<String, Object> properties = new HashMap<>();

    private final Set<PropertyListener> listeners = new CopyOnWriteArraySet<>();
    
    public <T> void put(String name, T value) {
        properties.put(name, value);
        for (PropertyListener listener : listeners) {
            listener.changed(name, value);
        }
    }
    
    public <T> T get(String name, Class<T> type) {
        Object o = properties.get(name);
        return o != null ? type.cast(o) : null;
    }
    
    public void remove(String name) {
        properties.remove(name);
        for (PropertyListener listener : listeners) {
            listener.removed(name);
        }
    }
    
    public void addListener(PropertyListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(PropertyListener listener) {
        listeners.remove(listener);
    }
}
