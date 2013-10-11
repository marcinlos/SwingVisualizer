package mlos.sgl.ui;

import static mlos.sgl.core.Geometry.clamp;
import static mlos.sgl.core.Geometry.diff;
import static mlos.sgl.core.Geometry.dist;
import static mlos.sgl.core.Geometry.dot;
import static mlos.sgl.core.Geometry.lerp;
import static mlos.sgl.core.Geometry.normSq;
import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;

public class SegmentController implements ObjectController {
    
    private final CanvasSegment segment;
    
    public SegmentController(CanvasSegment segment) {
        this.segment = segment;
    }

    private Vec2d closest(Segment s, Vec2d p) {
        Vec2d ab = diff(s.b, s.a);
        Vec2d ap = diff(p, s.a);
        double t = dot(ab, ap) / normSq(ab);
        return lerp(clamp(t, 0, 1), s);
    }
    
    private double distance(Segment s, Vec2d p) {
        return dist(p, closest(s, p));
    }
    
    @Override
    public CanvasSegment getObject() {
        return segment;
    }

    @Override
    public double distance(Vec2d p, Transform planeToScreen) {
        Segment seg = segment.getSegment().apply(planeToScreen);
        return distance(seg, p);
    }

}
