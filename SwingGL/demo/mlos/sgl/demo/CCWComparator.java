package mlos.sgl.demo;

import static mlos.sgl.core.Geometry.distSq;
import static mlos.sgl.core.Geometry.orient2d;

import java.util.Comparator;

import mlos.sgl.core.Vec2d;

public final class CCWComparator implements Comparator<Vec2d> {

    private final Vec2d origin;
    
    public CCWComparator(Vec2d origin) {
        this.origin = origin;
    }
    
    private int signum(double r) {
        return r == 0 ? 0 : r < 0 ? -1 : 1;
    }
    
    @Override
    public int compare(Vec2d o1, Vec2d o2) {
        if (o1.equals(o2)) {
            return 0;
        } else {
            double r = orient2d(origin, o2, o1);
            if (r == 0) {
                double d1 = distSq(origin, o1);
                double d2 = distSq(origin, o2);
                return Double.compare(d1, d2);
            }
            return signum(r);
        }
    }
    
}