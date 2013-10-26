package mlos.sgl.demo.hull;

import java.util.Collection;

import mlos.sgl.core.Vec2d;

public class ConvexHullAlgorithm {
    
    protected final Vec2d[] V;
    protected final int n;
    
    public ConvexHullAlgorithm(Collection<Vec2d> objects) {
        n = objects.size();
        if (n < 3) {
            String msg = String.format("Need at least 3 points, %d got", n);
            throw new IllegalArgumentException(msg);
        }
        V = objects.toArray(new Vec2d[n]);    
    }

}
