package mlos.sgl.ui;

import static mlos.sgl.core.Geometry.diff;
import static mlos.sgl.core.Geometry.dist;
import static mlos.sgl.core.Geometry.dot;
import static mlos.sgl.core.Geometry.lerp;
import static mlos.sgl.core.Geometry.normSq;
import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;

public class SegmentGeometry implements ObjectGeometry {
    
    private final CanvasSegment segment;
    
    public SegmentGeometry(CanvasSegment segment) {
        this.segment = segment;
    }

    @Override
    public boolean hit(Vec2d p, Transform screen, int treshold) {
        
        Segment seg = segment.getSegment();
        Vec2d a = screen.apply(seg.a);
        Vec2d b = screen.apply(seg.b);
        
        return distance(a, b, p) < treshold;
    }
    
    private Vec2d closest(Vec2d a, Vec2d b, Vec2d p) {
        Vec2d ab = diff(b, a);
        Vec2d ap = diff(p, a);
        double t = dot(ab, ap) / normSq(ab);
        if (0 <= t && t <= 1) {
            return lerp(t, a, b);
        } else {
            return t < 0 ? a : b;
        }
    }
    
    private double distance(Vec2d a, Vec2d b, Vec2d p) {
        return dist(p, closest(a, b, p));
    }
    
    @Override
    public CanvasSegment getObject() {
        return segment;
    }

}
