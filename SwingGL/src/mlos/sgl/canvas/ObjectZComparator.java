package mlos.sgl.canvas;

import java.util.Comparator;

public class ObjectZComparator implements Comparator<CanvasObject> {
    
    public static final ObjectZComparator INSTANCE = new ObjectZComparator();
    
    @Override
    public int compare(CanvasObject a, CanvasObject b) {
        int res = Double.compare(b.getZ(), a.getZ());
        if (res != 0) {
            return res;
        } else {
            return Integer.compare(a.hashCode(), b.hashCode());
        }
    }
}