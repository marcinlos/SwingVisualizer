package mlos.sgl.ui;

import static mlos.sgl.core.Geometry.*;

import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Vec2d;
import mlos.sgl.core.ScreenTransform;
import mlos.sgl.core.Segment;

public class SegmentGeometry implements ObjectGeometry {
    
    private final CanvasSegment segment;
    
    public SegmentGeometry(CanvasSegment segment) {
        this.segment = segment;
    }

    @Override
    public boolean hit(Vec2d p, ScreenTransform transform, int treshold) {
        
        Segment seg = segment.getSegment();
        Vec2d a = transform.toScreen(seg.a);
        Vec2d b = transform.toScreen(seg.b);
        
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
