package mlos.sgl.util;

public interface PropertyListener {

    void changed(String name, Object newValue);
    
    void removed(String name);
    
}
