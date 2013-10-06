package mlos.sgl.ui;

import static mlos.sgl.core.Geom.*;

import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Point;
import mlos.sgl.core.ScreenTransform;
import mlos.sgl.core.Segment;

public class SegmentGeometry implements ObjectGeometry {
    
    private final CanvasSegment segment;
    
    public SegmentGeometry(CanvasSegment segment) {
        this.segment = segment;
    }

    @Override
    public boolean hit(Point p, ScreenTransform transform, int treshold) {
        
        Segment seg = segment.getSegment();
        Point a = transform.toScreen(seg.a);
        Point b = transform.toScreen(seg.b);
        
        return distance(a, b, p) < treshold;
    }
    
    private Point closest(Point a, Point b, Point p) {
        Point ab = diff(b, a);
        Point ap = diff(p, a);
        double t = dot(ab, ap) / normSq(ab);
        if (0 <= t && t <= 1) {
            return lerp(t, a, b);
        } else {
            return t < 0 ? a : b;
        }
    }
    
    private double distance(Point a, Point b, Point p) {
        return dist(p, closest(a, b, p));
    }
    
    @Override
    public CanvasSegment getObject() {
        return segment;
    }

}
